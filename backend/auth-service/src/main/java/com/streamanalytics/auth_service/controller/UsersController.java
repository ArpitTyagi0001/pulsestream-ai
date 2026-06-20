package com.streamanalytics.auth_service.controller;

import com.streamanalytics.auth_service.dto.UsersDto;
import com.streamanalytics.auth_service.entity.Users;
import com.streamanalytics.auth_service.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security")
public class UsersController {

    private UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/user-register")
    public ResponseEntity<UsersDto> UsersRegister(@RequestBody Users users){
        UsersDto response = usersService.register(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
