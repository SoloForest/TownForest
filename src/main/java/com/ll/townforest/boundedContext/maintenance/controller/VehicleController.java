package com.ll.townforest.boundedContext.maintenance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.townforest.boundedContext.maintenance.form.VehicleForm;
import com.ll.townforest.boundedContext.maintenance.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/maintenance")
@RequiredArgsConstructor
public class VehicleController {
	private final VehicleService vehicleService;

	@GetMapping("/vehicle")
	public String showPage() {
		return "/maintenance/vehicle";
	}

	@GetMapping("/insert")
	public String getinsert(Model model) {
		model.addAttribute("form", new VehicleForm());

		return "maintenance/write";
	}

	@PostMapping("/insert")
	public String insert(@Valid @ModelAttribute VehicleForm form) {
		System.out.println("이름:" + form.getName());
		System.out.println("차량번호:" + form.getVehicleNumber());

		vehicleService.insert(form.getName(), form.getVehicleNumber());
		return "maintenance/vehicle";
	}
}
