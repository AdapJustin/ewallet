package com.project.ewallet.Demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@CrossOrigin(origins = "http://localhost:3000")
public class DemoController {

	@GetMapping
	public String hello() {
		return "Hello World";
	}
}
