package com.example.domitory.controllers;

import com.example.domitory.entity.Dormitory;
import com.example.domitory.services.DormitoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DormitoryController {

    @Autowired
    private DormitoryService service;

    @GetMapping("/dormitories")
    public String viewDormitories(Model model) {
        List<Dormitory> dormitories = service.listAll(); // Загрузка с JOIN FETCH
        model.addAttribute("dormitories", dormitories);
        return "dormitories";
    }
}