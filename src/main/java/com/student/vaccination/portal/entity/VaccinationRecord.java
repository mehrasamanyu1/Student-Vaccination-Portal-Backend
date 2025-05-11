package com.student.vaccination.portal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.student.vaccination.portal.enums.VaccineStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "vaccination_records",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "vaccine_name"}))
public class VaccinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vaccineName;
    private LocalDate vaccinationDate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "drive_id")
    @JsonIgnore
    private VaccinationDrive drive;

    @Enumerated(EnumType.STRING)
    private VaccineStatus status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public VaccinationDrive getDrive() {
        return drive;
    }

    public void setDrive(VaccinationDrive drive) {
        this.drive = drive;
    }

    public VaccineStatus getStatus() {
        return status;
    }

    public void setStatus(VaccineStatus status) {
        this.status = status;
    }
}
