package com.ll.townforest.boundedContext.apt.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class AptAccountHouse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String relationship;
	@OneToOne(cascade = {CascadeType.REMOVE}, orphanRemoval = true)
	private AptAccount user;
	@ManyToOne
	private House house;

	/*
	 * 탈퇴한 회원을 나타내는 변수
	 * true: 탈퇴한 회원
	 * false: 유효한 회원
	 * default : false (유효한 회원)
	 */
	@Builder.Default
	private boolean status = false;
}
