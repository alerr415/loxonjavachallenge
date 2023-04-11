package com.example.javachallenge;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.javachallenge.auth.AuthService;

@RestController
public class BaseController {

	// @Autowired
	// private AuthService authService;
    
    // @RequestMapping("/")
	// public ResponseEntity<String> index() {
	// 	return new ResponseEntity<String>(authService.getKeyPEM(), HttpStatus.OK);
	// }

}
