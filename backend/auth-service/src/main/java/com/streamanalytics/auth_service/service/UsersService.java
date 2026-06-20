package com.streamanalytics.auth_service.service;

import com.streamanalytics.auth_service.dto.UsersDto;
import com.streamanalytics.auth_service.entity.Users;
import com.streamanalytics.auth_service.repo.UsersRepo;
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
}
