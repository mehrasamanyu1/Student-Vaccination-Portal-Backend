package com.student.vaccination.portal.service;

import com.student.vaccination.portal.dto.DashboardStatsDTO;
import com.student.vaccination.portal.dto.VaccinationReportDTO;
import com.student.vaccination.portal.enums.VaccineStatus;
import com.student.vaccination.portal.repository.StudentRepository;
import com.student.vaccination.portal.repository.VaccinationDriveRepository;
import com.student.vaccination.portal.repository.VaccinationRecordRepository;
import com.student.vaccination.portal.util.CsvExportUtil;
import com.student.vaccination.portal.util.ExcelExportUtil;
import com.student.vaccination.portal.util.PdfExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    private final StudentRepository studentRepo;
    private final VaccinationRecordRepository recordRepo;
    private final VaccinationDriveRepository driveRepo;

    @Autowired
    public DashboardService(StudentRepository studentRepo,
                            VaccinationRecordRepository recordRepo,
                            VaccinationDriveRepository driveRepo) {
        this.studentRepo = studentRepo;
        this.recordRepo = recordRepo;
        this.driveRepo = driveRepo;
    }

    public DashboardStatsDTO getDashboardStats() {
        long totalStudents = studentRepo.count();
        long vaccinatedStudents = recordRepo.countDistinctStudentIdsByStatus(VaccineStatus.VACCINATED);
        long upcomingDrives = driveRepo.countByDriveDateAfter(LocalDate.now());

        return new DashboardStatsDTO(totalStudents, vaccinatedStudents, upcomingDrives);
    }
}
