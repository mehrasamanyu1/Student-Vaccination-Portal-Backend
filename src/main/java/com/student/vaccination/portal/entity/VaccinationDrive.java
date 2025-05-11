package com.student.vaccination.portal.entity;

import com.student.vaccination.portal.enums.DriveStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vaccination_drives")
public class VaccinationDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vaccineName;
    private LocalDate driveDate;
    private int availableDoses;

    @Enumerated(EnumType.STRING)
    private DriveStatus status = DriveStatus.PENDING;

    public DriveStatus getStatus() {
        return status;
    }

    public void setStatus(DriveStatus status) {
        this.status = status;
    }

    @ElementCollection
    @CollectionTable(name = "drive_classes", joinColumns = @JoinColumn(name = "drive_id"))
    @Column(name = "student_class")
    private List<String> applicableClasses;

    private boolean editable = true;

    @OneToMany(mappedBy = "drive", cascade = CascadeType.ALL)
    private List<VaccinationRecord> records = new ArrayList<>();

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

    public LocalDate getDriveDate() {
        return driveDate;
    }

    public void setDriveDate(LocalDate driveDate) {
        this.driveDate = driveDate;
    }

    public int getAvailableDoses() {
        return availableDoses;
    }

    public void setAvailableDoses(int availableDoses) {
        this.availableDoses = availableDoses;
    }

    public List<String> getApplicableClasses() {
        return applicableClasses;
    }

    public void setApplicableClasses(List<String> applicableClasses) {
        this.applicableClasses = applicableClasses;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public List<VaccinationRecord> getRecords() {
        return records;
    }

    public void setRecords(List<VaccinationRecord> records) {
        this.records = records;
    }

}
