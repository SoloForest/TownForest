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
	public String showPage() {
		Long userId = rq.getAptAccount().getId();
		String aptCheck = vehicleService.getUserid(userId);
		if (aptCheck == null) {
			return rq.redirectWithMsg("/", "동호수 등록후 이용해주십시오");
		}
		return aptCheck;
	}

	@GetMapping("/vehicle/{id}")
	@PreAuthorize("isAuthenticated()")
	public String getPage(Model model, @PathVariable(name = "id") Long id) {
		List<Vehicle> vehicleList = vehicleService.getVehicleList(rq.getAptAccount().getId(), id);
		if (vehicleList != null) {
			model.addAttribute("form", vehicleList);
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
		form.setUser(rq.getAptAccount());
		form.setAptHouse(vehicleService.findByUserId(userId));

		VehicleService.VehicleResult result = vehicleService.getAllId(rq.getAptAccount().getId(), form);

		switch (result) {
			case SUCCESS:
				return String.format("redirect:/maintenance/vehicle/%d", form.getAptHouse().getHouse().getId());
			case NAME_INVALID:
				return rq.historyBack(RsData.of("F-1", "존재하지 않는 세대원입니다."));
			case VEHICLE_DUPLICATION:
				return rq.historyBack(RsData.of("F-2", "이미 존재하는 차량번호 입니다."));
			default:
				return rq.historyBack(RsData.of("F-3", "알 수 없는 오류가 발생했습니다."));
		}
	}

	@GetMapping("/delete/{id}")
	@PreAuthorize("isAuthenticated()")
	public String delete(@PathVariable("id") Long id) {
		Long userId = rq.getAptAccount().getId();

		VehicleService.VehicleResult result = vehicleService.delete(id, userId);
		if (result == VehicleService.VehicleResult.SUCCESS) {
			return rq.redirectWithMsg("/maintenance/vehicle", "삭제되었습니다.");
		} else if (result == VehicleService.VehicleResult.FAILED) {
			return rq.historyBack(RsData.of("F-1", "등록자만 삭제할 수 있습니다."));
		}
		return rq.historyBack(RsData.of("F-2", "알 수 없는 오류가 발생했습니다."));
	}
}