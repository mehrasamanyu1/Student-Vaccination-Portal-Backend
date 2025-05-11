package com.student.vaccination.portal.service;

import com.student.vaccination.portal.dto.DriveSummaryDTO;
import com.student.vaccination.portal.entity.Student;
import com.student.vaccination.portal.entity.VaccinationDrive;
import com.student.vaccination.portal.entity.VaccinationRecord;
import com.student.vaccination.portal.enums.VaccineStatus;
import com.student.vaccination.portal.repository.StudentRepository;
import com.student.vaccination.portal.repository.VaccinationDriveRepository;
import com.student.vaccination.portal.repository.VaccinationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VaccinationDriveService {

    @Autowired
    private VaccinationDriveRepository vaccinationDriveRepository;

    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;

    @Autowired
    private StudentRepository studentRepository;

    public VaccinationDrive findById(Long driveId) {
        Optional<VaccinationDrive> vaccinationDriveOpt = vaccinationDriveRepository.findById(driveId);
        return vaccinationDriveOpt.orElse(null);
    }

    public boolean createDrive(VaccinationDrive newDrive) {
        List<VaccinationDrive> existingDrives = vaccinationDriveRepository.findAll();
        for (VaccinationDrive drive : existingDrives) {
            long days = Math.abs(ChronoUnit.DAYS.between(drive.getDriveDate(), newDrive.getDriveDate()));
            if (days < 15) {
                return false;
            }
        }

        // Fetch students matching the applicable classes
        List<Student> eligibleStudents = studentRepository.findByStudentClassIn(newDrive.getApplicableClasses());

        List<VaccinationRecord> records = new ArrayList<>();

        for (Student student : eligibleStudents) {
            VaccinationRecord record = new VaccinationRecord();
            record.setStudent(student);
            record.setDrive(newDrive);
            record.setVaccineName(newDrive.getVaccineName());
            record.setStatus(VaccineStatus.PENDING);
            record.setVaccinationDate(null); // will be set when vaccinated

            records.add(record);
        }

        newDrive.setRecords(records);
        vaccinationDriveRepository.save(newDrive);
        return true;
    }

    public boolean updateDrive(VaccinationDrive existingDrive, VaccinationDrive updatedDrive) {

        boolean anyVaccinated = existingDrive.getRecords().stream()
                .anyMatch(record -> record.getStatus() == VaccineStatus.VACCINATED);

        if (existingDrive.getDriveDate().isBefore(LocalDate.now()) || anyVaccinated) {
            existingDrive.setEditable(false);
            vaccinationDriveRepository.save(existingDrive);
            return false;
        }

        List<VaccinationDrive> allDrives = vaccinationDriveRepository.findAll();
        for (VaccinationDrive drive : allDrives) {
            if (!drive.getId().equals(existingDrive.getId())) {
                long days = Math.abs(ChronoUnit.DAYS.between(drive.getDriveDate(), updatedDrive.getDriveDate()));
                if (days < 15) return false;
            }
        }

        existingDrive.setDriveDate(updatedDrive.getDriveDate());
        existingDrive.setVaccineName(updatedDrive.getVaccineName());
        existingDrive.setAvailableDoses(updatedDrive.getAvailableDoses());
        existingDrive.setApplicableClasses(updatedDrive.getApplicableClasses());
        existingDrive.setStatus(updatedDrive.getStatus());

        List<VaccinationRecord> existingRecords = existingDrive.getRecords();

        List<VaccinationRecord> recordsToRemove = existingRecords.stream()
                .filter(record -> !updatedDrive.getApplicableClasses().contains(record.getStudent().getStudentClass()))
                .collect(Collectors.toList());

        existingRecords.removeAll(recordsToRemove);

        vaccinationRecordRepository.deleteAll(recordsToRemove);

        List<Student> allStudents = studentRepository.findAll();
        for (Student student : allStudents) {
            if (updatedDrive.getApplicableClasses().contains(student.getStudentClass())) {
                boolean alreadyExists = existingRecords.stream().anyMatch(r -> r.getStudent().equals(student));
                if (!alreadyExists) {
                    VaccinationRecord newRecord = new VaccinationRecord();
                    newRecord.setStudent(student);
                    newRecord.setDrive(existingDrive);
                    newRecord.setVaccineName(existingDrive.getVaccineName());
                    newRecord.setVaccinationDate(null);
                    newRecord.setStatus(VaccineStatus.PENDING);

                    existingRecords.add(newRecord);
                }
            }
        }

        existingDrive.setRecords(existingRecords);

        vaccinationDriveRepository.save(existingDrive);
        return true;
    }

    public List<VaccinationDrive> getAllDrives() {
        List<VaccinationDrive> drives = vaccinationDriveRepository.findAll();

        for (VaccinationDrive drive : drives) {
            List<VaccinationRecord> records = drive.getRecords();
            drive.setEditable(records.stream().noneMatch(vaccinationRecord -> vaccinationRecord.getStatus().equals(VaccineStatus.VACCINATED)));

            vaccinationDriveRepository.save(drive);
        }

        return drives;
    }

    public List<DriveSummaryDTO> getDriveSummary() {
        List<VaccinationDrive> drives = vaccinationDriveRepository.findAll();
        List<DriveSummaryDTO> summary = new ArrayList<>();

        for (VaccinationDrive drive : drives) {
            long totalUsedDoses = vaccinationRecordRepository.countByDrive_IdAndStatus(drive.getId(), VaccineStatus.VACCINATED);

            DriveSummaryDTO dto = new DriveSummaryDTO();

            dto.setDriveId(drive.getId());
            dto.setVaccineName(drive.getVaccineName());
            dto.setTotalDoses(drive.getAvailableDoses());
            dto.setUsedDoses(totalUsedDoses);
            dto.setRemainingDoses(drive.getAvailableDoses() - totalUsedDoses);
            dto.setDriveDate(drive.getDriveDate());
            dto.setDriveStatus(drive.getStatus());

            summary.add(dto);
        }

        return summary;
    }
}