package com.ll.townforest.boundedContext.apt.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.House;
import com.ll.townforest.boundedContext.apt.entity.HouseHistory;
import com.ll.townforest.boundedContext.apt.service.HouseHistoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HouseController {
	private final HouseHistoryService houseHistoryService;
	private final Rq rq;

	@GetMapping("/guest/booking")
	@PreAuthorize("isAuthenticated()")
	public String showBooking(Model model) {
		if (!rq.getAptAccount().isStatus() || !rq.getAptAccount().getApt().getId().equals(1L)) {
			return rq.historyBack("해당 아파트에 대한 권한이 없습니다.");
		}
		return "guest/house_booking";
	}

	@PostMapping("/guest/houses")
	@ResponseBody
	public List<House> canBookingHouseList(
		@RequestParam("selectedDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime selectedDate
	) {
		return houseHistoryService.canBookingHousesWithDate(selectedDate);
	}

	@PostMapping("/guest/booking/my")
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	public Slice<HouseHistory> myGuesthouseBooking(@RequestParam int page) {
		return houseHistoryService.historiesByUserWithPage(page, rq.getAptAccount().getId());
	}

	@PostMapping("/guest/booking")
	@PreAuthorize("isAuthenticated()")
	public String booking(@RequestParam LocalDateTime selectedDate, @RequestParam Long selectedRoom) {
		RsData<AptAccount> canBookingUser = houseHistoryService.canBooking(rq.getAptAccount(), selectedDate);
		if (canBookingUser.isFail()) {
			return rq.historyBack(canBookingUser.getMsg());
		}

		RsData<House> canBookingHouse = houseHistoryService.canBooking(selectedRoom, selectedDate);
		if (canBookingHouse.isFail()) {
			return rq.historyBack(canBookingHouse.getMsg());
		}

		RsData<HouseHistory> houseHistory =
			houseHistoryService.booking(selectedDate, canBookingUser.getData(), canBookingHouse.getData());
		if (houseHistory.isFail()) {
			return rq.historyBack(houseHistory.getMsg());
		}
		return rq.redirectWithMsg("/guest/booking", houseHistory.getMsg());
	}
}
