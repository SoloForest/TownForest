package com.ll.townforest.boundedContext.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ll.townforest.boundedContext.gym.entity.GymMembership;

public interface GymMembershipRepository extends JpaRepository<GymMembership, Long> {
}
