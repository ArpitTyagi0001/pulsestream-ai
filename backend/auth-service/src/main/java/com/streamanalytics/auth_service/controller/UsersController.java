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
    public ResponseEntity<UserDto> userRegister(@RequestBody Users user){
        UserDto user1 = usersService.userRegister(user);
        return ResponseEntity.accepted().body(user1);
    }

    @PostMapping("/login")
    public String verify(@RequestBody Users user){
        String result = usersService.userLogin(user);
         return result;
    }
}
