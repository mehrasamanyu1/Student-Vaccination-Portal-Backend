package com.student.vaccination.portal.dto;

import com.student.vaccination.portal.entity.VaccinationDrive;
import com.student.vaccination.portal.enums.DriveStatus;
import com.student.vaccination.portal.enums.VaccineStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccinationReportDTO {

    private String studentName;
    private String className;
    private String vaccineName;
    private LocalDate vaccinationDate;
    private VaccineStatus vaccineStatus;
    private DriveStatus driveStatus;

    public DriveStatus getDriveStatus() {
        return driveStatus;
    }
    public void setDriveStatus(DriveStatus status) {
        this.driveStatus=status;
    }

    public VaccineStatus getVaccineStatus() {
        return vaccineStatus;
    }

    public void setVaccineStatus(VaccineStatus vaccineStatus) {
        this.vaccineStatus = vaccineStatus;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public LocalDate getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }
}
