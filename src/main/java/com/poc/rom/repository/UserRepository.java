package com.poc.rom.repository;

import com.poc.rom.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByLastName(String lastName);
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);
}
