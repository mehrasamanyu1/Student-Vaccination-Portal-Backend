package com.student.vaccination.portal.dto;

import com.student.vaccination.portal.entity.VaccinationDrive;
import com.student.vaccination.portal.enums.DriveStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DriveSummaryDTO {

    private Long driveId;
    private String vaccineName;
    private long totalDoses;
    private long usedDoses;
    private long remainingDoses;
    private LocalDate driveDate;
    private DriveStatus driveStatus;

    public DriveStatus getDriveStatus() {
        return driveStatus;
    }

    public void setDriveStatus(DriveStatus driveStatus) {
        this.driveStatus = driveStatus;
    }

    public LocalDate getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(LocalDate driveDate) {
        this.driveDate = driveDate;
    }

    public Long getDriveId() {
        return driveId;
    }

    public void setDriveId(Long driveId) {
        this.driveId = driveId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public long getTotalDoses() {
        return totalDoses;
    }

    public void setTotalDoses(long totalDoses) {
        this.totalDoses = totalDoses;
    }

    public long getUsedDoses() {
        return usedDoses;
    }

    public void setUsedDoses(long usedDoses) {
        this.usedDoses = usedDoses;
    }

    public long getRemainingDoses() {
        return remainingDoses;
    }

    public void setRemainingDoses(long remainingDoses) {
        this.remainingDoses = remainingDoses;
    }
}
