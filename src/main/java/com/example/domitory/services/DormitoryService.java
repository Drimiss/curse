package com.example.domitory.services;

import com.example.domitory.entity.Dormitory;
import com.example.domitory.entity.Students;
import com.example.domitory.repos.DormitoryRepository;
import groovy.lang.GString;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DormitoryService {

    private final DormitoryRepository repository;

    public DormitoryService(DormitoryRepository repository) {
        this.repository = repository;
    }

    public List<Dormitory> listAll() {
        return repository.findAllWithDetails();
    }

    public List<Dormitory> searchDormitoriesByStudentName(String keyword) {
        return repository.searchByKeyword(keyword);
    }
    @Transactional
    public void callPopulateDormitory() {
        repository.callPopulateDormitory();
    }

    @Transactional
    public void moveStudentToNewRoom(Long studentId, Long newRoomId, String gender) {
        repository.deleteByStudentId(studentId);
        repository.insertNewRoomAssignment(newRoomId, studentId);
        repository.updateRoomGender(gender, newRoomId);
        repository.need();
    }

    public List<Map<String, Object>> getAvailableRooms(Long studentId) {
        return repository.findAvailableRooms(studentId);
    }

}
