package com.example.domitory.controllers;

import com.example.domitory.entity.Dormitory;
import com.example.domitory.repos.DormitoryRepository;
import com.example.domitory.services.DormitoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        // Удалите запись из базы данных по переданному id
        dormitoryRepository.deleteById(id);
        // Перенаправьте пользователя на страницу с обновленным списком общежитий
        return "redirect:/dormitories";
    }

    @PostMapping("/students/place")
    public String populateDormitory() {
        try {
            dormitoryRepository.callPopulateDormitory();
            return "redirect:/dormitories";
        } catch (Exception e) {
            return "error";
        }
    }
}
