package com.ll.townforest.boundedContext.maintenance.entity;

import java.time.LocalDateTime;

import com.ll.townforest.boundedContext.apt.entity.AptAccount;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@ToString
@Builder
public class Vehicle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private AptAccount user;
	@Column(length = 10)
	private String vehicleNumber;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private Integer type; // 방문차량(1), 세대주 차량(0) 구분
	private LocalDateTime date; // 방문일

}
