package com.example.domitory.services;

import com.example.domitory.entity.Students;
import com.example.domitory.repos.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


}

