package com.student.vaccination.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashboardStatsDTO {
    private long totalStudents;
    private long vaccinatedStudents;
    private long upcomingDrives;

    public DashboardStatsDTO(long totalStudents, long vaccinatedStudents, long upcomingDrives) {
        this.totalStudents = totalStudents;
        this.vaccinatedStudents = vaccinatedStudents;
        this.upcomingDrives = upcomingDrives;
    }

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public long getVaccinatedStudents() { return vaccinatedStudents; }
    public void setVaccinatedStudents(long vaccinatedStudents) { this.vaccinatedStudents = vaccinatedStudents; }

    public long getUpcomingDrives() { return upcomingDrives; }
    public void setUpcomingDrives(long upcomingDrives) { this.upcomingDrives = upcomingDrives; }
}
