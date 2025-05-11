package com.student.vaccination.portal.util;

import com.student.vaccination.portal.dto.*;
import com.student.vaccination.portal.enums.VaccineStatus;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExcelExportUtil {

    public static ByteArrayInputStream exportDriveReport(List<VaccinationReportDTO> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Drive-Wise Vaccination Report");

            Row header = sheet.createRow(0);
            String[] columns = {"Student Name", "Student Class", "Vaccine Name", "Vaccination Date", "Vaccination Status", "Drive Status"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (VaccinationReportDTO r : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(r.getStudentName());
                row.createCell(1).setCellValue(r.getClassName());
                row.createCell(2).setCellValue(r.getVaccineName());
                row.createCell(3).setCellValue(r.getVaccinationDate().toString());
                row.createCell(4).setCellValue(r.getVaccineStatus().toString());
                row.createCell(5).setCellValue(r.getDriveStatus().toString());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel", e);
        }
    }

    public static ByteArrayInputStream exportClassReport(List<StudentVaccinationStatusDTO> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Class-Wise Vaccination Report");

            Row header = sheet.createRow(0);
            String[] columns = {"Student Name", "Student Class", "Vaccine Name", "Vaccination Date", "Vaccination Status", "Drive Status"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowIdx = 1;

            for (StudentVaccinationStatusDTO student : data) {
                List<VaccinationDriveStatusDTO> vData = student.getVaccinationStatuses();

                for (VaccinationDriveStatusDTO v : vData) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(student.getStudentName());
                    row.createCell(1).setCellValue(student.getStudentClass());
                    row.createCell(2).setCellValue(v.getDriveName());
                    row.createCell(3).setCellValue(v.getVaccinationDate() != null ? v.getVaccinationDate().toString() : "N/A");
                    row.createCell(4).setCellValue(v.getVaccinationStatus());
                    row.createCell(5).setCellValue(v.getDriveStatus().toString());
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel", e);
        }
    }

    public static ByteArrayInputStream exportDriveSummary(List<DriveSummaryDTO> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Drive Summary Report");

            Row header = sheet.createRow(0);
            String[] columns = {"Vaccine Name", "Drive Date", "Drive Status", "Total Doses", "Used Doses", "Remaining Doses"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

            int rowIdx = 1;
            for (DriveSummaryDTO d : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(d.getVaccineName());

                Cell dateCell = row.createCell(1);
                dateCell.setCellValue(d.getDriveDate());
                dateCell.setCellStyle(dateCellStyle);

                row.createCell(2).setCellValue(d.getDriveStatus().toString());
                row.createCell(3).setCellValue(d.getTotalDoses());
                row.createCell(4).setCellValue(d.getUsedDoses());
                row.createCell(5).setCellValue(d.getRemainingDoses());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel", e);
        }
    }

    public static ByteArrayInputStream exportMissedVaccinations(List<MissedVaccinationDTO> data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Students Missed Vaccinations Report");

            Row header = sheet.createRow(0);
            String[] columns = {"Student Name", "Student Class", "Vaccine Name", "Drive Date"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (MissedVaccinationDTO m : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(m.getStudentName());
                row.createCell(1).setCellValue(m.getStudentClass());
                row.createCell(2).setCellValue(m.getVaccineName());
                row.createCell(3).setCellValue(m.getDriveDate().toString());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel", e);
        }
    }

}
