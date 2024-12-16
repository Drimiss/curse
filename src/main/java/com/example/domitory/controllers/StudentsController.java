package com.example.domitory.controllers;

import com.example.domitory.entity.Students;
import com.example.domitory.services.StudentsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        model.addAttribute("students", students);
        return "students";
    }


    @PostMapping("/students/upload-json")
    public Object uploadJsonFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".json")) {
            return "error";
        }

        try {
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
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        service.deleteStudent(id);
        return "redirect:/students"; // Перенаправление на список студентов после удаления
    }

    @GetMapping("/students/edit_student/{id}")
    public String showEditStudentForm(@PathVariable("id") Long id, Model model) {
        Students student = service.getStudentById(id); // Получаем студента по ID
        model.addAttribute("student", student);
        return "/edit_student"; // Страница с формой редактирования
    }

    @PostMapping("/students/edit_student/{id}")
    public String updateStudent(@PathVariable("id") Long id, HttpServletRequest request) {
        // Получаем текущего студента
        Students existingStudent = service.getStudentById(id);

        // Извлекаем параметры из запроса
        String fullName = request.getParameter("fullName");
        String course = request.getParameter("course");
        String faculty = request.getParameter("faculty");

        // Обновляем только изменяемые поля
        existingStudent.setFullName(fullName);
        existingStudent.setCourse(Integer.parseInt(course));
        existingStudent.setFaculty(faculty);

        // Сохраняем изменения
        service.saveStudent(existingStudent);

        return "redirect:/students";
    }

}
