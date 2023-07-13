package com.ll.townforest.boundedContext.apt.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ll.townforest.boundedContext.account.entity.Account;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@EntityListeners(AuditingEntityListener.class)
public class AptAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Account account;
	@ManyToOne
	private Apt apt;

	/*
	 * 회원 권한 코드를 나타내는 변수
	 * 0: 일반 입주민
	 * 1: 아파트 관리자
	 * 2: 독서실 관리자
	 * 3: 헬스장 관리자
	 * default : 0 (일반 입주민)
	 */
	@Builder.Default
	private int authority = 0;

	/*
	 * 회원 가입 상태를 나타내는 변수
	 * true: 회원 가입 승인
	 * false: 회원 가입 대기
	 * default : false (회원 가입 대기)
	 */
	@Builder.Default
	private boolean status = false;
}
