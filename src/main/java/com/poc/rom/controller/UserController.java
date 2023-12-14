package com.poc.rom.controller;

import com.poc.rom.entity.User;
import com.poc.rom.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/findUserById/{id}")
    public User findUserByLastName(@PathVariable Long id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.orElse(null);
    }

    @GetMapping("/checkUserPasswordTest")
    public boolean findUserByLastName() {
        Optional<User> byId = userRepository.findById(52L);
        if (byId.isPresent()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return passwordEncoder.matches("testPassword", byId.get().getPassword());
        }
        return false;
    }



    @GetMapping("/createTestUser")
    public User createUser() {
        User user = new User();
        user.setFirstName("testFirstName");
        user.setLastName("testLast");
        user.setPassword("testPassword");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        Optional<User> byId = userRepository.findById(user.getId());
        if (byId.isPresent()) {
            return byId.get();
        } else {
            return null;
        }
    }


}
