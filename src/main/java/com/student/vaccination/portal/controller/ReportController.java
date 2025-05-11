package com.student.vaccination.portal.controller;

import com.student.vaccination.portal.dto.*;
import com.student.vaccination.portal.enums.ExportFormat;
import com.student.vaccination.portal.service.DashboardService;
import com.student.vaccination.portal.service.StudentService;
import com.student.vaccination.portal.service.VaccinationDriveService;
import com.student.vaccination.portal.service.VaccinationRecordService;
import com.student.vaccination.portal.util.CsvExportUtil;
import com.student.vaccination.portal.util.ExcelExportUtil;
import com.student.vaccination.portal.util.PdfExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private VaccinationRecordService vaccinationRecordService;

    @Autowired
    private VaccinationDriveService vaccinationDriveService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/drive/{driveId}")
    public ResponseEntity<Page<VaccinationReportDTO>> getDriveReport(
            @PathVariable Long driveId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("vaccinationDate").descending());
        Page<VaccinationReportDTO> report = vaccinationRecordService.getVaccinationReportByDrive(driveId, pageable);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/class/{className}")
    public ResponseEntity<List<StudentVaccinationStatusDTO>> getClassReport(@PathVariable String className) {
        List<StudentVaccinationStatusDTO> report = vaccinationRecordService.getVaccinationStatusByClass(className);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/drive-summary")
    public ResponseEntity<List<DriveSummaryDTO>> getDriveSummary() {
        List<DriveSummaryDTO> summary = vaccinationDriveService.getDriveSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/missed-vaccinations")
    public ResponseEntity<List<MissedVaccinationDTO>> getMissedVaccinations() {
        List<MissedVaccinationDTO> missedReport = vaccinationRecordService.getMissedVaccinations();
        return ResponseEntity.ok(missedReport);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReport(
            @RequestParam String reportType,
            @RequestParam(required = false) Long driveId,
            @RequestParam(required = false) String className,
            @RequestParam ExportFormat format) {

        byte[] fileBytes;
        String contentType;
        String fileName;

        switch (reportType) {
            case "drive-report" -> {
                List<VaccinationReportDTO> data = vaccinationRecordService
                        .getVaccinationReportByDriveAndVaccine(driveId, null);

                if(format == ExportFormat.CSV) {
                    fileBytes   = CsvExportUtil.exportDriveReport(data).readAllBytes();
                    contentType = "text/csv";
                    fileName    = "drive-report.csv";
                } else if(format == ExportFormat.EXCEL) {
                    fileBytes   = ExcelExportUtil.exportDriveReport(data).readAllBytes();
                    contentType ="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    fileName    = "drive-report.xlsx";
                } else {
                    fileBytes   = PdfExportUtil.exportDriveReport(data).readAllBytes();
                    contentType = "application/pdf";
                    fileName    = "drive-report.pdf";
                }
            }

            case "class-report" -> {
                List<StudentVaccinationStatusDTO> data =
                        vaccinationRecordService.getVaccinationStatusByClass(className);

                if(format == ExportFormat.CSV) {
                    fileBytes   = CsvExportUtil.exportClassReport(data).readAllBytes();
                    contentType = "text/csv";
                    fileName    = "class-report.csv";
                } else if(format == ExportFormat.EXCEL) {
                    fileBytes   = ExcelExportUtil.exportClassReport(data).readAllBytes();
                    contentType =
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    fileName    = "class-report.xlsx";
                } else {
                    fileBytes   = PdfExportUtil.exportClassReport(data).readAllBytes();
                    contentType = "application/pdf";
                    fileName    = "class-report.pdf";
                }
            }

            case "drive-summary" -> {
                var data = vaccinationDriveService.getDriveSummary();

                if (format == ExportFormat.CSV) {
                    fileBytes   = CsvExportUtil.exportDriveSummary(data).readAllBytes();
                    contentType = "text/csv";
                    fileName    = "drive-summary.csv";
                } else if (format == ExportFormat.EXCEL) {
                    fileBytes   = ExcelExportUtil.exportDriveSummary(data).readAllBytes();
                    contentType =
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    fileName    = "drive-summary.xlsx";
                } else {
                    fileBytes   = PdfExportUtil.exportDriveSummary(data).readAllBytes();
                    contentType = "application/pdf";
                    fileName    = "drive-summary.pdf";
                }
            }

            case "missed-vaccinations" -> {
                var data = vaccinationRecordService.getMissedVaccinations();

                if (format == ExportFormat.CSV) {
                    fileBytes   = CsvExportUtil.exportMissedVaccinations(data).readAllBytes();
                    contentType = "text/csv";
                    fileName    = "missed-vaccinations.csv";
                } else if (format == ExportFormat.EXCEL) {
                    fileBytes   = ExcelExportUtil.exportMissedVaccinations(data).readAllBytes();
                    contentType =
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    fileName    = "missed-vaccinations.xlsx";
                } else {
                    fileBytes   = PdfExportUtil.exportMissedVaccinations(data).readAllBytes();
                    contentType = "application/pdf";
                    fileName    = "missed-vaccinations.pdf";
                }
            }

            default -> throw new IllegalArgumentException("Unsupported export format");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(fileBytes.length)
                .body(fileBytes);

    }

    @GetMapping("/stats")
    public DashboardStatsDTO getDashboardStats() {
        return dashboardService.getDashboardStats();
    }
}
