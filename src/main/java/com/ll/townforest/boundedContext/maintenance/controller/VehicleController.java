package com.ll.townforest.boundedContext.maintenance.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.townforest.base.rq.Rq;
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
	private final Rq rq;

	@GetMapping("/vehicle/{id}")
	public String showPage(Model model, @PathVariable("id") Long id) {
		List<Vehicle> vehicle = vehicleService.findByUserId(id);
		model.addAttribute("form", vehicle);
		return "maintenance/vehicle";
	}

	@GetMapping("/add")
	public String getInsert(Model model) {
		//등록페이지에서 작성한 값이 들어갈 빈 객체
		model.addAttribute("form", new VehicleForm());
		return "maintenance/add";
	}

	@PostMapping("/add")
	public String insert(@Valid @ModelAttribute VehicleForm form) {
		form.setUser(rq.getAptAccount());
		vehicleService.create(form.getName(), form.getVehicleNumber(), form.getUser());
		return "redirect:/maintenance/vehicle/%d".formatted(form.getUser().getId());
	}
}
