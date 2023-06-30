package com.ll.townforest.boundedContext.gym.entity;

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
import jakarta.persistence.OneToOne;
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
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private LocalDateTime paymentDate;
	private int price;
	/*
	결제 방법
	0 : 계좌이체
	1 : 카드
	*/
	private int paymentMethod;
	/*
	이용권 종류
	*/
	@OneToOne
	private GymTicket gymTicket;
	/*
	상태
	0: 결제
	1: 만료
	2: 연장	등
	추가 예정	// TODO : 상태 고려
	*/
	private int status;
}