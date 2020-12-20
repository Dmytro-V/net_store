package com.netstore.home.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @Column(name = "city")
    private String city;
    @Column(name = "address")
    private String address;
    @Column(name = "message")
    private String message;
    @Column(name = "avatar")
    private String avatar;
}
