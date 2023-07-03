package com.ll.townforest.boundedContext.gym.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.gym.entity.GymHistory;
import com.ll.townforest.boundedContext.gym.entity.GymMembership;
import com.ll.townforest.boundedContext.gym.entity.GymTicket;
import com.ll.townforest.boundedContext.gym.repository.GymHistoryRepository;
import com.ll.townforest.boundedContext.gym.repository.GymMembershipRepository;
import com.ll.townforest.boundedContext.gym.repository.GymRepository;
import com.ll.townforest.boundedContext.gym.repository.GymTicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GymService {
	private final GymTicketRepository gymTicketRepository;
	private final GymMembershipRepository gymMembershipRepository;
	private final GymHistoryRepository gymHistoryRepository;
	private final GymRepository gymRepository;

	public GymTicket getTicket(Integer ticketType) {
		GymTicket gymTicket = gymTicketRepository.findByType(ticketType).orElse(null);

		return gymTicket;
	}

	@Transactional
	public void create(AptAccount user, LocalDateTime startDate, Integer ticketType, String method) {

		GymTicket gymTicket = gymTicketRepository.findByType(ticketType).orElse(null);

		LocalDateTime endDate = getEndDate(gymTicket, startDate);

		GymMembership tmp = GymMembership.builder()
			.apt(user.getApt())
			.gym(gymRepository.findById(1L).orElse(null))
			.startDate(startDate)
			.endDate(endDate)
			.user(user)
			.build();

		gymMembershipRepository.save(tmp);

		GymHistory tmp2 = GymHistory.builder()
			.apt(user.getApt())
			.gym(gymRepository.findById(1L).orElse(null))
			.price(gymTicket.getPrice())
			.name(gymTicket.getName())
			.startDate(startDate)
			.endDate(endDate)
			.status(0)
			.paymentMethod(method)
			.user(user)
			.build();

		gymHistoryRepository.save(tmp2);

	}

	public List<GymTicket> getGymTickets(Long gymId) {

		return gymTicketRepository.findAllByGymId(gymId);

	}

	@Transactional
	public LocalDateTime getEndDate(GymTicket gymTicket, LocalDateTime startDate) {
		return startDate.plusDays(gymTicket.getDays());
	}
}
