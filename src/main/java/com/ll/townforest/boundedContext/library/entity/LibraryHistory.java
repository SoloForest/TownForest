package com.ll.townforest.boundedContext.library.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ll.townforest.boundedContext.apt.entity.Apt;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;

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
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
public class LibraryHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Apt apart;

	@ManyToOne
	private Library library;

	@ManyToOne
	private AptAccount user;

	@Column(nullable = false)
	private String addressToString;

	@ManyToOne
	private Seat seat;

	@CreatedDate
	private LocalDateTime date;

	/*
	 * 0: 예약
	 * 1: 취소(반납)
	 * 2: 자동취소
	 * 3: 관리자취소
	 * */
	@Column(nullable = false)
	private Integer statusType;

	private String fullName;

	public String getStatusTypeToString() {
		return switch (statusType) {
			case 0 -> "예약";
			case 1 -> "취소";
			case 2 -> "자동취소";
			case 3 -> "관리자취소";
			default -> "";
		};
	}
}
