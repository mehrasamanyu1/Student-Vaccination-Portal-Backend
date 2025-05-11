package com.student.vaccination.portal.util;

import com.student.vaccination.portal.dto.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class PdfExportUtil {
    public static ByteArrayInputStream exportDriveReport(List<VaccinationReportDTO> data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><head>")
                    .append("<style>")
                    .append("table { width: 100%; border-collapse: collapse; }")
                    .append("th, td { padding: 10px; text-align: left; border: 1px solid #ddd; }")
                    .append("th { background-color: #f2f2f2; font-weight: bold; }")
                    .append("tr:nth-child(even) { background-color: #f9f9f9; }")
                    .append("</style>")
                    .append("</head><body>")
                    .append("<h1>Drive-Wise Vaccination Report</h1>")
                    .append("<table>")
                    .append("<thead><tr>")
                    .append("<th>Student Name</th>")
                    .append("<th>Student Class</th>")
                    .append("<th>Vaccine Name</th>")
                    .append("<th>Vaccination Date</th>")
                    .append("<th>Vaccination Status</th>")
                    .append("<th>Drive Status</th>")
                    .append("</tr></thead><tbody>");

            for (VaccinationReportDTO r : data) {
                htmlContent.append("<tr>")
                        .append("<td>").append(r.getStudentName()).append("</td>")
                        .append("<td>").append(r.getClassName()).append("</td>")
                        .append("<td>").append(r.getVaccineName()).append("</td>")
                        .append("<td>").append(r.getVaccinationDate() != null ? r.getVaccinationDate().toString() : "N/A").append("</td>")
                        .append("<td>").append(r.getVaccineStatus().toString()).append("</td>")
                        .append("<td>").append(r.getDriveStatus().toString()).append("</td>")
                        .append("</tr>");
            }

            htmlContent.append("</tbody></table></body></html>");

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent.toString());
            renderer.layout();
            renderer.createPDF(out);

            byte[] fileBytes = out.toByteArray();

            return new ByteArrayInputStream(fileBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    public static ByteArrayInputStream exportClassReport(List<StudentVaccinationStatusDTO> data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><head>")
                    .append("<style>")
                    .append("table { width: 100%; border-collapse: collapse; }")
                    .append("th, td { padding: 10px; text-align: left; border: 1px solid #ddd; }")
                    .append("th { background-color: #f2f2f2; font-weight: bold; }")
                    .append("tr:nth-child(even) { background-color: #f9f9f9; }")
                    .append("</style>")
                    .append("</head><body>")
                    .append("<h1>Class-Wise Vaccination Report</h1>")
                    .append("<table>")
                    .append("<thead><tr>")
                    .append("<th>Student Name</th>")
                    .append("<th>Student Class</th>")
                    .append("<th>Vaccine Name</th>")
                    .append("<th>Vaccination Date</th>")
                    .append("<th>Vaccination Status</th>")
                    .append("<th>Drive Status</th>")
                    .append("</tr></thead><tbody>");

            for (StudentVaccinationStatusDTO student : data) {
                for (VaccinationDriveStatusDTO v : student.getVaccinationStatuses()) {

                    htmlContent.append("<tr>")
                            .append("<td>").append(student.getStudentName()).append("</td>")
                            .append("<td>").append(student.getStudentClass()).append("</td>")
                            .append("<td>").append(v.getDriveName()).append("</td>")
                            .append("<td>").append(v.getVaccinationDate() != null ? v.getVaccinationDate().toString() : "N/A").append("</td>")
                            .append("<td>").append(v.getVaccinationStatus()).append("</td>")
                            .append("<td>").append(v.getDriveStatus().toString()).append("</td>")
                            .append("</tr>");
                }
            }

            htmlContent.append("</tbody></table></body></html>");

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent.toString());
            renderer.layout();
            renderer.createPDF(out);

            byte[] fileBytes = out.toByteArray();

            return new ByteArrayInputStream(fileBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    public static ByteArrayInputStream exportDriveSummary(List<DriveSummaryDTO> data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><head>")
                    .append("<style>")
                    .append("table { width: 100%; border-collapse: collapse; }")
                    .append("th, td { padding: 10px; text-align: left; border: 1px solid #ddd; }")
                    .append("th { background-color: #f2f2f2; font-weight: bold; }")
                    .append("tr:nth-child(even) { background-color: #f9f9f9; }")
                    .append("</style>")
                    .append("</head><body>")
                    .append("<h1>Drive Summary Report</h1>")
                    .append("<table>")
                    .append("<thead><tr>")
                    .append("<th>Vaccine Name</th>")
                    .append("<th>Drive Date</th>")
                    .append("<th>Drive Status</th>")
                    .append("<th>Total Doses</th>")
                    .append("<th>Used Doses</th>")
                    .append("<th>Remaining Doses</th>")
                    .append("</tr></thead><tbody>");

            for (DriveSummaryDTO d : data) {
                htmlContent.append("<tr>")
                        .append("<td>").append(d.getVaccineName()).append("</td>")
                        .append("<td>").append(d.getDriveDate()).append("</td>")
                        .append("<td>").append(d.getDriveStatus()).append("</td>")
                        .append("<td>").append(d.getTotalDoses()).append("</td>")
                        .append("<td>").append(d.getUsedDoses()).append("</td>")
                        .append("<td>").append(d.getRemainingDoses()).append("</td>")
                        .append("</tr>");
            }

            htmlContent.append("</tbody></table></body></html>");

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent.toString());
            renderer.layout();
            renderer.createPDF(out);

            byte[] fileBytes = out.toByteArray();

            return new ByteArrayInputStream(fileBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

    }

    public static ByteArrayInputStream exportMissedVaccinations(List<MissedVaccinationDTO> data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html><head>")
                    .append("<style>")
                    .append("table { width: 100%; border-collapse: collapse; }")
                    .append("th, td { padding: 10px; text-align: left; border: 1px solid #ddd; }")
                    .append("th { background-color: #f2f2f2; font-weight: bold; }")
                    .append("tr:nth-child(even) { background-color: #f9f9f9; }")
                    .append("</style>")
                    .append("</head><body>")
                    .append("<h1>Students Missed Vaccination Report</h1>")
                    .append("<table>")
                    .append("<thead><tr>")
                    .append("<th>Student Name</th>")
                    .append("<th>Student Class</th>")
                    .append("<th>Vaccine Name</th>")
                    .append("<th>Drive Date</th>")
                    .append("</tr></thead><tbody>");

            for (MissedVaccinationDTO m : data) {
                htmlContent.append("<tr>")
                        .append("<td>").append(m.getStudentName()).append("</td>")
                        .append("<td>").append(m.getStudentClass()).append("</td>")
                        .append("<td>").append(m.getVaccineName()).append("</td>")
                        .append("<td>").append(m.getDriveDate().toString()).append("</td>")
                        .append("</tr>");
            }

            htmlContent.append("</tbody></table></body></html>");

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent.toString());
            renderer.layout();
            renderer.createPDF(out);

            byte[] fileBytes = out.toByteArray();

            return new ByteArrayInputStream(fileBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

    }

}
