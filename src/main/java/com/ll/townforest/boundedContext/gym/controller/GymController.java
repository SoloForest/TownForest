package com.ll.townforest.boundedContext.gym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gym")
public class GymController {

	@GetMapping("")
	public String gymMain() {
		return "gym/gym";
	}

	@GetMapping("/locker")
	public String locker() {
		return "gym/locker";
	}

	@GetMapping("/refund")
	public String refund() {
		return "gym/refund";
	}

	@GetMapping("/register")
	public String register() {
		return "gym/register";
	}

}
