package com.ll.townforest.boundedContext.gym.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.rsData.RsData;
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
	public void create(AptAccount user, LocalDate startDate, Integer ticketType, String method) {

		GymTicket gymTicket = gymTicketRepository.findByType(ticketType).orElse(null);
		if (gymTicket == null) {
			throw new RuntimeException("존재하지 않는 이용권입니다. 다시 시도해주세요");
		}

		LocalDate endDate = getEndDate(gymTicket, startDate);

		// 기본값은 오늘, 이용중인 상태
		int status = 1;
		// 만일 시작날이 미래라면, 이용 대기중인 상태
		if (startDate.isAfter(LocalDate.now())) {
			status = 0;
		}

		GymMembership tmp = GymMembership.builder()
			.apt(user.getApt())
			.gym(gymRepository.findById(1L).orElse(null))
			.startDate(startDate)
			.endDate(endDate)
			.user(user)
			.status(status)
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
	public LocalDate getEndDate(GymTicket gymTicket, LocalDate startDate) {
		return startDate.plusDays(gymTicket.getDays());
	}

	public GymMembership getMembershipByUser(AptAccount user) {
		return gymMembershipRepository.findByUserId(user.getId()).orElse(null);
	}

	@Transactional
	public void update(AptAccount user, LocalDate startDate, LocalDate endDate, int ticketType, String method) {
		GymTicket gymTicket = gymTicketRepository.findByType(ticketType).orElse(null);

		if (gymTicket == null) {
			throw new RuntimeException("존재하지 않는 이용권입니다. 다시 시도해주세요");
		}

		GymMembership gymMembership = gymMembershipRepository.findByUserId(user.getId()).orElse(null);

		if (gymMembership == null) {
			throw new RuntimeException("잘못된 접근입니다.(이용권 연장이 아닌 결제를 이용해주세요)");
		}

		GymMembership updateMembership = gymMembership
			.toBuilder()
			.endDate(endDate)
			.status(3)  // 3은 연장 상태를 나타냄
			.paymentDate(LocalDateTime.now())
			.build();
		gymMembershipRepository.save(updateMembership);

		GymHistory tmp2 = GymHistory.builder()
			.apt(user.getApt())
			.gym(gymRepository.findById(1L).orElse(null))
			.price(gymTicket.getPrice())
			.name(gymTicket.getName())
			.startDate(startDate)
			.endDate(endDate)
			.status(1) // 1은 연장을 나타냄
			.paymentMethod(method)
			.user(user)
			.build();

		gymHistoryRepository.save(tmp2);
	}

	public Page<GymHistory> getPersonalHistories(int page, Long userId) {
		Pageable pageable = PageRequest.of(page, 5);
		return gymHistoryRepository.findAllByUserIdOrderByIdDesc(userId, pageable);
	}

	public GymMembership getMembershopByMembershipId(Long membershipId) {
		return gymMembershipRepository.findById(membershipId).get();
	}

	@Transactional
	public RsData<GymMembership> pauseMembership(GymMembership pauseMembership) {
		LocalDate localDate = LocalDate.now();

		long remainingDay = ChronoUnit.DAYS.between(localDate, pauseMembership.getEndDate());

		if (remainingDay <= 0)
			return RsData.of("F-1", "당일 종료권 또는 기한 지난 이용권은 일시정지가 불가능합니다.");

		GymMembership tmp = pauseMembership.toBuilder()
			.status(2)
			.pauseDate(localDate)
			.remainingDay((int)remainingDay)
			.build();

		AptAccount user = pauseMembership.getUser();

		gymMembershipRepository.save(tmp);

		GymHistory tmp2 = GymHistory.builder()
			.status(2)
			.pauseDate(localDate)
			.remainingDay((int)remainingDay)
			.apt(tmp.getApt())
			.gym(tmp.getGym())
			.user(tmp.getUser())
			.startDate(tmp.getStartDate())
			.endDate(tmp.getEndDate())
			.build();

		gymHistoryRepository.save(tmp2);

		return RsData.of("S-1", "일시정지 성공");

	}

	@Transactional
	public RsData<GymMembership> unPauseMembership(GymMembership pauseMembership) {

		LocalDate localDate = LocalDate.now();
		LocalDate pauseDate = pauseMembership.getPauseDate();

		// 이용 종료일 갱신
		int remainingDay = pauseMembership.getRemainingDay();
		LocalDate updatedEndDate = pauseDate.plusDays(remainingDay);

		GymMembership tmp = pauseMembership.toBuilder()
			.status(4)
			.pauseDate(null)
			.remainingDay(null)
			.endDate(updatedEndDate)
			.build();

		gymMembershipRepository.save(tmp);

		GymHistory tmp2 = GymHistory.builder()
			.status(3)
			.apt(tmp.getApt())
			.gym(tmp.getGym())
			.user(tmp.getUser())
			.startDate(tmp.getStartDate())
			.endDate(tmp.getEndDate())
			.build();

		gymHistoryRepository.save(tmp2);

		return RsData.of("S-1", "일시정지 해제");
	}
}
