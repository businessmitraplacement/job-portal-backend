package com.decoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.decoder.dto.CareerPreferenceDTO;
import com.decoder.service.CareerPreferenceService;

@RestController
@RequestMapping("/api")
public class CareerPreferenceController {

	@Autowired
	private CareerPreferenceService careerPreferenceService;

	@GetMapping("/user_id")
	public ResponseEntity<CareerPreferenceDTO> getCareerPreference(@AuthenticationPrincipal UserDetails userDetails) {
	    String email = userDetails.getUsername();
	    CareerPreferenceDTO dto = careerPreferenceService.getPreferenceByUserEmail(email);
	    return ResponseEntity.ok(dto);
	}

	@PostMapping("/career-preferences")
	public ResponseEntity<CareerPreferenceDTO> saveCareerPreference(@AuthenticationPrincipal UserDetails userDetails,
	                                                                 @RequestBody CareerPreferenceDTO dto) {
	    String email = userDetails.getUsername();
	    CareerPreferenceDTO saved = careerPreferenceService.saveOrUpdatePreference(email, dto);
	    return ResponseEntity.ok(saved);
	}

}
