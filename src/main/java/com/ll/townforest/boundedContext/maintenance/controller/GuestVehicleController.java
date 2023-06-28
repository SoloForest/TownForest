package com.ll.townforest.boundedContext.maintenance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/guest")
@RequiredArgsConstructor
public class GuestVehicleController {
	@GetMapping("/register")
	public String showPage() {
		return "/guest/car_register";
	}
}
