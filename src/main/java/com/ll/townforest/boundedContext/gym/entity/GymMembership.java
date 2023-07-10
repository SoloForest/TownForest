package com.ll.townforest.boundedContext.gym.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.data.annotation.CreatedDate;
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
public class GymMembership {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private AptAccount user;
	@ManyToOne
	private Gym gym;
	@ManyToOne
	private Apt apt;
	private String address;
	private String contact;
	private LocalDate startDate;
	private LocalDate endDate;
	@CreatedDate
	private LocalDateTime paymentDate;
	/*
	상태
	0: 이용 대기중(시작 전)
	1: 이용중
	2: 일시정지
	3: 연장
	4: 재시작
	*/
	private int status;

	// 일시정지 한 날
	private LocalDate pauseDate;
	// 일시정지 후 남은 날짜
	private Integer remainingDay;
	// 재시작일
	private LocalDate restartDate;
	
  // 이용권 총 며칠 남았는지
	// 남은 일자 구하기
	public long getRemainingDays() {
		switch (this.status) {
			case 0, 3 -> {
				return ChronoUnit.DAYS.between(this.startDate, this.endDate);
			}
			default -> {
				return ChronoUnit.DAYS.between(LocalDate.now(), this.endDate);
			}
		}
	}
}