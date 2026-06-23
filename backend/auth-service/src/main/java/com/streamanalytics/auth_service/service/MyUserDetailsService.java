package com.streamanalytics.auth_service.service;

import com.streamanalytics.auth_service.entity.UserPrinciple;
import com.streamanalytics.auth_service.entity.Users;
import com.streamanalytics.auth_service.repo.UsersRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {


    private final UsersRepo usersRepo;

    public MyUserDetailsService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepo.findByUsername(username);

        if(users == null) {
            System.out.println("User not Found By username :" + username);
            throw new UsernameNotFoundException("User Not Found username");
        }
        return new UserPrinciple(users);
    }
}
