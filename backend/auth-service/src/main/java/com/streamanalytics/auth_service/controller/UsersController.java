package com.streamanalytics.auth_service.controller;

import com.streamanalytics.auth_service.dto.UserDto;
import com.streamanalytics.auth_service.model.Users;
import com.streamanalytics.auth_service.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth-service")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody Users users){
        UserDto user = usersService.userRegister(users);
        return ResponseEntity.accepted().body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody Users users){
         boolean result = usersService.userLogin(users);
         return ResponseEntity.ok(result);
    }
}
