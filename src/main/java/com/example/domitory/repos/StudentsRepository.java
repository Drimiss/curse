package com.example.domitory.repos;

import com.example.domitory.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentsRepository extends JpaRepository<Students, Long> {

    @Query("SELECT d FROM Students d")
    List<Students> findAll();

    @Query("SELECT s FROM Students s LEFT JOIN Dormitory d ON s.id = d.student.id WHERE d.student.id IS NULL")
    List<Students> findAllStudentsNotInDormitory();

}

