package com.ll.townforest.boundedContext.maintenance.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.townforest.boundedContext.maintenance.entity.Vehicle;
import com.ll.townforest.boundedContext.maintenance.form.VehicleForm;
import com.ll.townforest.boundedContext.maintenance.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/maintenance")
@RequiredArgsConstructor
public class VehicleController {
	private final VehicleService vehicleService;

	@GetMapping("/vehicle/{id}")
	public String showPage(Model model, @PathVariable("id") Long id) {
		List<Vehicle> vehicle = vehicleService.findByUserId(id);
		model.addAttribute("form", vehicle);
		return "maintenance/vehicle";
	}

	@GetMapping("/add")
	public String getinsert() {
		return "maintenance/add";
	}

	@PostMapping("/add")
	public String insert(@Valid @ModelAttribute VehicleForm form) {
		vehicleService.create(form.getName(), form.getVehicleNumber());
		return "maintenance/vehicle";
	}
}
