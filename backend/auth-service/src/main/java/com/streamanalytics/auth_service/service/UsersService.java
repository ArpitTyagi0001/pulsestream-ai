package com.streamanalytics.auth_service.service;

import com.streamanalytics.auth_service.dto.UsersDto;
import com.streamanalytics.auth_service.entity.Users;
import com.streamanalytics.auth_service.repo.UsersRepo;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;


    public UsersService(UsersRepo usersRepo, PasswordEncoder passwordEncoder) {
        this.usersRepo = usersRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UsersDto register(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));

        Users savedUser = usersRepo.save(users);

        return new UsersDto("User registered successfully" , savedUser.getUsername());
    }

    public UsersDto login(Users users) {

        Users dbUser = usersRepo.findByUsername(users.getUsername());

        if (dbUser == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(users.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return new UsersDto("Login successful", dbUser.getUsername());
    }
}
