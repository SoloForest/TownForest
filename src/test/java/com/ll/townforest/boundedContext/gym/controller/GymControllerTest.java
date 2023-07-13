package com.ll.townforest.boundedContext.gym.controller;

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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.boundedContext.gym.service.GymService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class GymControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private GymService gymService;

	@Test
	@WithUserDetails("yujin11006")
	@DisplayName("성공 - 이용권 있는 회원의 일시정지 페이지 접속")
	void t001() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/gym/pause"))
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(GymController.class))
			.andExpect(handler().methodName("showPause"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("gym/pause"));
	}

	@Test
	@WithUserDetails("test2")
	@DisplayName("승인되지 않은 회원의 일시정지 페이지 접속시 실패")
	void t002() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/gym/pause"))
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(GymController.class))
			.andExpect(handler().methodName("showPause"))
			.andExpect(status().is4xxClientError());
	}

	@Test
	@WithUserDetails("yujin11006")
	@DisplayName("이용권 일시정지 시도 - 성공")
	void t003() throws Exception {
		ResultActions resultActions = mvc
			.perform(post("/gym/pause")
				.with(csrf())
				.param("membershipId", "1")
			)
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(GymController.class))
			.andExpect(handler().methodName("pause"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("/gym**"));
	}

	@Test
	@WithUserDetails("yujin11006")
	@DisplayName("이용권 일시정지 후 일시정지 풀기 - 성공")
	void t004() throws Exception {
		ResultActions resultActions = mvc
			.perform(post("/gym/pause")
				.with(csrf())
				.param("membershipId", "1")
			)
			.andDo(print());

		ResultActions resultActions2 = mvc
			.perform(post("/gym/unpause")
				.with(csrf())
				.param("membershipId", "1")
			)
			.andDo(print());

		resultActions2
			.andExpect(handler().handlerType(GymController.class))
			.andExpect(handler().methodName("unPause"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("/gym**"));
	}

}