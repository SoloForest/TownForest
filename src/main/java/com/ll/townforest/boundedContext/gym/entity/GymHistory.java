package com.ll.townforest.boundedContext.gym.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ll.townforest.boundedContext.apt.entity.Apt;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
public class GymHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private AptAccount user;
	@ManyToOne
	private Gym gym;
	@ManyToOne
	private Apt apt;
	private LocalDate startDate;
	private LocalDate endDate;

	private LocalDateTime paymentDate;
	private int price;
	/*
	결제 방법
	*/
	private String paymentMethod;
	/*
	이용권 종류
	관리자가 이용권 삭제할 경우 삭제 안되는 것을 방지하기 위해 String으로
	예시 : 30일권
	*/
	private String name;
	/*
	상태
	0: 결제
	1: 연장
	2: 일시정지
	3: 재시작
	4. 만료
	*/
	private int status;

	// 일시정지 한 날
	private LocalDate pauseDate;
	// 일시정지 후 남은 날짜
	private Integer remainingDay;
	// 재시작일
	private LocalDate restartDate;
	private String address;
	private String contact;
	private String userName;
}