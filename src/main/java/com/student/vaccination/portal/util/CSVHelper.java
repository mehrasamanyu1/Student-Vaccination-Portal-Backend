package com.student.vaccination.portal.util;

import com.student.vaccination.portal.dto.StudentDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    public static List<StudentDTO> parseStudentsCsv(MultipartFile file) {
        List<StudentDTO> students = new ArrayList<>();
        try {
            CSVParser csvParser = new CSVParser(
                    new InputStreamReader(file.getInputStream()),
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            for (CSVRecord record : csvParser) {
                StudentDTO student = new StudentDTO();
                student.setStudentId(record.get("studentId"));
                student.setName(record.get("name"));
                student.setStudentClass(record.get("studentClass"));
                student.setGender(record.get("gender"));
                student.setDateOfBirth(record.get("dateOfBirth"));
                students.add(student);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing CSV file: " + e.getMessage());
        }
        return students;
    }
}
