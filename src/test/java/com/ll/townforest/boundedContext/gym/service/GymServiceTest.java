package com.ll.townforest.boundedContext.gym.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.gym.entity.GymMembership;
import com.ll.townforest.boundedContext.gym.repository.GymHistoryRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class GymServiceTest {

	@Autowired
	private GymService gymService;

	@Autowired
	private GymHistoryRepository gymHistoryRepository;
	@Autowired
	private Rq rq;

	@Test
	@DisplayName("이용권 연장(추가결제) 시 이용정보 변경 테스트")
	@WithUserDetails("yujin11006")
	void t002() throws Exception {

		AptAccount aptAccount = rq.getAptAccount();

		GymMembership gymMembership = gymService.getMembershipByUser(aptAccount);

		gymService.update(aptAccount, LocalDate.now(), LocalDate.now(), 1, "카드");

		// 기존 이용권 연장 -> 상태 변경 (1 -> 3)
		assertThat(gymMembership.getStatus()).isEqualTo(3);
	}

}
