package com.ll.townforest.boundedContext.gym.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.gym.entity.GymTicket;

public interface GymTicketRepository extends JpaRepository<GymTicket, Long> {
	List<GymTicket> findAllByGymId(Long gymId);

	Optional<GymTicket> findByType(Integer ticketType);
}
