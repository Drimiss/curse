package com.example.domitory.entity;

import com.example.domitory.entity.Students;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "room")
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Auto-generated primary key

    @Column(name = "nub_room")
    private Integer nubRoom;

    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "gender", nullable = false)
    private String gender;

    public Rooms() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNubRoom() {
        return nubRoom;
    }

    public void setNubRoom(Integer nubRoom) {
        this.nubRoom = nubRoom;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
