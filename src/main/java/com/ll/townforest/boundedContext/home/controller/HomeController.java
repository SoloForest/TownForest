package com.ll.townforest.boundedContext.home.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.notice.entity.Notice;
import com.ll.townforest.boundedContext.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final Rq rq;

	private final NoticeService noticeService;

	@GetMapping("/")
	public String root(Model model) {
		if (rq.isLogout())
			return "redirect:/account/login";

		if (rq.getAptAccount() == null)
			return "redirect:/aptAccount/register";

		if (rq.isAdmin())
			return "redirect:/admin";

		if (!rq.getAptAccount().isStatus())
			return "aptAccount/awaiting_approval";

		// 여기까지 온거면 일반 사용자
		AptAccount user = rq.getAptAccount();
		List<Notice> noticeList = noticeService.getNoticeTop5LatestByUser_AptId(user);

		model.addAttribute("noticeList", noticeList);

		return "home/main";
	}
}
