package com.example.domitory.services;

import com.example.domitory.entity.Roles;
import com.example.domitory.entity.Students;
import com.example.domitory.repos.RoleRepository;
import com.example.domitory.repos.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentsService {

    @Autowired
    private StudentsRepository repository;

    public List<Students> listAllSt() {
        return repository.findAll();
    }

    public List<Students> getStudentsNotInDormitory() {
        return repository.findAllStudentsNotInDormitory();
    }

    public void saveStudent(String fullName, String birthDate, String gender, String faculty, int course) {
        repository.saveStudent(fullName, birthDate, gender, faculty, course);
    }

    public void deleteStudent(Long id) {
        repository.deleteStudentById(id);

    }

    public Students getStudentById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + id));
    }

    public void saveStudent(Students student) {
        repository.save(student);
    }
}
