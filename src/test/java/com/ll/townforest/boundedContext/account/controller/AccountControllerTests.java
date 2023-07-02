package com.ll.townforest.boundedContext.account.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.boundedContext.account.service.AccountService;

import jakarta.servlet.http.HttpSession;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AccountControllerTests {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private AccountService accountService;

	@Test
	@DisplayName("로그인 폼")
	void t001() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/account/login"))
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(AccountController.class))
			.andExpect(handler().methodName("showLogin"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("account/login"));
	}

	@Test
	@DisplayName("로그인 시 메인 페이지 호출")
	void t002() throws Exception {
		ResultActions resultActions = mvc
			.perform(post("/account/login")
				.with(csrf())
				.param("username", "admin")
				.param("password", "admin1!")
			)
			.andDo(print());

		MvcResult mvcResult = resultActions.andReturn();
		HttpSession session = mvcResult.getRequest()
			.getSession(false);// 원래 getSession 을 하면, 만약에 없을 경우에 만들어서라도 준다., false 는 없으면 만들지 말라는 뜻
		SecurityContext securityContext = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
		User user = (User)securityContext.getAuthentication().getPrincipal();

		assertThat(user.getUsername()).isEqualTo("admin");

		resultActions
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"));
	}

	@Test
	@DisplayName("회원 가입 폼")
	void t003() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/account/join"))
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(AccountController.class))
			.andExpect(handler().methodName("showJoin"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("account/join"));
	}

	@Test
	@DisplayName("회원 가입 테스트")
	void t004() throws Exception {
		ResultActions resultActions = mvc
			.perform(post("/account/join")
				.with(csrf())
				.param("username", "testUser")
				.param("password", "test1234!")
				.param("fullName", "test")
				.param("email", "test@eamil.com")
				.param("phoneNumber", "01000001234")
			)
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(AccountController.class))
			.andExpect(handler().methodName("join"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("/account/login**"));

		assertThat(accountService.findByUsername("testUser").isPresent()).isEqualTo(true);
	}
}
