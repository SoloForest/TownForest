package com.ll.townforest.boundedContext.apt.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.repository.AptAccountHouseRepository;
import com.ll.townforest.boundedContext.apt.repository.AptAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AptAccountHouseService {
	private final AptAccountRepository aptAccountRepository;
	private final AptAccountHouseRepository aptAccountHouseRepository;

	public Optional<AptAccountHouse> findById(Long id) {
		return aptAccountHouseRepository.findById(id);
	}

	public Optional<AptAccountHouse> findByAptAccount(AptAccount aptAccount) {
		return aptAccountHouseRepository.findByUser(aptAccount);
	}

	public Page<AptAccountHouse> findAptAccountHouseBySortCode(int page, int sortCode) {
    Pageable pageable = PageRequest.of(page, 10);
  
		return switch (sortCode) {
			case 2 -> aptAccountHouseRepository.findByUser_StatusTrueAndStatusFalseOrderByUserIdDesc(pageable);
			case 3 -> aptAccountHouseRepository.findByUser_StatusFalseAndStatusFalseOrderByUserIdDesc(pageable);
			default -> aptAccountHouseRepository.findAllByStatusFalseOrderByIdDesc(pageable);
		};
	}

	@Transactional
	public RsData<AptAccountHouse> canApprove(AptAccount aptAccount, AptAccountHouse aptAccountHouse) {
		if (aptAccount.getAuthority() != 1) {
			return RsData.of("F-1", "해당 회원을 승인할 권한이 없습니다.");
		}

		if (aptAccountHouse == null) {
			return RsData.of("F-2", "존재하지 않는 회원 입니다.");
		}

		return approve(aptAccountHouse);
	}

	@Transactional
	public RsData<AptAccountHouse> approve(AptAccountHouse aptAccountHouse) {
		AptAccount aptAccount = aptAccountHouse.getUser();
		AptAccount modifyAptAccount = aptAccount.toBuilder()
			.status(true)
			.build();

		aptAccountRepository.save(modifyAptAccount);
		return RsData.of("S-1", "승인되었습니다.");
	}

	@Transactional
	public RsData<AptAccountHouse> canDelete(AptAccount aptAccount, AptAccountHouse aptAccountHouse) {
		if (aptAccount.getAuthority() != 1) {
			return RsData.of("F-1", "해당 회원을 삭제할 권한이 없습니다.");
		}

		if (aptAccountHouse == null) {
			return RsData.of("F-2", "존재하지 않는 회원 입니다.");
		}

		return delete(aptAccountHouse);
	}

	@Transactional
	public RsData<AptAccountHouse> delete(AptAccountHouse aptAccountHouse) {
		String userFullName = aptAccountHouse.getUser().getAccount().getFullName();

		aptAccountHouseRepository.delete(aptAccountHouse);

		return RsData.of("S-1", "%s님이 삭제되었습니다.".formatted(userFullName));
	}
}
