package com.ll.townforest.boundedContext.library.controller;

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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class LibraryControllerTests {
	@Autowired
	private MockMvc mvc;

	@Test
	@DisplayName("독서실 자리 예약 폼")
	void t001() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/library/booking"))
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(LibraryController.class))
			.andExpect(handler().methodName("showBooking"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/account/login"));
	}

	@Test
	@DisplayName("독서실 자리 예약 폼")
	@WithUserDetails("yujin11006")
	void t002() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/library/booking"))
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(LibraryController.class))
			.andExpect(handler().methodName("showBooking"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("library/booking"));
	}

	@Test
	@DisplayName("독서실 자리 예약")
	@WithUserDetails("bbosong")
	void t003() throws Exception {
		ResultActions resultActions = mvc
			.perform(post("/library/booking")
				.with(csrf())
				.param("selectedSeat", "1")
			)
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(LibraryController.class))
			.andExpect(handler().methodName("booking"))
			.andExpect(status().is2xxSuccessful());

		assertThat(resultActions.andReturn().getResponse().getContentAsString())
			.isEqualTo("해당 아파트 독서실 이용권한이 없습니다.");
	}

	@Test
	@DisplayName("독서실 자리 예약")
	@WithUserDetails("yujin11006")
	void t004() throws Exception {
		ResultActions resultActions = mvc
			.perform(post("/library/booking")
				.with(csrf())
				.param("selectedSeat", "1")
			)
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(LibraryController.class))
			.andExpect(handler().methodName("booking"))
			.andExpect(status().is2xxSuccessful());

		assertThat(resultActions.andReturn().getResponse().getContentAsString())
			.isEqualTo("001번 자리를 예약했습니다.");
	}

	@Test
	@DisplayName("독서실 자리 예약 취소")
	@WithUserDetails("yujin11006")
	void t005() throws Exception {
		ResultActions resultActions = mvc
			.perform(post("/library/cancel")
				.with(csrf()))
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(LibraryController.class))
			.andExpect(handler().methodName("cancel"))
			.andExpect(status().is2xxSuccessful());

		assertThat(resultActions.andReturn().getResponse().getContentAsString())
			.isEqualTo("이용중인 독서실 자리가 없습니다.");
	}

	@Test
	@DisplayName("독서실 자리 예약 취소")
	@WithUserDetails("yujin11006")
	void t006() throws Exception {
		mvc.perform(post("/library/booking").with(csrf()).param("selectedSeat", "1")).andDo(print());

		ResultActions resultActions = mvc
			.perform(post("/library/cancel")
				.with(csrf()))
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(LibraryController.class))
			.andExpect(handler().methodName("cancel"))
			.andExpect(status().is2xxSuccessful());

		assertThat(resultActions.andReturn().getResponse().getContentAsString())
			.isEqualTo("001번 자리 이용을 취소합니다.");
	}
}