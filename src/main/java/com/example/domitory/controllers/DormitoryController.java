package com.example.domitory.controllers;

import com.example.domitory.entity.Dormitory;
import com.example.domitory.repos.DormitoryRepository;
import com.example.domitory.services.DormitoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DormitoryController {

    private final DormitoryService service;

    public DormitoryController(DormitoryService service) {
        this.service = service;
    }


    @Autowired
    private DormitoryRepository dormitoryRepository;

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



    @GetMapping("/dormitories/move/{id}")
    public String removeStudentFromDormitory(@PathVariable Long id) {
        dormitoryRepository.deleteById(id);
        dormitoryRepository.resetRoomGenderForEmptyRooms();
        return "dormitories";
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
    @RequestMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/dormitory/{studentId}/availableRooms")
    public ResponseEntity<List<Map<String, Object>>> getAvailableRooms(@PathVariable Long studentId) {
        List<Map<String, Object>> availableRooms = service.getAvailableRooms(studentId);
        return ResponseEntity.ok(availableRooms);
    }

    @PutMapping("/dormitory/moveStudent")
    public ResponseEntity<Map<String, String>> moveStudent(@RequestBody Map<String, Object> requestData) {
        Long studentId = Long.valueOf(requestData.get("studentId").toString());
        Long roomId = Long.valueOf(requestData.get("roomId").toString());
        String gender = (String) requestData.get("gender");  // Получаем gender

        // Передаем все параметры в сервис
        service.moveStudentToNewRoom(studentId, roomId,gender);

        // Возвращаем правильный JSON объект
        Map<String, String> response = new HashMap<>();
        response.put("message", "Студент успешно перемещен!");

        return ResponseEntity.ok(response);
    }

}

