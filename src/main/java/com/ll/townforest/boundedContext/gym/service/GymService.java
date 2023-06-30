package com.ll.townforest.boundedContext.gym.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.boundedContext.gym.entity.GymTicket;
import com.ll.townforest.boundedContext.gym.repository.GymTicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GymService {
	private final GymTicketRepository gymTicketRepository;

	public List<GymTicket> getGymTickets(Long gymId) {

		return gymTicketRepository.findAllByGymId(gymId);

	}

}
