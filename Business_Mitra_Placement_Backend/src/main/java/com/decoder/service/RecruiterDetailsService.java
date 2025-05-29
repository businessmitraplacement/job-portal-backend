package com.decoder.service;

import com.decoder.model.Recruiter;
import com.decoder.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RecruiterDetailsService implements UserDetailsService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	 System.out.println("✅ RecruiterDetailsService triggered for: " + email);
    	
        Recruiter recruiter = recruiterRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Recruiter not found with email: " + email));

        return new User(
                recruiter.getEmail(),
                recruiter.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_RECRUITER"))

        );
        
    }
}
