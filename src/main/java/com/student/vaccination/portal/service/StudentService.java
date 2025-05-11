package com.student.vaccination.portal.service;

import com.student.vaccination.portal.dto.StudentDTO;
import com.student.vaccination.portal.entity.Student;
import com.student.vaccination.portal.entity.VaccinationDrive;
import com.student.vaccination.portal.entity.VaccinationRecord;
import com.student.vaccination.portal.enums.VaccineStatus;
import com.student.vaccination.portal.repository.StudentRepository;
import com.student.vaccination.portal.repository.VaccinationDriveRepository;
import com.student.vaccination.portal.repository.VaccinationRecordRepository;
import com.student.vaccination.portal.util.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private VaccinationDriveRepository vaccinationDriveRepository;

    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;

    public StudentDTO addStudent(StudentDTO dto) {
        if (studentRepository.existsByStudentId(dto.getStudentId())) {
            return null;
        }

        // Add the new student
        Student student = new Student();
        student.setStudentId(dto.getStudentId());
        student.setName(dto.getName());
        student.setStudentClass(dto.getStudentClass());
        student.setGender(dto.getGender());
        student.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth()));

        studentRepository.save(student);

        generateVaccinationRecordsForStudent(dto, student);

        return dto;
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(s -> {
                    StudentDTO dto = new StudentDTO();
                    dto.setStudentId(s.getStudentId());
                    dto.setName(s.getName());
                    dto.setStudentClass(s.getStudentClass());
                    dto.setGender(s.getGender());
                    dto.setDateOfBirth(s.getDateOfBirth().toString());
                    return dto;
                })
                .sorted(Comparator.comparing(StudentDTO::getStudentId))
                .collect(Collectors.toList());
    }

    public void updateStudent(String studentId, StudentDTO dto) {
        Student student = findById(studentId);
        String oldClass = student.getStudentClass();
        String newClass = dto.getStudentClass();

        student.setName(dto.getName());
        student.setStudentClass(newClass);
        student.setGender(dto.getGender());
        student.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth()));

        studentRepository.save(student);

        if (!oldClass.equals(newClass)) {
            List<VaccinationDrive> oldClassDrives = vaccinationDriveRepository.findByApplicableClasses(oldClass);
            for (VaccinationDrive drive : oldClassDrives) {
                vaccinationRecordRepository.deleteByStudent_StudentIdAndDrive_Id(studentId, drive.getId());
            }

            List<VaccinationDrive> newClassDrives = vaccinationDriveRepository.findByApplicableClasses(newClass);
            for (VaccinationDrive drive : newClassDrives) {
                VaccinationRecord record = new VaccinationRecord();
                record.setStudent(student);
                record.setDrive(drive);
                record.setVaccineName(drive.getVaccineName());
                record.setStatus(VaccineStatus.PENDING);
                record.setVaccinationDate(null);

                vaccinationRecordRepository.save(record);
            }
        }
    }

    public Student findById(String studentId) {
        Optional<Student> studentOpt = studentRepository.findByStudentId(studentId);
        return studentOpt.orElse(null);
    }

    public void bulkUploadStudents(MultipartFile file) {
        List<StudentDTO> students = CSVHelper.parseStudentsCsv(file);
        DateTimeFormatter CSV_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (StudentDTO dto : students) {
            if (!studentRepository.existsByStudentId(dto.getStudentId())) {
                Student student = new Student();
                student.setStudentId(dto.getStudentId());
                student.setName(dto.getName());
                student.setStudentClass(dto.getStudentClass());
                student.setGender(dto.getGender());
                student.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth(), CSV_DATE_FORMAT));

                studentRepository.save(student);

                generateVaccinationRecordsForStudent(dto, student);
            }
        }
    }

    public List<StudentDTO> searchStudents(String name, String studentClass, String studentId, Boolean vaccinated) {
        List<Student> students = studentRepository.searchStudents(name, studentClass, studentId, vaccinated);

        return students.stream().map(student -> {
            StudentDTO dto = new StudentDTO();

            dto.setStudentId(student.getStudentId());
            dto.setName(student.getName());
            dto.setStudentClass(student.getStudentClass());
            dto.setGender(student.getGender());
            dto.setDateOfBirth(student.getDateOfBirth().toString());

            return dto;
        }).collect(Collectors.toList());
    }

    public void deleteStudent(Student student) {
        studentRepository.delete(student);
    }

    private void generateVaccinationRecordsForStudent(StudentDTO dto, Student student) {
        List<VaccinationDrive> drives = vaccinationDriveRepository.findByApplicableClasses(dto.getStudentClass());
        List<VaccinationRecord> records = new ArrayList<>();

        for (VaccinationDrive drive : drives) {
            VaccinationRecord record = new VaccinationRecord();
            record.setStudent(student);
            record.setDrive(drive);
            record.setVaccineName(drive.getVaccineName());
            record.setStatus(VaccineStatus.PENDING);
            record.setVaccinationDate(null); // will be set when vaccinated

            records.add(record);
        }

        vaccinationRecordRepository.saveAll(records); // Save all the records for this student
    }

}
