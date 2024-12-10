package com.example.domitory.repos;

import com.example.domitory.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentsRepository extends JpaRepository<Students, Long> {

    @Query("SELECT d FROM Students d")
    List<Students> findAll();

    @Query("SELECT s FROM Students s WHERE s.id NOT IN (SELECT d.student.id FROM Dormitory d)")
    List<Students> findAllStudentsNotInDormitory();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO students (full_name, b_date, gender, faculty, course) VALUES (:fullName, :birthDate, :gender, :faculty, :course)", nativeQuery = true)
    void saveStudent(String fullName, String birthDate, String gender, String faculty, Integer course);

}

