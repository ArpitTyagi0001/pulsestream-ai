package com.streamanalytics.auth_service.service;

import com.streamanalytics.auth_service.dto.UsersDto;
import com.streamanalytics.auth_service.entity.Users;
import com.streamanalytics.auth_service.repo.UsersRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UsersService(UsersRepo usersRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.usersRepo = usersRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public UsersDto register(Users users) {
        if (usersRepo.findByUsername(users.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        users.setPassword(passwordEncoder.encode(users.getPassword()));

        Users savedUser = usersRepo.save(users);

        return new UsersDto("User registered successfully" , savedUser.getUsername());
    }

    public UsersDto login(Users users) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));

        if (authentication.isAuthenticated()) {
            return new UsersDto("User login successfully", users.getUsername());
        } else {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
