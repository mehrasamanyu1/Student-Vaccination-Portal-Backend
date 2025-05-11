package com.student.vaccination.portal.repository;

import com.student.vaccination.portal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentId(String studentId);

    Optional<Student> findByStudentId(String studentId);

    @Query("SELECT s FROM Student s " +
            "LEFT JOIN s.vaccinations v " +
            "WHERE (:name IS NULL OR s.name LIKE %:name%) " +
            "AND (:studentClass IS NULL OR s.studentClass = :studentClass) " +
            "AND (:studentId IS NULL OR s.studentId = :studentId) " +
            "AND (:vaccinated IS NULL OR " +
            "(:vaccinated = TRUE AND v.status = 'VACCINATED') OR " +
            "(:vaccinated = FALSE AND v.status = 'PENDING'))")
    List<Student> searchStudents(
            @Param("name") String name,
            @Param("studentClass") String studentClass,
            @Param("studentId") String studentId,
            @Param("vaccinated") Boolean vaccinated
    );


    List<Student> findByStudentClassIn(List<String> applicableClasses);

    List<Student> findByStudentClass(String className);
}
