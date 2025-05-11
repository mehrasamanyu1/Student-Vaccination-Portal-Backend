package com.student.vaccination.portal.controller;

import com.student.vaccination.portal.entity.Student;
import com.student.vaccination.portal.entity.VaccinationDrive;
import com.student.vaccination.portal.entity.VaccinationRecord;
import com.student.vaccination.portal.enums.DriveStatus;
import com.student.vaccination.portal.jwt.JwtRequestValidator;
import com.student.vaccination.portal.repository.VaccinationDriveRepository;
import com.student.vaccination.portal.service.StudentService;
import com.student.vaccination.portal.service.VaccinationDriveService;
import com.student.vaccination.portal.service.VaccinationRecordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vaccination")
public class VaccinationController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private VaccinationDriveService vaccinationDriveService;

    @Autowired
    private VaccinationRecordService vaccinationRecordService;

    @Autowired
    private VaccinationDriveRepository vaccinationDriveRepository;

    @Autowired
    JwtRequestValidator jwtRequestValidator;

    @PostMapping("/drive")
    public ResponseEntity<String> createVaccinationDrive(HttpServletRequest request, @RequestBody VaccinationDrive drive) {
        if (!jwtRequestValidator.hasRole(request, "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Role or Token expired.");
        }

        boolean status = vaccinationDriveService.createDrive(drive);
        if (status)
            return ResponseEntity.status(HttpStatus.CREATED).body("Vaccination drive created successfully.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Drive date conflicts with another drive scheduled within 15 days.");

    }

    @GetMapping("/drives")
    public List<VaccinationDrive> getAllDrives() {
        return vaccinationDriveService.getAllDrives();
    }

    @PutMapping("/drive/{driveId}")
    public ResponseEntity<String> updateDrive(@PathVariable Long driveId, @RequestBody VaccinationDrive updatedDrive) {
        VaccinationDrive vacationDrive = vaccinationDriveService.findById(driveId);
        if (vacationDrive == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Drive not found.");

        boolean updated = vaccinationDriveService.updateDrive(vacationDrive, updatedDrive);

        if (updated) {
            return ResponseEntity.ok("Drive updated successfully.");
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Drive is in past or not editable.");
    }

    @GetMapping("/drive/{id}")
    public ResponseEntity<?> getDriveById(@PathVariable Long id) {
        VaccinationDrive drive = vaccinationDriveService.findById(id);
        if (drive == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Drive with id " + id + " not found.");

        return ResponseEntity.ok(drive);
    }

    @PutMapping("/drive/{id}/status")
    public ResponseEntity<?> updateDriveStatus(@PathVariable Long id, @RequestParam DriveStatus status) {
        VaccinationDrive drive = vaccinationDriveService.findById(id);

        if (drive == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Drive with id " + id + " not found.");

        drive.setStatus(status);
        vaccinationDriveRepository.save(drive);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/vaccination-records")
    public ResponseEntity<List<VaccinationRecord>> getAllVaccinationRecords() {
        List<VaccinationRecord> records = vaccinationRecordService.findAll();
        return ResponseEntity.ok(records);
    }

    @PostMapping("/students/{studentId}/drives/{driveId}/vaccinate")
    public ResponseEntity<String> vaccinateStudent(@PathVariable String studentId, @PathVariable Long driveId) {
        Student student = studentService.findById(studentId);
        VaccinationDrive drive = vaccinationDriveService.findById(driveId);

        if (student == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with id " + studentId + " not found.");

        else if (drive == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Drive with id " + driveId + " not found.");

        else if (drive.getStatus() == DriveStatus.PENDING)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Drive not approved yet. Cannot vaccinate.");

        else if (drive.getStatus() == DriveStatus.REJECTED)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Drive is rejected. Cannot vaccinate.");

        else if(drive.getDriveDate().isAfter(LocalDate.now()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Drive is in future. Cannot vaccinate");

        else if(drive.getDriveDate().isBefore(LocalDate.now()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Drive date expired.");

        else if (drive.getAvailableDoses() <= 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No doses available.");

        else if (!drive.getApplicableClasses().contains(student.getStudentClass()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student is not eligible for this vaccination drive.");

        else if (vaccinationRecordService.alreadyVaccinated(student, drive))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student already vaccinated for this drive.");

        vaccinationRecordService.updateVaccinationRecord(student, drive);

        drive.setAvailableDoses(drive.getAvailableDoses() - 1);
        vaccinationDriveRepository.save(drive);

        return ResponseEntity.ok("Student successfully vaccinated.");
    }
}
