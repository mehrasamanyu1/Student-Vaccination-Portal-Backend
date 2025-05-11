package com.student.vaccination.portal.service;

import com.student.vaccination.portal.dto.MissedVaccinationDTO;
import com.student.vaccination.portal.dto.StudentVaccinationStatusDTO;
import com.student.vaccination.portal.dto.VaccinationDriveStatusDTO;
import com.student.vaccination.portal.dto.VaccinationReportDTO;
import com.student.vaccination.portal.entity.Student;
import com.student.vaccination.portal.entity.VaccinationDrive;
import com.student.vaccination.portal.entity.VaccinationRecord;
import com.student.vaccination.portal.enums.VaccineStatus;
import com.student.vaccination.portal.repository.StudentRepository;
import com.student.vaccination.portal.repository.VaccinationDriveRepository;
import com.student.vaccination.portal.repository.VaccinationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VaccinationRecordService {

    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private VaccinationDriveRepository vaccinationDriveRepository;

    public boolean alreadyVaccinated(Student student, VaccinationDrive drive) {
        return vaccinationRecordRepository
                .existsByStudentAndDriveAndStatus(student, drive, VaccineStatus.VACCINATED);
    }

    public List<VaccinationRecord> findByStudentId(String studentId) {
        return vaccinationRecordRepository.findByStudent_StudentId(studentId);
    }

    public List<VaccinationRecord> findAll() {
        return vaccinationRecordRepository.findAll();
    }

    public void updateVaccinationRecord(Student student, VaccinationDrive drive) {
        List<VaccinationRecord> list = vaccinationRecordRepository.findByStudent_StudentId(student.getStudentId());
        for (VaccinationRecord record : list) {
            if (record.getDrive().getId().equals(drive.getId())) {
                record.setStatus(VaccineStatus.VACCINATED);
                record.setVaccinationDate(LocalDate.now());
                vaccinationRecordRepository.save(record);
            }
        }
    }

    public Page<VaccinationReportDTO> getVaccinationReportByDrive(Long driveId, Pageable pageable) {
        Page<VaccinationRecord> recordsPage = vaccinationRecordRepository.
                findByDrive_IdAndStatus(driveId, VaccineStatus.VACCINATED, pageable);

        List<VaccinationReportDTO> report = recordsPage.stream().map(record -> {

            VaccinationReportDTO dto = new VaccinationReportDTO();

            dto.setStudentName(record.getStudent().getName());
            dto.setClassName(record.getStudent().getStudentClass());
            dto.setVaccineName(record.getDrive().getVaccineName());
            dto.setVaccinationDate(record.getVaccinationDate());
            dto.setVaccineStatus(record.getStatus());
            dto.setDriveStatus(record.getDrive().getStatus());

            return dto;

        }).toList();

        return new PageImpl<>(report, pageable, recordsPage.getTotalElements());
    }

    public List<StudentVaccinationStatusDTO> getVaccinationStatusByClass(String className) {
        List<Student> students = studentRepository.findByStudentClass(className);
        List<StudentVaccinationStatusDTO> report = new ArrayList<>();

        for (Student student : students) {
            List<VaccinationDrive> drives = vaccinationDriveRepository.findByApplicableClasses(className);

            StudentVaccinationStatusDTO statusDTO = new StudentVaccinationStatusDTO();
            statusDTO.setStudentName(student.getName());
            statusDTO.setStudentClass(student.getStudentClass());

            List<VaccinationDriveStatusDTO> driveStatuses = new ArrayList<>();

            for (VaccinationDrive drive : drives) {
                List<VaccinationRecord> records = vaccinationRecordRepository.
                        findByStudent_StudentIdAndDrive_Id(student.getStudentId(), drive.getId());
                VaccinationDriveStatusDTO driveStatusDTO = new VaccinationDriveStatusDTO();

                VaccinationRecord record = records.get(0);
                String status = record.getStatus() == VaccineStatus.VACCINATED ? "VACCINATED" : "PENDING";

                driveStatusDTO.setDriveName(drive.getVaccineName());
                driveStatusDTO.setVaccinationStatus(status);
                driveStatusDTO.setVaccinationDate(record.getVaccinationDate());
                driveStatusDTO.setDriveStatus(drive.getStatus());

                driveStatuses.add(driveStatusDTO);
            }

            statusDTO.setVaccinationStatuses(driveStatuses);

            report.add(statusDTO);
        }

        return report;
    }

    public List<MissedVaccinationDTO> getMissedVaccinations() {
        List<VaccinationRecord> pendingRecords = vaccinationRecordRepository.findByStatus
                (VaccineStatus.PENDING);
        List<MissedVaccinationDTO> missedReport = new ArrayList<>();

        for (VaccinationRecord record : pendingRecords) {
            MissedVaccinationDTO missedDTO = new MissedVaccinationDTO();

            missedDTO.setStudentName(record.getStudent().getName());
            missedDTO.setStudentClass(record.getStudent().getStudentClass());
            missedDTO.setDriveDate(record.getDrive().getDriveDate());
            missedDTO.setVaccineName(record.getDrive().getVaccineName());

            missedReport.add(missedDTO);
        }

        return missedReport;
    }

    public List<VaccinationReportDTO> getVaccinationReportByDriveAndVaccine(Long driveId, String vaccineName) {
        List<VaccinationRecord> records;

        if (vaccineName != null && !vaccineName.isBlank()) {
            records = vaccinationRecordRepository.findByDrive_IdAndDrive_VaccineNameAndStatus(
                    driveId, vaccineName, VaccineStatus.VACCINATED);
        } else {
            records = vaccinationRecordRepository.findByDrive_IdAndStatus(
                    driveId, VaccineStatus.VACCINATED);
        }

        return records.stream().map(record -> {
            VaccinationReportDTO dto = new VaccinationReportDTO();

            dto.setStudentName(record.getStudent().getName());
            dto.setClassName(record.getStudent().getStudentClass());
            dto.setVaccineName(record.getDrive().getVaccineName());
            dto.setVaccinationDate(record.getVaccinationDate());
            dto.setVaccineStatus(record.getStatus());
            dto.setDriveStatus(record.getDrive().getStatus());

            return dto;
        }).toList();
    }
}
