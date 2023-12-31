package com.ll.townforest.base.rq;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.account.entity.Account;
import com.ll.townforest.boundedContext.account.service.AccountService;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.service.AptAccountService;
import com.ll.townforest.standard.util.Ut;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
@RequestScope
public class Rq {
	private final AccountService accountService;
	private final AptAccountService aptAccountService;
	private final HttpServletRequest req;
	private final HttpServletResponse resp;
	private final HttpSession session;
	private final User user;
	private Account account = null; // 레이지 로딩, 처음부터 넣지 않고, 요청이 들어올 때 넣는다.
	private AptAccount aptAccount = null;

	public Rq(AccountService accountService, AptAccountService aptAccountService, HttpServletRequest req,
		HttpServletResponse resp, HttpSession session) {
		this.accountService = accountService;
		this.aptAccountService = aptAccountService;
		this.req = req;
		this.resp = resp;
		this.session = session;

		// 현재 로그인한 회원의 인증정보를 가져옴
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication.getPrincipal() instanceof User) {
			this.user = (User)authentication.getPrincipal();
		} else {
			this.user = null;
		}
	}

	// 로그인 되어 있는지 체크
	public boolean isLogin() {
		return user != null;
	}

	// 로그아웃 되어 있는지 체크
	public boolean isLogout() {
		return !isLogin();
	}

	// 로그인 된 회원의 객체
	public Account getAccount() {
		if (isLogout())
			return null;

		// 데이터가 없는지 체크
		if (account == null) {
			account = accountService.findByUsername(user.getUsername()).orElseThrow();
		}

		return account;
	}

	public AptAccount getAptAccount() {
		if (isLogout())
			return null;

		getAccount();
		// 데이터가 없는지 체크
		if (aptAccount == null) {
			aptAccount = aptAccountService.findByAccount(account).orElse(null);
		}

		return aptAccount;
	}

	// 관리자인지 체크
	public boolean isAdmin() {
		getAptAccount();
		return aptAccount.getAuthority() != 0;
	}

	// 아파트 관리자인지 체크
	public boolean isAptAdmin() {
		getAptAccount();
		return aptAccount.getAuthority() == 1;
	}

	// 독서실 관리자인지 체크
	public boolean isLibraryAdmin() {
		getAptAccount();
		return aptAccount.getAuthority() == 2;
	}

	// 헬스장 관리자인지 체크
	public boolean isGymAdmin() {
		getAptAccount();
		return aptAccount.getAuthority() == 3;
	}

	public String historyBack(String msg) {
		String referer = req.getHeader("referer");
		String key = "historyBackErrorMsg___" + referer;
		req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
		req.setAttribute("historyBackErrorMsg", msg);
		// 200 이 아니라 400 으로 응답코드가 지정되도록
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return "common/js";
	}

	// 뒤로가기 + 메세지
	public String historyBack(RsData rsData) {
		return historyBack(rsData.getMsg());
	}

	// 302 + 메세지
	public String redirectWithMsg(String url, RsData rsData) {
		return redirectWithMsg(url, rsData.getMsg());
	}

	// 302 + 메세지
	public String redirectWithMsg(String url, String msg) {
		return "redirect:" + urlWithMsg(url, msg);
	}

	private String urlWithMsg(String url, String msg) {
		// 기존 URL에 혹시 msg 파라미터가 있다면 그것을 지우고 새로 넣는다.
		return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
	}

	private String msgWithTtl(String msg) {
		return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
	}
}