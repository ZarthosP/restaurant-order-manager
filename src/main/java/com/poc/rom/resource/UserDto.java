package com.poc.rom.resource;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonRootName("user")
public class UserDto implements Serializable {

    private Long id;

    private String email;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String userType;
}
