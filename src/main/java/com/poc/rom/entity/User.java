package com.poc.rom.entity;

import com.poc.rom.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    @Column(unique=true)
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserType userType;
}
