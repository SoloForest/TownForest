package com.ll.townforest.boundedContext.library.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ll.townforest.boundedContext.apt.entity.Apt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
public class Library {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Apt apart;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer maxPeople;
	
	private String seatingChart;
}
