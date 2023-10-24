package com.example.chatIvzilol.model.entity;


import jakarta.persistence.*;

import java.util.Set;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String uniqueCode;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> userRooms;

    @ManyToOne
    private User userCreator;

    public ChatRoom() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Set<User> getUserRooms() {
        return userRooms;
    }

    public void setUserRooms(Set<User> userRooms) {
        this.userRooms = userRooms;
    }

    public User getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(User userCreator) {
        this.userCreator = userCreator;
    }
}
