	package com.decoder.service;
	
	import com.decoder.model.Recruiter;
	import com.decoder.repository.RecruiterRepository;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.security.crypto.password.PasswordEncoder;
	import org.springframework.stereotype.Service;
	
	import java.util.Optional;
	
	@Service
	public class RecruiterService {
	
	    @Autowired
	    private RecruiterRepository repo;
	
	    @Autowired
	    private PasswordEncoder passwordEncoder;
	
	    public Recruiter register(Recruiter recruiter) {
	        recruiter.setPassword(passwordEncoder.encode(recruiter.getPassword()));
	        return repo.save(recruiter);
	    }
	    
	    public String encodePassword(String rawPassword) {
	        return passwordEncoder.encode(rawPassword);
	    }
	
	    public boolean isEmailOrNameExists(String email, String name) {
	        return repo.existsByEmail(email) || repo.existsByName(name);
	    }
	    
	    public Optional<Recruiter> findByEmail(String email) {
	        return repo.findByEmail(email);
	    }
	
	}
	
