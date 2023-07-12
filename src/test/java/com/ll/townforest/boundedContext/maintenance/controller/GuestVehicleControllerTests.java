package com.ll.townforest.boundedContext.maintenance.controller;

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

import com.ll.townforest.boundedContext.maintenance.repository.VehicleRepository;
import com.ll.townforest.boundedContext.maintenance.service.VehicleService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class GuestVehicleControllerTests {
	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private MockMvc mvc;

	@Test
	@DisplayName("방문차량 등록 폼")
	void t001() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/guest/register"))
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(GuestVehicleController.class))
			.andExpect(handler().methodName("showPage"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/account/login"));
	}

	@Test
	@DisplayName("방문차량 등록 폼")
	@WithUserDetails("yujin11006")
	void t002() throws Exception {
		ResultActions resultActions = mvc
			.perform(get("/guest/register"))
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(GuestVehicleController.class))
			.andExpect(handler().methodName("showPage"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("/guest/car_register"));
	}

	@Test
	@DisplayName("방문차량 등록")
	@WithUserDetails("yujin11006")
	void t003() throws Exception {
		ResultActions resultActions = mvc
			.perform(post("/guest/register")
				.with(csrf())
				.param("selectedDate", "2023-07-07T12:00")
				.param("name", "이송이")
				.param("vehicleNumber", "123가 4567")
			)
			.andDo(print());

		resultActions.andExpect(handler().handlerType(GuestVehicleController.class))
			.andExpect(handler().methodName("guestVehicleRegister"))
			.andExpect(status().is4xxClientError());
	}

	@Test
	@DisplayName("방문차량 등록")
	@WithUserDetails("yujin11006")
	void t004() throws Exception {
		ResultActions resultActions = mvc
			.perform(post("/guest/register")
				.with(csrf())
				.param("selectedDate", "2023-07-07T12:00")
				.param("name", "방유진")
				.param("vehicleNumber", "123가 4567")
			)
			.andDo(print());

		resultActions
			.andExpect(handler().handlerType(GuestVehicleController.class))
			.andExpect(handler().methodName("guestVehicleRegister"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("register?msg=**"));
	}
}