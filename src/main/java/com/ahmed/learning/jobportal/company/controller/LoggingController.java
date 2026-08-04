package com.ahmed.learning.jobportal.company.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logging")
@Slf4j
public class LoggingController {

	@GetMapping("/public")
	public ResponseEntity<String> testLogging() {
		log.info("this is the logger from logger controller!");
		return ResponseEntity.ok("Logging is working successfully");
	}
}
