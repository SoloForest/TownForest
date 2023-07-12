package com.ll.townforest.boundedContext.apt.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
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
public class House {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Integer dong;
	@Column(nullable = false)
	private Integer ho;
	@CreatedDate
	private LocalDateTime date;
	/*
	 * 0: 일반 가정집 (default)
	 * 1: 게스트 하우스
	 * */
	@Column(nullable = false, columnDefinition = "INT default '0'")
	private int type = 0;
	@ManyToOne
	private Apt apt;
}
