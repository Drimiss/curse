package com.example.domitory.controllers;

import com.example.domitory.entity.Students;
import com.example.domitory.services.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StudentsController {

    @Autowired
    private StudentsService service;

    @GetMapping("/students")
    public String viewStudents(Model model) {
        List<Students> students = service.listAllSt(); // Все студенты
        List<Students> studentsNotInDormitory = service.getStudentsNotInDormitory(); // Студенты, которых нет в общежитии
        model.addAttribute("students", students);
        model.addAttribute("studentsNotInDormitory", studentsNotInDormitory); // Передаем в модель
        return "students";
    }

}

