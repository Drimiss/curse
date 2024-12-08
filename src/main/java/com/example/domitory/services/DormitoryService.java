package com.example.domitory.services;

import com.example.domitory.entity.Dormitory;
import com.example.domitory.repos.DormitoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DormitoryService {

    @Autowired
    private DormitoryRepository DormitoryRepository;

    public List<Dormitory> listAll() {
        return DormitoryRepository.findAllWithDetails();
    }

    public Dormitory save(Dormitory dormitory) {
        return DormitoryRepository.save(dormitory);
    }

    public void delete(Long id) {
        DormitoryRepository.deleteById(id);
    }

    public Dormitory get(Long id) {
        Optional<Dormitory> optionalDormitory = DormitoryRepository.findById(id);
        return optionalDormitory.orElse(null); // Возвращаем null, если запись не найдена
    }
}