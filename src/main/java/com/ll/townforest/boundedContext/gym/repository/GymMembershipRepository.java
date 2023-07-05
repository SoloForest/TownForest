package com.ll.townforest.boundedContext.gym.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.gym.entity.GymMembership;

public interface GymMembershipRepository extends JpaRepository<GymMembership, Long> {
	Optional<GymMembership> findByUserId(Long userId);

	// 관리자 화면에서 인원수를 나타내기 위한 리스트
	List<GymMembership> findByGymId(Long gymId);

	// 현재 이용권 만료 전인 회원 목록을 위한 페이징용 메서드
	Page<GymMembership> findAllByGymId(Long gymId, Pageable pageable);

}
