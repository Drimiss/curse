package com.example.domitory.repos;

import com.example.domitory.entity.Dormitory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DormitoryRepository extends JpaRepository<Dormitory, Long> {

    @Query("SELECT d FROM Dormitory d JOIN FETCH d.room r JOIN FETCH d.student s")
    List<Dormitory> findAllWithDetails();


}
