package com.example.domitory.repos;

import com.example.domitory.entity.Dormitory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface DormitoryRepository extends JpaRepository<Dormitory, Long> {

    @Query("SELECT d FROM Dormitory d JOIN FETCH d.room r JOIN FETCH d.student s")
    List<Dormitory> findAllWithDetails();

    @Query("""
                SELECT d FROM Dormitory d 
                JOIN FETCH d.room r 
                JOIN FETCH d.student s 
                WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
                   OR (LENGTH(:keyword) >= 2 AND CAST(r.nub_room AS string) LIKE LOWER(CONCAT('%', :keyword, '%')))
                   OR LOWER(s.faculty) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR CAST(s.course AS string) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR (LOWER(:keyword) IN ('male', 'female') AND LOWER(s.gender) = LOWER(:keyword))
            """)
    List<Dormitory> searchByKeyword(@Param("keyword") String keyword);


    @Modifying
    @Transactional
    @Query(value = "CALL fill_dormitory2();", nativeQuery = true)
    void callPopulateDormitory();

    @Query(value = """
            SELECT r.id, r.nub_room
            FROM room r
            LEFT JOIN dormitory d ON r.id = d.id_room
            LEFT JOIN students s ON s.id = d.id_student
            WHERE r.gender IS NULL
               OR (r.gender = (SELECT gender FROM students WHERE id = :studentId) AND s.id != :studentId
               AND (SELECT COUNT(*) FROM dormitory d2 WHERE d2.id_room = r.id) < r.quantity)
            GROUP BY r.id, r.nub_room
            """,

            nativeQuery = true)
    List<Map<String, Object>> findAvailableRooms(@Param("studentId") Long studentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Dormitory d WHERE d.student.id = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO dormitory (id_room, id_student) VALUES (:roomId, :studentId)", nativeQuery = true)
    void insertNewRoomAssignment(@Param("roomId") Long roomId, @Param("studentId") Long studentId);

    @Modifying
    @Transactional
    @Query("UPDATE Rooms r SET r.gender = :gender WHERE r.id = :roomId")
    void updateRoomGender(@Param("gender") String gender, @Param("roomId") Long roomId);

    @Modifying
    @Transactional
    @Query("UPDATE Rooms r SET r.gender = NULL WHERE r.id NOT IN (" +
            "SELECT d.room.id FROM Dormitory d GROUP BY d.room.id HAVING COUNT(d) > 0)")
    void resetRoomGenderForEmptyRooms();


}


