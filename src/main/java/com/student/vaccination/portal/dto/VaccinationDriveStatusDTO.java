package com.student.vaccination.portal.dto;

import com.student.vaccination.portal.entity.VaccinationDrive;
import com.student.vaccination.portal.enums.DriveStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccinationDriveStatusDTO {

    private String driveName;
    private String vaccinationStatus;
    private LocalDate vaccinationDate;
    private DriveStatus driveStatus;

    public DriveStatus getDriveStatus() {
        return driveStatus;
    }

    public void setDriveStatus(DriveStatus driveStatus) {
        this.driveStatus = driveStatus;
    }

    public String getDriveName() {
        return driveName;
    }

    public void setDriveName(String driveName) {
        this.driveName = driveName;
    }

    public String getVaccinationStatus() {
        return vaccinationStatus;
    }

    public void setVaccinationStatus(String vaccinationStatus) {
        this.vaccinationStatus = vaccinationStatus;
    }

    public LocalDate getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }
}
