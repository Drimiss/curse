DELIMITER //

CREATE PROCEDURE fill_dormitory()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE student_id BIGINT;
    DECLARE student_gender ENUM('male', 'female');
    DECLARE room_id INT;
    DECLARE room_gender ENUM('male', 'female');
    DECLARE room_count INT;
    DECLARE current_occupancy INT;
    DECLARE room_found INT DEFAULT 0;

    DECLARE student_cursor CURSOR FOR
SELECT id, gender FROM students WHERE id NOT IN (SELECT id_student FROM dormitory);

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

OPEN student_cursor;

read_loop: LOOP
        FETCH student_cursor INTO student_id, student_gender;
        IF done THEN
            LEAVE read_loop;
END IF;

        SET room_id = NULL;
        SET room_found = 0;

        -- Цикл для поиска подходящей комнаты
        room_search: LOOP
SELECT r.id, r.gender, r.quantity,
       (SELECT COUNT(*) FROM dormitory d WHERE d.id_room = r.id) AS current_occupancy
INTO room_id, room_gender, room_count, current_occupancy
FROM room r
WHERE (r.gender IS NULL OR r.gender = student_gender)  -- Комната пустая или соответствует полу
  AND current_occupancy < r.quantity
ORDER BY r.id ASC
    LIMIT 1;

IF room_id IS NOT NULL THEN
                -- Если пол комнаты пустой, обновляем его
                IF room_gender IS NULL THEN
UPDATE room r
SET r.gender = student_gender
WHERE r.id = room_id;
END IF;

                -- Заселяем студента в найденную комнату
INSERT INTO dormitory (id_room, id_student) VALUES (room_id, student_id);
SET room_found = 1;
                LEAVE read_loop;
END IF;

            -- Если комната не найдена, выходим из поиска и продолжаем с новым студентом
            IF room_found = 0 THEN
                LEAVE room_search;
END IF;
END LOOP room_search;
END LOOP;

CLOSE student_cursor;
END;
//

DELIMITER ;
