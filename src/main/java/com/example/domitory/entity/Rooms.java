package com.example.domitory.entity;

import com.example.domitory.entity.Students;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Rooms {
    @Id
    private Long id; // Задается вручную, без генерации.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_student", referencedColumnName = "id")
    @JsonIgnore // Игнорирует это поле при сериализации
    private Students student;

    public Rooms() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }
}
