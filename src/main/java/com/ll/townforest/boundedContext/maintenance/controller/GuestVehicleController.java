package com.ll.townforest.boundedContext.maintenance.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.maintenance.entity.Vehicle;
import com.ll.townforest.boundedContext.maintenance.form.GuestVehicleDTO;
import com.ll.townforest.boundedContext.maintenance.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/guest")
@RequiredArgsConstructor
public class GuestVehicleController {
	private final VehicleService vehicleService;
	private final Rq rq;

	@GetMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public String showPage() {
		return "/guest/car_register";
	}

	@PostMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public String guestVehicleRegister(@Valid @ModelAttribute GuestVehicleDTO guestVehicleDTO) {
		RsData<AptAccount> canRegisterGuestUser =
			vehicleService.canRegisterGuest(rq.getAptAccount(), guestVehicleDTO.getName());
		if (canRegisterGuestUser.isFail()) {
			return rq.historyBack(RsData.of("F-1", canRegisterGuestUser.getMsg()));
		}

		RsData<Vehicle> createdGuest = vehicleService.createGuest(canRegisterGuestUser.getData(), guestVehicleDTO);

		return rq.redirectWithMsg("register", createdGuest);
	}
}
