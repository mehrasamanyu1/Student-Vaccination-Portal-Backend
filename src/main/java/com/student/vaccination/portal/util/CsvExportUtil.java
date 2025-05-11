package com.student.vaccination.portal.util;

import com.student.vaccination.portal.dto.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.List;

public class CsvExportUtil {

    public static ByteArrayInputStream exportDriveReport(List<VaccinationReportDTO> data) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader("Student Name", "Class", "Vaccine Name", "Vaccination Date","Vaccine Status","Drive Status");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(new PrintWriter(out), format)) {

            for (VaccinationReportDTO r : data) {
                printer.printRecord(r.getStudentName(), r.getClassName(), r.getVaccineName(), r.getVaccinationDate(),r.getVaccineStatus(),r.getDriveStatus());
            }

            printer.flush();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generating CSV", e);
        }
    }

    public static ByteArrayInputStream exportClassReport(List<StudentVaccinationStatusDTO> data) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader(
                "Student Name", "Student Class", "Vaccine Name", "Vaccination Date", "Vaccination Status", "Drive Status"
        );

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(new PrintWriter(out), format)) {

            for (StudentVaccinationStatusDTO student : data) {
                List<VaccinationDriveStatusDTO> vData = student.getVaccinationStatuses();

                for (VaccinationDriveStatusDTO v : vData) {
                    printer.printRecord(
                            student.getStudentName(),
                            student.getStudentClass(),
                            v.getDriveName(),
                            v.getVaccinationDate() != null ? v.getVaccinationDate().toString() : "N/A",
                            v.getVaccinationStatus(),
                            v.getDriveStatus().toString()
                    );
                }
            }

            printer.flush();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generating CSV", e);
        }
    }

    public static ByteArrayInputStream exportDriveSummary(List<DriveSummaryDTO> data) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader("Vaccine Name", "Drive Date", "Drive Status", "Total Doses","Used Doses", "Remaining Doses");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(new PrintWriter(out), format)) {

            for (DriveSummaryDTO d : data) {
                printer.printRecord(d.getVaccineName(),d.getDriveDate(),d.getDriveStatus().toString(),d.getTotalDoses(),d.getUsedDoses(),d.getRemainingDoses());
            }

            printer.flush();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generating CSV", e);
        }
    }

    public static ByteArrayInputStream exportMissedVaccinations(List<MissedVaccinationDTO> data) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader("Student Name", "Student Class", "Vaccine Name", "Drive Date");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(new PrintWriter(out), format)) {

            for (MissedVaccinationDTO m : data) {
                printer.printRecord(m.getStudentName(),m.getStudentClass(), m.getVaccineName(),m.getDriveDate());
            }

            printer.flush();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Error generating CSV", e);
        }
    }
}
