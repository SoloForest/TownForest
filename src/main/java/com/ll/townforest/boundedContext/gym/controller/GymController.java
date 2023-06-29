package com.ll.townforest.boundedContext.gym.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.townforest.boundedContext.gym.entity.GymTicket;
import com.ll.townforest.boundedContext.gym.service.GymService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gym")
public class GymController {

	private final GymService gymService;

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
	public String register(Model model) {
		// 아파트 여러개라면 현재 로그인한 사용자가 속한 Gym ID를 넘긴다.
		List<GymTicket> gymTicketList = gymService.getGymTickets(1L);

		model.addAttribute("gymTicketList", gymTicketList);

		return "gym/register";
	}

}
