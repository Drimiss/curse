DELIMITER //

CREATE PROCEDURE `fill_dormitory2`()
BEGIN
    DECLARE finished INT DEFAULT 0;
    DECLARE student_id BIGINT;
    DECLARE student_gender ENUM('male', 'female');
    DECLARE room_id INT;
    DECLARE room_gender ENUM('male', 'female');
    DECLARE room_quantity INT;
    DECLARE current_occupancy INT;

    -- Курсор для студентов
    DECLARE student_cursor CURSOR FOR
SELECT id, gender FROM students ORDER BY id;

-- Курсор для комнат
DECLARE room_cursor CURSOR FOR
SELECT id, gender, quantity,
       (SELECT COUNT(*) FROM dormitory WHERE id_room = room.id) AS current_occupancy
FROM room ORDER BY id;

-- Обработчик окончания курсора
DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;

    -- Открываем курсор для студентов
OPEN student_cursor;

student_loop: LOOP
        FETCH student_cursor INTO student_id, student_gender;

        IF finished THEN
            LEAVE student_loop;
END IF;

        -- Открываем курсор для комнат
        SET finished = 0;
OPEN room_cursor;

room_loop: LOOP
            FETCH room_cursor INTO room_id, room_gender, room_quantity, current_occupancy;

            IF finished THEN
                LEAVE room_loop;
END IF;

            -- Проверка условий заселения
            IF (room_gender = student_gender OR room_gender IS NULL) AND current_occupancy < room_quantity THEN
                -- Обновляем гендер комнаты, если он NULL
                IF room_gender IS NULL THEN
UPDATE room SET gender = student_gender WHERE id = room_id;
END IF;

                -- Заселение студента
INSERT INTO dormitory (id_room, id_student) VALUES (room_id, student_id);
LEAVE room_loop;
END IF;
END LOOP;

CLOSE room_cursor;
END LOOP;

CLOSE student_cursor;
END
//

DELIMITER ;
