package com.ll.townforest.boundedContext.gym.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.Apt;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.service.AptAccountService;
import com.ll.townforest.boundedContext.gym.entity.Gym;
import com.ll.townforest.boundedContext.gym.entity.GymHistory;
import com.ll.townforest.boundedContext.gym.entity.GymMembership;
import com.ll.townforest.boundedContext.gym.entity.GymTicket;
import com.ll.townforest.boundedContext.gym.repository.GymHistoryRepository;
import com.ll.townforest.boundedContext.gym.repository.GymMembershipRepository;
import com.ll.townforest.boundedContext.gym.repository.GymRepository;
import com.ll.townforest.boundedContext.gym.repository.GymTicketRepository;
import com.ll.townforest.boundedContext.home.dto.SearchDTO;
import com.ll.townforest.boundedContext.home.dto.TicketForm;
import com.ll.townforest.standard.util.Ut;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GymService {
	private final GymTicketRepository gymTicketRepository;
	private final GymMembershipRepository gymMembershipRepository;
	private final GymHistoryRepository gymHistoryRepository;
	private final GymRepository gymRepository;

	private final AptAccountService aptAccountService;

	private final Rq rq;

	public GymTicket getTicket(Long ticketId) {
		GymTicket gymTicket = gymTicketRepository.findById(ticketId).orElse(null);

		return gymTicket;
	}

	@Transactional
	public void create(AptAccount user, LocalDate startDate, Long ticketId, String method) {

		GymTicket gymTicket = gymTicketRepository.findById(ticketId).orElse(null);
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
		String userName = user.getAccount().getFullName();
		String contact = user.getAccount().getPhoneNumString();
		String userAddress = aptAccountService.makeAddressToString(user).getData();
		Gym gym = gymRepository.findById(1L).orElse(null);
		Apt apt = user.getApt();

		GymMembership tmp = GymMembership.builder()
			.apt(apt)
			.gym(gym)
			.startDate(startDate)
			.endDate(endDate)
			.user(user)
			.status(status)
			.userName(userName != null ? userName : "알수없음")
			.paymentDate(LocalDateTime.now())
			.contact(contact != null ? contact : "알수없음")
			.address(userAddress != null ? userAddress : "알수없음")
			.build();

		gymMembershipRepository.save(tmp);

		GymHistory tmp2 = GymHistory.builder()
			.apt(apt)
			.gym(gym)
			.price(gymTicket.getPrice())
			.name(gymTicket.getName())
			.startDate(startDate)
			.endDate(endDate)
			.status(0)
			.paymentMethod(method)
			.paymentDate(LocalDateTime.now())
			.user(user)
			.userName(userName != null ? userName : "알수없음")
			.contact(contact != null ? contact : "알수없음")
			.address(userAddress != null ? userAddress : "알수없음")
			.build();

		gymHistoryRepository.save(tmp2);

	}

	public List<GymTicket> getGymTicketList(Long gymId) {

		return gymTicketRepository.findAllByGymIdOrderByPrice(gymId);

	}

	@Transactional
	public LocalDate getEndDate(GymTicket gymTicket, LocalDate startDate) {
		return startDate.plusDays(gymTicket.getDays());
	}

	public GymMembership getMembershipByUser(AptAccount user) {
		return gymMembershipRepository.findByUserId(user.getId()).orElse(null);
	}

	@Transactional
	public void update(AptAccount user, LocalDate startDate, LocalDate endDate, Long ticketId, String method) {
		GymTicket gymTicket = gymTicketRepository.findById(ticketId).orElse(null);

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

		String userName = user.getAccount().getFullName();
		String contact = user.getAccount().getPhoneNumString();
		String userAddress = aptAccountService.makeAddressToString(user).getData();

		GymHistory tmp2 = GymHistory.builder()
			.apt(user.getApt())
			.gym(updateMembership.getGym())
			.price(gymTicket.getPrice())
			.name(gymTicket.getName())
			.startDate(startDate)
			.endDate(endDate)
			.status(1) // 1은 연장을 나타냄
			.paymentMethod(method)
			.user(user)
			.userName(userName != null ? userName : "알수없음")
			.contact(contact != null ? contact : "알수없음")
			.address(userAddress != null ? userAddress : "알수없음")
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
			.status(2) // 2는 일시정지상태
			.pauseDate(localDate)
			.remainingDay((int)remainingDay)
			.build();

		AptAccount user = pauseMembership.getUser();

		gymMembershipRepository.save(tmp);

		String userName = user.getAccount().getFullName();
		String contact = user.getAccount().getPhoneNumString();
		String userAddress = aptAccountService.makeAddressToString(user).getData();

		GymHistory tmp2 = GymHistory.builder()
			.status(2)
			.pauseDate(localDate)
			.remainingDay((int)remainingDay)
			.apt(tmp.getApt())
			.gym(tmp.getGym())
			.user(tmp.getUser())
			.startDate(tmp.getStartDate())
			.endDate(tmp.getEndDate())
			.userName(userName != null ? userName : "알수없음")
			.contact(contact != null ? contact : "알수없음")
			.address(userAddress != null ? userAddress : "알수없음")
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
			.status(4)  // 4는 재시작을 나타냄
			.pauseDate(null)
			.remainingDay(null)
			.endDate(updatedEndDate)
			.restartDate(localDate)
			.build();

		gymMembershipRepository.save(tmp);

		AptAccount user = pauseMembership.getUser();

		String userName = user.getAccount().getFullName();
		String contact = user.getAccount().getPhoneNumString();
		String userAddress = aptAccountService.makeAddressToString(user).getData();

		GymHistory tmp2 = GymHistory.builder()
			.status(3)
			.apt(tmp.getApt())
			.gym(tmp.getGym())
			.user(tmp.getUser())
			.startDate(tmp.getStartDate())
			.endDate(tmp.getEndDate())
			.restartDate(localDate)
			.userName(userName != null ? userName : "알수없음")
			.contact(contact != null ? contact : "알수없음")
			.address(userAddress != null ? userAddress : "알수없음")
			.build();

		gymHistoryRepository.save(tmp2);

		return RsData.of("S-1", "일시정지 해제");
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void delete() {
		LocalDate endDate = LocalDate.now().minusDays(1);

		// 일시정지 상태가 아니며, 어제 날짜가 끝인 이용권들을 찾는다.
		// 오늘 날짜가 끝이라면, 끝나는날도 이용은 가능하니 어제 날짜로 찾기
		// status가 2이면 일시정지 상태니까, 삭제하지 않는다.
		List<GymMembership> list = gymMembershipRepository.findAllByEndDateAndStatusNot(endDate, 2);
		gymMembershipRepository.deleteAll(list);

		List<GymHistory> histories = new ArrayList<>();

		// 누가 언제부터 언제까지 이용했던 헬스장 이용권 만료되었다 히스토리 남기기
		for (GymMembership gymMembership : list) {

			AptAccount user = gymMembership.getUser();

			String userName = user.getAccount().getFullName();
			String contact = user.getAccount().getPhoneNumString();
			String userAddress = aptAccountService.makeAddressToString(user).getData();

			GymHistory tmp = GymHistory.builder()
				.status(4) // 4는 만료
				.gym(gymMembership.getGym())
				.startDate(gymMembership.getStartDate())
				.endDate(gymMembership.getEndDate())
				.apt(gymMembership.getApt())
				.user(gymMembership.getUser())
				.userName(userName != null ? userName : "알수없음")
				.contact(contact != null ? contact : "알수없음")
				.address(userAddress != null ? userAddress : "알수없음")
				.build();

			histories.add(tmp);
		}

		gymHistoryRepository.saveAll(histories);
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void beginMembership() {
		LocalDate today = LocalDate.now();

		// 아직 이용권 대기 상태이며, 오늘 날짜가 시작인 이용권들을 찾는다.
		// status를 0에서 1로 변경해준다(시작 대기 -> 시작)
		// history는 변동 없음(이용 대기, 이용중 -> 0번 으로 결제로 표기)
		List<GymMembership> searchlist = gymMembershipRepository.findAllByStartDateAndStatus(today, 0);

		List<GymMembership> updatedList = new ArrayList<>();
		for (GymMembership membership : searchlist) {
			GymMembership tmp1 = membership.toBuilder()
				.status(1)
				.build();
			updatedList.add(tmp1);
		}

		gymMembershipRepository.saveAll(updatedList);
	}

	public List<GymMembership> getMemberList(AptAccount user) {
		Apt apt = user.getApt();
		Gym gym = gymRepository.findByAptId(apt.getId()).get();
		if (gym == null)
			new RuntimeException("잘못된 접근입니다. 회원님의 아파트에는 gym이 없습니다.");

		return gymMembershipRepository.findByGymId(gym.getId());
	}

	public Page<GymMembership> getMemberPage(int page, AptAccount user, SearchDTO searchDTO) {
		Apt apt = user.getApt();
		Gym gym = gymRepository.findByAptId(apt.getId()).get();
		if (gym == null)
			new RuntimeException("잘못된 접근입니다. 회원님의 아파트에는 gym이 없습니다.");
		Pageable pageable = PageRequest.of(page, 5);

		Page<GymMembership> result = gymMembershipRepository.findAllByGymId(gym.getId(), pageable);

		// 검색어가 없다 -> 전체 리스트 반환
		// 검색어 대로 검색한 페이지 반환
		if (searchDTO.getSearchQuery() != null && searchDTO.getSearchQuery().trim().length() != 0) {
			// 이름 아니면 연락처로 검색 가능
			if ("name".equals(searchDTO.getSearchType())) {
				result = gymMembershipRepository.findAllByFullName(searchDTO.getSearchQuery(), pageable);
			} else {
				result = gymMembershipRepository.findAllByPhoneNumber(searchDTO.getSearchQuery(), pageable);
			}
		}

		return result;
	}

	public Page<GymHistory> getAllHistories(int page, SearchDTO searchDTO) {
		AptAccount user = rq.getAptAccount();
		Apt apt = user.getApt();
		Gym gym = gymRepository.findByAptId(apt.getId()).get();
		if (gym == null)
			new RuntimeException("잘못된 접근입니다. 회원님의 아파트에는 gym이 없습니다.");

		Pageable pageable = PageRequest.of(page, 5);

		// case 6 - 결제일자 필터 x, 검색어x : 가장 기본 검색이기에 먼저 값을 가져옴
		Page<GymHistory> result = gymHistoryRepository.findAllByGymId(gym.getId(), pageable);

		// 결제일 선택 안했다면 null로 바꾸기
		String yearMonth = searchDTO.getYearMonth();
		if (yearMonth != null) {
			if (yearMonth.isBlank()) {
				yearMonth = null;
			}
		}
		// case 1, 2, 3 - 결제일 필터
		if (yearMonth != null) {
			// 결제일 필터가 있다면 해당 달에 해당하는 날짜를 구하기 위함
			int monthEndDay = Ut.date.getEndDayOf(yearMonth);
			String fromDateStr = yearMonth + "-01 00:00:00.000000";
			String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
			LocalDateTime fromDate = Ut.date.parse(fromDateStr);
			LocalDateTime toDate = Ut.date.parse(toDateStr);

			// case 1, 2 - 결제일 필터와 검색어가 있을 경우
			if (searchDTO.getSearchQuery() != null && searchDTO.getSearchQuery().trim().length() != 0) {
				// case 1 - 이름 검색
				if (searchDTO.getSearchType().equals("name")) {
					result = gymHistoryRepository.findAllByFullNameAndPaymentDate(searchDTO.getSearchQuery(), fromDate,
						toDate, pageable);
				}
				// case 2 - 핸드폰 번호 검색
				if (searchDTO.getSearchType().equals("phone")) {
					result = gymHistoryRepository.findAllByPhoneNumberAndPaymentDate(searchDTO.getSearchQuery(),
						fromDate, toDate, pageable);
				}
			}
			// case 3 - 결제일 필터가 있으나 검색을 하지 않은 경우
			// gymId를 JQuery가 아니기에 넘겨줌
			else {
				result = gymHistoryRepository.findAllByGymIdAndPaymentDateBetweenOrderByIdAsc(gym.getId(), fromDate,
					toDate, pageable);
			}
		}
		// case 4, 5 -결제일 필터가 없다
		else {
			// 검색어가 없다 -> 전체 리스트 반환 case 6
			// 검색어가 있다 -> 검색한 페이지 반환
			if (searchDTO.getSearchQuery() != null && searchDTO.getSearchQuery().trim().length() != 0) {
				// 이름 아니면 연락처로 검색 가능
				// 해당 쿼리에 gym이 account가 속한 gym과 동일한지 검사하므로 gym을 넘겨주지 않음
				if (searchDTO.getSearchType().equals("name")) {
					result = gymHistoryRepository.findAllByFullName(searchDTO.getSearchQuery(), pageable);
				}
				if (searchDTO.getSearchType().equals("phone")) {
					result = gymHistoryRepository.findAllByPhoneNumber(searchDTO.getSearchQuery(), pageable);
				}
			}
		}

		return result;
	}

	@Transactional
	public RsData createTicket(AptAccount user, TicketForm ticketForm) {

		GymTicket gymTicket = GymTicket.builder()
			.days(ticketForm.getDays())
			.content(ticketForm.getContent())
			.price(ticketForm.getPrice())
			.name(ticketForm.getName())
			.apt(user.getApt())
			.gym(gymRepository.findById(1L).orElse(null))
			.build();

		gymTicketRepository.save(gymTicket);

		return RsData.of("S-1", "이용권 생성 성공");
	}

	@Transactional
	public RsData modifyTicket(GymTicket gymTicket, TicketForm ticketForm) {

		GymTicket modifyTicket = gymTicket.toBuilder()
			.days(ticketForm.getDays())
			.content(ticketForm.getContent())
			.name(ticketForm.getName())
			.price(ticketForm.getPrice())
			.build();

		gymTicketRepository.save(modifyTicket);

		return RsData.of("S-1", "이용권 정보 수정 성공");
	}

	@Transactional
	public RsData deleteTicket(GymTicket gymTicket) {

		gymTicketRepository.delete(gymTicket);

		return RsData.of("S-1", "이용권 삭제 성공");
	}
}
