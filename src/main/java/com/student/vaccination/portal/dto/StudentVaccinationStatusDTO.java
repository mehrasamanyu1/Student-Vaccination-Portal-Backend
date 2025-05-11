package com.student.vaccination.portal.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentVaccinationStatusDTO {

    private String studentName;
    private String studentClass;
    private List<VaccinationDriveStatusDTO> vaccinationStatuses;
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public List<VaccinationDriveStatusDTO> getVaccinationStatuses() {
        return vaccinationStatuses;
    }

    public void setVaccinationStatuses(List<VaccinationDriveStatusDTO> vaccinationStatuses) {
        this.vaccinationStatuses = vaccinationStatuses;
    }
}
