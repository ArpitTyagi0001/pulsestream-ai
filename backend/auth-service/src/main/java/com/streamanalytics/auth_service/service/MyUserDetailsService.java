package com.streamanalytics.auth_service.service;

import com.streamanalytics.auth_service.model.UserPrinciple;
import com.streamanalytics.auth_service.model.Users;
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
        Users user = usersRepo.findByUsername(username);

        if(user == null){
            System.out.println("User Not found");
            throw new UsernameNotFoundException("User Not Found");
        }

        return new UserPrinciple(user);
    }
}
