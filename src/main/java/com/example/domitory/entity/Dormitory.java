package com.example.domitory.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dormitory")
public class Dormitory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_room", referencedColumnName = "id")
    private Rooms room; // Proper join to Rooms table

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "id_student", referencedColumnName = "id")
    private Students student; // Proper join to Students table

    public Dormitory() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rooms getRoom() {
        return room;
    }

    public void setRoom(Rooms room) {
        this.room = room;
    }

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }
}
