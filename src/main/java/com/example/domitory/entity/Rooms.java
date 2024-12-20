package com.example.domitory.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "room")
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nub_room")
    private Integer nub_room;

    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "gender", nullable = true)
    private String gender;

    public Rooms() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNubRoom() {
        return nub_room;
    }

    public void setNubRoom(Integer nubRoom) {
        this.nub_room = nubRoom;
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
