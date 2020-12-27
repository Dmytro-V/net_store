package com.netstore.home.service;

import com.netstore.home.model.Role;
import com.netstore.home.model.Status;
import com.netstore.home.model.User;
import com.netstore.home.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean addUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return false;
        }
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return true;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByEmail(name);
    }

    public void save(User updateUser) {
        userRepository.save(updateUser);
    }
}
