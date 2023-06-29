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
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private LocalDateTime paymentDate;
	// 기간 만료 전에 연장했을 경우 이용권 2개 이상일 테지만, 1:1 관계로 맺음
	// user와의 관계가 N:1 이므로, 이용권 당 1개의 Membership 객체를 가지면 됨
	@OneToOne
	private GymTicket gymTicket;

}