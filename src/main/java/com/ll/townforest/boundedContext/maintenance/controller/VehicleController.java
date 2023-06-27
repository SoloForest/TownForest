package com.ll.townforest.boundedContext.maintenance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/maintenance")
@RequiredArgsConstructor
public class VehicleController {
	@GetMapping("/vehicle")
	public String showPage() {
		return "/maintenance/vehicle";
	}
}
