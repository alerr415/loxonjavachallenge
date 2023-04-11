package com.example.javachallenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.javachallenge.registration.RegistrationService;

@RestController
public class BaseController {

	// @Autowired
	// private AuthService authService;
    
    // @RequestMapping("/")
	// public ResponseEntity<String> index() {
	// 	return new ResponseEntity<String>(authService.getKeyPEM(), HttpStatus.OK);
	// }

	@Autowired
	private RegistrationService registrationService;

	@RequestMapping("/")
	public ResponseEntity<String> index() throws Exception {
		return new ResponseEntity<String>(registrationService.registerTeam(), HttpStatus.OK);
	}

}
