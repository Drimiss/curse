package com.example.domitory.controllers;

import com.example.domitory.entity.Dormitory;
import com.example.domitory.repos.DormitoryRepository;
import com.example.domitory.services.DormitoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DormitoryController {

    private final DormitoryService service;

    public DormitoryController(DormitoryService service) {
        this.service = service;
    }

    @GetMapping("/dormitories")
    public String viewDormitories(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model
    ) {
        List<Dormitory> dormitories;

        if (keyword != null && !keyword.isEmpty()) {
            dormitories = service.searchDormitoriesByStudentName(keyword);
        } else {
            dormitories = service.listAll(); // Загрузка с JOIN FETCH
        }

        model.addAttribute("dormitories", dormitories);
        model.addAttribute("keyword", keyword);

        return "dormitories";
    }
    @Autowired
    private DormitoryRepository dormitoryRepository;

    @GetMapping("/delete/{id}")
    public String removeStudentFromDormitory(@PathVariable Long id) {
        // Найдите запись по id
        Optional<Dormitory> dormitoryOptional = dormitoryRepository.findById(id);
        if (dormitoryOptional.isPresent()) {
            Dormitory dormitory = dormitoryOptional.get();
            // Установите id_student в NULL
            dormitory.setStudent(null);
            dormitoryRepository.save(dormitory);
        }
        // Перенаправьте пользователя на страницу с обновленным списком общежитий
        return "redirect:/dormitories";
    }

}
