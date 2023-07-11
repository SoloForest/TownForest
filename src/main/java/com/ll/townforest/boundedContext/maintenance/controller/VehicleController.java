package com.ll.townforest.boundedContext.maintenance.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.base.rsData.RsData;
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

	@GetMapping("/vehicle")
	@PreAuthorize("isAuthenticated()")
	public String showPage() { //처음 접속할 페이지
		Long userId = rq.getAptAccount().getId();
		Long houseId = vehicleService.findByUserId(userId).getHouse().getId();
		return String.format("redirect:/maintenance/vehicle/%d", houseId);
	}

	@GetMapping("/vehicle/{id}")
	@PreAuthorize("isAuthenticated()")
	public String getPage(Model model, @PathVariable(name = "id") Long id) {//차량목록을 조회할 페이지
		if (vehicleService.accessTokenVehicle(rq.getAptAccount().getId(), id)) {
			List<Vehicle> vehicle = vehicleService.findByHouseId(id);
			model.addAttribute("form", vehicle);
			return "/maintenance/vehicle";
		} else {
			return rq.historyBack(RsData.of("F-1", "잘못된 접근"));
		}
	}

	@GetMapping("/add")
	@PreAuthorize("isAuthenticated()")
	public String getInsert(Model model) {
		//등록페이지에서 작성한 값이 들어갈 빈 객체
		model.addAttribute("form", new VehicleForm());
		return "/maintenance/add";
	}

	@PostMapping("/add")
	@PreAuthorize("isAuthenticated()")
	public String insert(@Valid VehicleForm form) {
		Long userId = rq.getAptAccount().getId();
		form.setAptHouse(vehicleService.findByUserId(userId));
		form.setUser(rq.getAptAccount());

		vehicleService.create(form);
		return String.format("redirect:/maintenance/vehicle/%d", form.getAptHouse().getHouse().getId());
	}

	@GetMapping("/delete/{id}")
	@PreAuthorize("isAuthenticated()")
	public String delete(@PathVariable("id") Long id) {
		Vehicle vehicle = vehicleService.findByVehicleId(id).get();
		Long UserId = rq.getAptAccount().getId();

		if (vehicle.getUser().getId().equals(UserId)) {
			vehicleService.deleteVehicleById(id);
			return rq.redirectWithMsg("/maintenance/vehicle", "삭제되었습니다.");
		} else {
			return rq.historyBack(RsData.of("F-1", "본인만 삭제할 수 있습니다."));
		}
	}
}