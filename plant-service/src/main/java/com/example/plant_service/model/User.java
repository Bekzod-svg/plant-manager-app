package com.example.plant_service.model;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
}
