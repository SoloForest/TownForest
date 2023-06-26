package com.ll.townforest.boundedContext.gym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GymController {

	@GetMapping("/gym")
	public String root() {
		return "gym/Gym";
	}

}
