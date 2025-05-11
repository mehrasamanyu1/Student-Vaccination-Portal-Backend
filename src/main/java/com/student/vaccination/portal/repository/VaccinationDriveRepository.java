package com.student.vaccination.portal.repository;

import com.student.vaccination.portal.entity.VaccinationDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccinationDriveRepository extends JpaRepository<VaccinationDrive, Long> {
    List<VaccinationDrive> findByApplicableClasses(String className);
    long countByDriveDateAfter(LocalDate date);
}
