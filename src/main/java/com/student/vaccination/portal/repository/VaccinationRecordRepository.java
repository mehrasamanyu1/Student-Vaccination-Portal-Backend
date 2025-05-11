package com.student.vaccination.portal.repository;

import com.student.vaccination.portal.entity.Student;
import com.student.vaccination.portal.entity.VaccinationDrive;
import com.student.vaccination.portal.entity.VaccinationRecord;
import com.student.vaccination.portal.enums.VaccineStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {
    boolean existsByStudentAndDriveAndStatus(Student student, VaccinationDrive drive, VaccineStatus status);
    List<VaccinationRecord> findByStudent_StudentId(String studentId);
    List<VaccinationRecord> findAll();

    Page<VaccinationRecord> findByDrive_IdAndStatus(Long driveId, VaccineStatus vaccineStatus, Pageable pageable);
    List<VaccinationRecord> findByDrive_IdAndStatus(Long driveId, VaccineStatus vaccineStatus);

    long countByDrive_IdAndStatus(Long id, VaccineStatus vaccineStatus);

    List<VaccinationRecord> findByStatus(VaccineStatus vaccineStatus);

    List<VaccinationRecord> findByStudent_StudentIdAndDrive_Id(String studentId, Long id);

    List<VaccinationRecord> findByDrive_IdAndDrive_VaccineNameAndStatus(Long driveId, String vaccineName, VaccineStatus vaccineStatus);

    @Query("SELECT COUNT(DISTINCT vr.student.id) FROM VaccinationRecord vr WHERE vr.status = :status")
    long countDistinctStudentIdsByStatus(@Param("status") VaccineStatus status);

    void deleteByStudent_StudentIdAndDrive_Id(String studentId, Long id);
}
