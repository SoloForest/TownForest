package com.ll.townforest.boundedContext.gym.entity;

import java.util.ArrayList;
import java.util.List;

import com.ll.townforest.boundedContext.apt.entity.Apt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@ToString
public class Gym {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	@ToString.Exclude
	private Apt apt;

	private String name;

	@OneToMany
	@Builder.Default
	@ToString.Exclude
	private List<GymTicket> gymTicketList = new ArrayList<>();
}