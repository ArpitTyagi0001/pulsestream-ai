package com.streamanalytics.auth_service.service;

import com.streamanalytics.auth_service.dto.UserDto;
import com.streamanalytics.auth_service.model.Users;
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

    public UserDto userRegister(Users users) {
       users.setPassword(passwordEncoder.encode(users.getPassword()));
       Users user = usersRepo.save(users);
       return new UserDto("User Successfully Register" , user.getUsername());
    }

    public boolean userLogin(Users users) {
        Users user = usersRepo.findByUsername(users.getUsername());

        if(user == null){
            return false;
        }

        return true;
    }
}
