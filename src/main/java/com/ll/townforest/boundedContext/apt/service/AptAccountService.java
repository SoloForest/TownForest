package com.ll.townforest.boundedContext.apt.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.account.entity.Account;
import com.ll.townforest.boundedContext.apt.DTO.AptAccountDTO;
import com.ll.townforest.boundedContext.apt.entity.Apt;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.entity.House;
import com.ll.townforest.boundedContext.apt.repository.AptAccountHouseRepository;
import com.ll.townforest.boundedContext.apt.repository.AptAccountRepository;
import com.ll.townforest.boundedContext.apt.repository.AptRepository;
import com.ll.townforest.boundedContext.apt.repository.HouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AptAccountService {
	private final AptRepository aptRepository;
	private final HouseRepository houseRepository;
	private final AptAccountRepository aptAccountRepository;
	private final AptAccountHouseRepository aptAccountHouseRepository;

	public Optional<AptAccount> findByAccount(Account account) {
		return aptAccountRepository.findByAccount(account);
	}

	public RsData<AptAccount> canRegister(Account account, AptAccountDTO aptAccountDTO) {
		Apt apt = aptRepository.findByName(aptAccountDTO.getAptName()).orElse(null);

		if (apt == null) {
			return RsData.of("F-1", "등록되지 않은 아파트입니다.");
		}

		House house = houseRepository.findByAptAndDongAndHo(apt, aptAccountDTO.getDong(), aptAccountDTO.getHo())
			.orElse(null);

		if (house == null) {
			return RsData.of("F-2", "거주하시는 동과 호수를 바르게 입력해 주세요.");
		}

		return register(account, apt, house, aptAccountDTO.getRelationship());
	}

	@Transactional
	public RsData<AptAccount> register(Account account, Apt apt, House house, String relationship) {
		AptAccount aptAccount = AptAccount.builder()
			.account(account)
			.apt(apt)
			.build();

		aptAccountRepository.save(aptAccount);

		AptAccountHouse aptAccountHouse = AptAccountHouse.builder()
			.relationship(relationship)
			.user(aptAccount)
			.house(house)
			.build();

		aptAccountHouseRepository.save(aptAccountHouse);

		return RsData.of("S-1", "내 아파트 등록 요청을 완료했습니다.<br>관리자의 승인을 기다려주세요.", aptAccount);
	}
}
