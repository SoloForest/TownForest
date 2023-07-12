package com.ll.townforest.boundedContext.home.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.service.AptAccountHouseService;
import com.ll.townforest.boundedContext.apt.service.AptAccountService;
import com.ll.townforest.boundedContext.gym.entity.GymHistory;
import com.ll.townforest.boundedContext.gym.entity.GymMembership;
import com.ll.townforest.boundedContext.gym.entity.GymTicket;
import com.ll.townforest.boundedContext.gym.service.GymService;
import com.ll.townforest.boundedContext.home.dto.SearchDTO;
import com.ll.townforest.boundedContext.home.dto.TicketForm;
import com.ll.townforest.boundedContext.library.entity.LibraryHistory;
import com.ll.townforest.boundedContext.library.entity.Seat;
import com.ll.townforest.boundedContext.library.service.LibraryService;
import com.ll.townforest.boundedContext.maintenance.form.GuestVehicleHistory;
import com.ll.townforest.boundedContext.maintenance.service.VehicleService;
import com.ll.townforest.boundedContext.notice.entity.Notice;
import com.ll.townforest.boundedContext.notice.service.NoticeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	private final Rq rq;
	private final LibraryService libraryService;
	private final AptAccountService aptAccountService;
	private final VehicleService vehicleService;
	private final AptAccountHouseService aptAccountHouseService;
	private final GymService gymService;

	private final NoticeService noticeService;

	@GetMapping("")
	@PreAuthorize("isAuthenticated()")
	public String showAdmin(Model model) {
		if (!rq.isAdmin()) {
			return rq.historyBack("관리자 전용 페이지입니다.");
		}

		// 여기까지 온거면 관리자
		AptAccount user = rq.getAptAccount();
		List<Notice> noticeList = noticeService.getNoticeTop5LatestByUser_AptId(user);

		model.addAttribute("noticeList", noticeList);

		return "admin/main";
	}

	// 독서실 관리자 페이지 시작
	@GetMapping("/library/histories")
	@PreAuthorize("isAuthenticated()")
	public String showLibraryHistories() {
		if (!rq.isAdmin()) {
			return rq.historyBack("관리자 전용 페이지입니다.");
		}

		if (rq.isGymAdmin()) {
			return rq.historyBack("독서실 관리 권한이 없습니다.");
		}

		return "admin/library/histories";
	}

	@PostMapping("/library/histories")
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	public Page<LibraryHistory> histories(@RequestParam int page,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate) {
		return libraryService.findAllHistoriesByDate(searchDate, PageRequest.of(page, 25));
	}

	@PostMapping("/library/histories/all")
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	public Page<LibraryHistory> histories(@RequestParam int page) {

		return libraryService.findAllHistories(PageRequest.of(page, 25));
	}

	@PostMapping("/library/cancel")
	@ResponseBody
	@PreAuthorize("isAuthenticated()")
	public String libraryBookingCancel(@RequestParam Long libraryHistoryId) {
		RsData<AptAccount> canCancelUser = libraryService.canAdminCancel(rq.getAptAccount());
		if (canCancelUser.isFail()) {
			return canCancelUser.getMsg();
		}

		RsData<Seat> canCancelSeat = libraryService.canAdminCancel(libraryHistoryId);
		if (canCancelSeat.isFail()) {
			return canCancelSeat.getMsg();
		}

		AptAccount targetUser = libraryService.getTargetUserForAdminCancel(libraryHistoryId);

		return libraryService.adminCancel(targetUser, canCancelSeat.getData(), libraryHistoryId).getMsg();
	}
	// 독서실 관리자 페이지 끝

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/gym")
	public String adminMain(Model model) {
		AptAccount user = rq.getAptAccount();
		if (!rq.isGymAdmin())
			rq.historyBack("헬스장 관리자만 접속 가능합니다.");

		List<GymMembership> currentUsers = gymService.getMemberList(user);
		model.addAttribute("currentUsers", currentUsers);

		// 이용권 정지시킨 회원 필터링
		List<GymMembership> pauseUsers = currentUsers
			.stream()
			.filter(a -> a.getStatus() == 3)
			.collect(Collectors.toList());

		model.addAttribute("pauseUsers", pauseUsers);

		return "admin/gym/gym";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/gym/member")
	public String showMember(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
		SearchDTO searchDTO) {

		AptAccount user = rq.getAptAccount();
		if (!rq.isGymAdmin())
			rq.historyBack("헬스장 관리자만 접속 가능합니다.");

		Page<GymMembership> paging = gymService.getMemberPage(page, user, searchDTO);
		model.addAttribute("paging", paging);

		return "admin/gym/members";
	}

	@GetMapping("/management")
	@PreAuthorize("isAuthenticated()")
	public String showAptAccountManagement(Model model, @RequestParam(defaultValue = "1") int sortCode) {
		int authority = rq.getAptAccount().getAuthority();

		if (authority == 0) {
			return "redirect:/";
		}

		if (authority == 2 || authority == 3) {
			return "redirect:/admin";
		}

		List<AptAccountHouse> aptAccountHouseList = aptAccountHouseService.findAptAccountHouse(sortCode);

		model.addAttribute("aptAccountHouseList", aptAccountHouseList);
		model.addAttribute("sortCode", sortCode);
		return "admin/aptAccount/management";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/gym/history")
	public String showAllGymHistory(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
		SearchDTO searchDTO) {
		if (!rq.isGymAdmin())
			return rq.historyBack("헬스장 관리자만 접속 가능합니다");

		Page<GymHistory> gymHistories = gymService.getAllHistories(page, searchDTO);

		model.addAttribute("paging", gymHistories);

		return "admin/gym/history";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/gym/ticket")
	public String showAllGymTicket(Model model) {
		if (!rq.isGymAdmin())
			return rq.historyBack("헬스장 관리자만 접속 가능합니다");

		// TODO : 아파트가 우선 1개이기에 하드코딩, 여러개 될 시 관리자가 관리하는 gym 넣어주기
		List<GymTicket> gymTicketList = gymService.getGymTickets(1L);
		model.addAttribute("gymTicketList", gymTicketList);

		return "admin/gym/ticket/ticket";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/gym/ticket/create")
	public String showCreateGymTicket(TicketForm ticketForm) {
		if (!rq.isGymAdmin())
			return rq.historyBack("헬스장 관리자만 접속 가능합니다");

		return "admin/gym/ticket/ticket_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/gym/ticket/create")
	public String createGymTicket(@Valid TicketForm ticketForm, BindingResult bindingResult) {
		if (!rq.isGymAdmin())
			return rq.historyBack("헬스장 관리자만 접속 가능합니다");

		if (bindingResult.hasErrors()) {
			return "admin/gym/ticket/ticket_form";
		}

		AptAccount user = rq.getAptAccount();

		RsData rsTicket = gymService.createTicket(user, ticketForm);

		return rq.redirectWithMsg("/admin/gym/ticket", rsTicket.getMsg());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/gym/ticket/modify/{id}")
	public String showModifyGymTicket(@PathVariable("id") Long id, TicketForm ticketForm) {
		if (!rq.isGymAdmin())
			return rq.historyBack("헬스장 관리자만 접속 가능합니다");

		GymTicket gymTicket = gymService.getTicket(id);

		if (gymTicket == null) {
			return rq.historyBack("존재하지 않는 이용권입니다.");
		}

		ticketForm.setName(gymTicket.getName());
		ticketForm.setDays(gymTicket.getDays());
		ticketForm.setPrice(gymTicket.getPrice());
		ticketForm.setContent(gymTicket.getContent());

		return "admin/gym/ticket/ticket_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/gym/ticket/modify/{id}")
	public String modifyGymTicket(@Valid TicketForm ticketForm, @PathVariable Long id) {
		if (!rq.isGymAdmin())
			return rq.historyBack("헬스장 관리자만 접속 가능합니다");

		GymTicket gymTicket = gymService.getTicket(id);

		if (gymTicket == null) {
			return rq.historyBack("존재하지 않는 이용권입니다.");
		}

		RsData result = gymService.modifyTicket(gymTicket, ticketForm);

		return rq.redirectWithMsg("/admin/gym/ticket", result.getMsg());
	}

	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/gym/ticket/{id}")
	public String deleteGymTicket(@PathVariable Long id) {
		if (!rq.isGymAdmin())
			return rq.historyBack("헬스장 관리자만 삭제 가능합니다");

		GymTicket gymTicket = gymService.getTicket(id);

		if (gymTicket == null) {
			return rq.historyBack("존재하지 않는 이용권입니다.");
		}

		RsData result = gymService.deleteTicket(gymTicket);

		return rq.redirectWithMsg("/admin/gym/ticket", result.getMsg());
	}

	// 방문차량 조회 페이지 시작
	@GetMapping("/guest/vehicle")
	@PreAuthorize("isAuthenticated()")
	public String showGuestVehicles(
		Model model,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "1") int tab
	) {
		if (rq.getAptAccount().getAuthority() != 1) {
			return rq.historyBack("차량 관리 권한이 없습니다.");
		}

		Page<GuestVehicleHistory> vehicles = vehicleService.findGuestByTab(page, tab);

		model.addAttribute("vehicles", vehicles);
		model.addAttribute("tab", tab);

		return "admin/guest/vehicle";
	}
	//방문자량 조회 페이지 끝

	@PostMapping("/approve/{id}")
	@PreAuthorize("isAuthenticated()")
	public String approve(@PathVariable Long id, @RequestParam int sortCode) {
		AptAccountHouse aptAccountHouse = aptAccountHouseService.findById(id).orElse(null);

		RsData<AptAccountHouse> aptAccountHouseRsData = aptAccountHouseService.canApprove(rq.getAptAccount(),
			aptAccountHouse);

		if (aptAccountHouseRsData.isFail()) {
			return rq.historyBack(aptAccountHouseRsData);
		}

		return rq.redirectWithMsg("/admin/management?sortCode=%d".formatted(sortCode), aptAccountHouseRsData);
	}

	@PostMapping("/delete/{id}")
	@PreAuthorize("isAuthenticated()")
	public String delete(@PathVariable Long id, @RequestParam int sortCode) {
		AptAccountHouse aptAccountHouse = aptAccountHouseService.findById(id).orElse(null);

		RsData<AptAccountHouse> aptAccountHouseRsData = aptAccountHouseService.canDelete(rq.getAptAccount(),
			aptAccountHouse);

		if (aptAccountHouseRsData.isFail()) {
			return rq.historyBack(aptAccountHouseRsData);
		}

		return rq.redirectWithMsg("/admin/management?sortCode=%d".formatted(sortCode), aptAccountHouseRsData);
	}
}
