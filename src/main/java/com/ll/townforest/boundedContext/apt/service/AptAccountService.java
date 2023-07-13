package com.ll.townforest.boundedContext.apt.service;

import java.util.List;
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

		Optional<AptAccountHouse> householder = aptAccountHouseRepository.findByHouseAndRelationship(house, "본인");
		if (householder.isPresent()) {
			return RsData.of("F-3", "세대주가 이미 존재합니다.");
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

		return RsData.of("S-1", "내 아파트 등록 요청을 완료했습니다.", aptAccount);
	}

	public RsData<String> makeAddressToString(AptAccount user) {

		AptAccountHouse aptAccountHouse = aptAccountHouseRepository.findByUserId(user.getId()).orElse(null);
		if (aptAccountHouse == null) {
			return RsData.of("F-1", "주소를 알 수 없는 계정입니다.");
		}

		return RsData.of("S-1", "주소를 문자열로 변환합니다.",
			aptAccountHouse.getHouse().getDong() + "동 " + aptAccountHouse.getHouse().getHo() + "호");
	}

	public List<AptAccountHouse> findAllByHouse(AptAccountHouse aptAccountHouse) {
		if (aptAccountHouse == null)
			return null;
		House house = aptAccountHouse.getHouse();
		List<AptAccountHouse> household = aptAccountHouseRepository.findAllByHouseAndStatusFalse(house);
		household.remove(aptAccountHouse);

		return household;
	}
}
