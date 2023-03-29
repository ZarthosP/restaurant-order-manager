package com.poc.rom.controller;

import com.poc.rom.entity.User;
import com.poc.rom.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String test() {
        return "Hello world";
    }

    @GetMapping("/findUser")
    public List<User> findUser() {
        return userRepository.findByLastName("testLastName");
    }

    @GetMapping("/createUser")
    public User createUser() {
        User user = new User();
        user.setFirstName("test");
        user.setLastName("test");
        userRepository.save(user);
        Optional<User> byId = userRepository.findById(user.getId());
        if (byId.isPresent()) {
            return byId.get();
        } else {
            return null;
        }
    }


}
