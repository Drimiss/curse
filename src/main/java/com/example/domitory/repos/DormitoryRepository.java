package com.example.domitory.repos;

import com.example.domitory.entity.Dormitory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DormitoryRepository extends JpaRepository<Dormitory, Long> {

    @Query("SELECT d FROM Dormitory d JOIN FETCH d.room r JOIN FETCH d.student s")
    List<Dormitory> findAllWithDetails();

    @Query("""
    SELECT d FROM Dormitory d 
    JOIN FETCH d.room r 
    JOIN FETCH d.student s 
    WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
       OR (LENGTH(:keyword) >= 2 AND CAST(r.nubRoom AS string) LIKE LOWER(CONCAT('%', :keyword, '%')))
       OR LOWER(s.faculty) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR CAST(s.course AS string) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR (LOWER(:keyword) IN ('male', 'female') AND LOWER(s.gender) = LOWER(:keyword))
""")
    List<Dormitory> searchByKeyword(@Param("keyword") String keyword);


    @Modifying
    @Transactional
    @Query("UPDATE Dormitory d SET d.student = NULL WHERE d.id = :id")
    void removeStudentFromDormitory(@Param("id") Long id);
}
