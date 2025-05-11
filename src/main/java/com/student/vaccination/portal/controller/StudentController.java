package com.student.vaccination.portal.controller;

import com.student.vaccination.portal.dto.StudentDTO;
import com.student.vaccination.portal.entity.Student;
import com.student.vaccination.portal.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody StudentDTO dto) {
        StudentDTO studentDTO = studentService.addStudent(dto);

        if(studentDTO==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student Id already exists.");

        return ResponseEntity.ok("Student added.");

    }

    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable String studentId, @RequestBody StudentDTO dto) {
        Student student = studentService.findById(studentId);
        if (student == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID " + studentId + " not found.");

        studentService.updateStudent(studentId,dto);
        return ResponseEntity.ok("");
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudent(@PathVariable String studentId) {
        Student student = studentService.findById(studentId);
        if (student == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID " + studentId + " not found.");

        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable String studentId) {
        Student student = studentService.findById(studentId);
        if (student == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID " + studentId + " not found.");

        studentService.deleteStudent(student);
        return ResponseEntity.ok("");
    }

    @PostMapping("/bulk-upload")
    public String bulkUploadStudents(@RequestParam("file") MultipartFile file) {
        studentService.bulkUploadStudents(file);
        return "Bulk upload successful!";
    }

    @GetMapping("/search")
    public List<StudentDTO> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String studentClass,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) Boolean vaccinated
    ) {
        return studentService.searchStudents(name, studentClass, studentId, vaccinated);
    }
}
