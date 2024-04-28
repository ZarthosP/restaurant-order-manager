package com.poc.rom.controller;

import com.poc.rom.entity.User;
import com.poc.rom.mapper.UserMapper;
import com.poc.rom.repository.UserRepository;
import com.poc.rom.resource.UserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping
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

    @PostMapping("/create")
    public User create(@RequestBody UserDto userDto) {
        User user = userMapper.map(userDto);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }


    @PostMapping("/checkUserExists")
    public boolean checkUserExists(@RequestBody String username) {
        Optional<User> byId = userRepository.findByUsername(username);
        if (byId.isPresent()) {
            return true;
        }
        return false;
    }

    @PostMapping("/checkLogin")
    public UserDto checkLogin(@RequestBody UserDto userDto) {
        Optional<User> byId = userRepository.findByUsername(userDto.getUsername());
        if (byId.isPresent()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(userDto.getPassword(), byId.get().getPassword())) {
                return userMapper.map(byId.get());
            };
        }
        return null;
    }

}
