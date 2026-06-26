package com.streamanalytics.auth_service.service;

import com.streamanalytics.auth_service.dto.UserDto;
import com.streamanalytics.auth_service.model.Users;
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
    private final JWTService jwtService;

    public UsersService(UsersRepo usersRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.usersRepo = usersRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserDto userRegister(Users user) {
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       Users user1 = usersRepo.save(user);
       return new UserDto("User Successfully Register" , user.getUsername());
    }

    public String userLogin(Users user) {
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        if(authentication.isAuthenticated()){
             return jwtService.generateToken(user.getUsername());
        }

        return "fail";
    }
}
