package com.example.domitory.controllers;

import com.example.domitory.entity.Students;
import com.example.domitory.services.StudentsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping("/students/upload-json")
    public Object uploadJsonFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".json")) {
            return "error";
        }

        try {
            // Чтение файла и конвертация в список объектов
            ObjectMapper objectMapper = new ObjectMapper();
            List<Students> students = objectMapper.readValue(file.getInputStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Students.class));

            // Сохранение данных в базу
            for (Students student : students) {
                service.saveStudent(
                        student.getFullName(),
                        student.getBirthDate(),
                        student.getGender(),
                        student.getFaculty(),
                        student.getCourse()
                );
            }

                     return "redirect:/students";

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

    }
}