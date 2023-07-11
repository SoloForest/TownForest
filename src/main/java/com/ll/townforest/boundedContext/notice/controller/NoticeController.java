package com.ll.townforest.boundedContext.notice.controller;

import org.springframework.data.domain.Page;
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

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.Apt;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.notice.entity.Notice;
import com.ll.townforest.boundedContext.notice.service.NoticeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
	private final Rq rq;
	private final NoticeService noticeService;

	@GetMapping("")
	@PreAuthorize("isAuthenticated()")
	public String showMain(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

		AptAccount user = rq.getAptAccount();

		if (user == null || !user.isStatus())
			return rq.redirectWithMsg("/", "승인된 아파트 회원만 접속 가능합니다.");

		model.addAttribute("user", user);

		Apt apt = user.getApt();

		Page<Notice> notices = noticeService.getNotices(page, apt);

		model.addAttribute("paging", notices);

		return "notice/notice";
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public String showNotice(@PathVariable Long id, Model model) {
		AptAccount user = rq.getAptAccount();

		if (user == null || !user.isStatus())
			return rq.redirectWithMsg("/", "승인된 아파트 회원만 접속 가능합니다.");

		Notice notice = noticeService.getNoticeById(id);

		if (notice == null) {
			return rq.redirectWithMsg("/", "삭제된 공지글입니다.");
		}

		model.addAttribute("notice", notice);

		return "notice/detail";
	}

	@Data
	public static class NoticeForm {
		@NotBlank(message = "제목을 입력해주세요.")
		private String title;
		@NotBlank(message = "내용을 입력해주세요.")
		private String content;
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String showCreateNotice(NoticeForm noticeForm) {
		if (!rq.isAdmin())
			return rq.historyBack("관리자만 작성할 수 있습니다.");

		return "notice/notice_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String createNotice(@Valid NoticeForm noticeForm, BindingResult bindingResult) {
		if (!rq.isAdmin())
			return rq.historyBack("관리자만 작성할 수 있습니다.");

		if (bindingResult.hasErrors()) {
			return "notice/notice_form";
		}

		AptAccount writer = rq.getAptAccount();

		RsData rs = noticeService.create(writer, noticeForm.getContent(), noticeForm.getTitle());

		return rq.redirectWithMsg("/notice", rs.getMsg());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String showModifyNotice(@PathVariable("id") Long id, NoticeForm noticeForm) {
		if (!rq.isAdmin())
			return rq.historyBack("관리자만 수정할 수 있습니다.");

		Notice notice = noticeService.getNoticeById(id);

		if (notice == null) {
			return rq.historyBack("존재하지 않는 공지글입니다.");
		}

		if (!notice.getWriter().equals(rq.getAptAccount())) {
			return rq.historyBack("작성자만 수정할 수 있습니다.");
		}

		noticeForm.setContent(notice.getContent());
		noticeForm.setTitle(notice.getTitle());

		return "notice/notice_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String modifyNotice(@Valid NoticeForm noticeForm, @PathVariable Long id) {
		if (!rq.isAdmin())
			return rq.historyBack("관리자만 수정할 수 있습니다.");

		Notice notice = noticeService.getNoticeById(id);

		if (notice == null) {
			return rq.historyBack("존재하지 않는 공지글입니다.");
		}

		if (!notice.getWriter().equals(rq.getAptAccount())) {
			return rq.historyBack("작성자만 수정할 수 있습니다.");
		}

		RsData result = noticeService.modify(notice, noticeForm.getTitle(), noticeForm.getContent());

		return rq.redirectWithMsg("/notice", result.getMsg());
	}

	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/{id}")
	public String deleteNotice(@PathVariable Long id) {
		if (!rq.isAdmin())
			return rq.historyBack("관리자만 삭제 가능합니다");

		Notice notice = noticeService.getNoticeById(id);

		if (notice == null) {
			return rq.historyBack("존재하지 않는 공지글입니다.");
		}

		if (!notice.getWriter().equals(rq.getAptAccount())) {
			return rq.historyBack("작성자만 삭제할 수 있습니다.");
		}

		RsData result = noticeService.delete(notice);

		return rq.redirectWithMsg("/notice", result.getMsg());
	}

}
