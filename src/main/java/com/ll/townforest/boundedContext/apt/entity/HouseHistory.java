package com.ll.townforest.boundedContext.apt.entity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@EntityListeners(AuditingEntityListener.class)
public class HouseHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private House house;
	@Column(nullable = false)
	private String houseAddress;
	@ManyToOne
	private AptAccount user;
	@Column(nullable = false)
	private String fullName;
	@Column(nullable = false)
	private String userAddress;
	private LocalDateTime selectedDate;
	/*
	 * 0: 신청
	 * 1: 승인
	 * 2: 반려
	 * */
	private int status;

	public String getStatusStr() {
		return switch (this.status) {
			case 1 -> "승인";
			case 2 -> "반려";
			default -> "신청";
		};
	}
}