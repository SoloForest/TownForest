package com.ll.townforest.base.initData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.boundedContext.account.entity.Account;
import com.ll.townforest.boundedContext.account.repository.AccountRepository;
import com.ll.townforest.boundedContext.apt.entity.Apt;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.entity.House;
import com.ll.townforest.boundedContext.apt.repository.AptAccountHouseRepository;
import com.ll.townforest.boundedContext.apt.repository.AptAccountRepository;
import com.ll.townforest.boundedContext.apt.repository.AptRepository;
import com.ll.townforest.boundedContext.apt.repository.HouseRepository;
import com.ll.townforest.boundedContext.library.entity.Library;
import com.ll.townforest.boundedContext.library.entity.Seat;
import com.ll.townforest.boundedContext.library.repository.LibraryRepository;
import com.ll.townforest.boundedContext.library.repository.SeatRepository;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
	@Bean
	CommandLineRunner initData(
		AccountRepository accountRepository,
		AptAccountRepository aptAccountRepository,
		AptRepository aptRepository,
		HouseRepository houseRepository,
		AptAccountHouseRepository aptAccountHouseRepository,
		LibraryRepository libraryRepository,
		SeatRepository seatRepository
	) {
		return new CommandLineRunner() {
			@Override
			@Transactional
			public void run(String... args) throws Exception {
				Account act = Account.builder()
					.password("12341234")
					.username("aaaa")
					.build();
				accountRepository.save(act);

				Account account1 = Account.builder()
					.userId("admin")
					.password("admin1!")
					.username("admin")
					.email("admin@test.com")
					.phoneNumber("01012345678")
					.build();
				accountRepository.save(account1);

				Account account2 = Account.builder()
					.userId("library")
					.password("library1!")
					.username("library admin")
					.email("forestLibrary@test.com")
					.phoneNumber("01056781234")
					.build();
				accountRepository.save(account2);

				Account account3 = Account.builder()
					.userId("gym")
					.password("gym1!")
					.username("gym admin")
					.email("gym@test.com")
					.phoneNumber("01012341234")
					.build();
				accountRepository.save(account3);

				Account account4 = Account.builder()
					.userId("yujin11006")
					.password("1234")
					.username("방유진")
					.email("yujin11006@test.com")
					.phoneNumber("01000000000")
					.build();
				accountRepository.save(account4);

				Account account5 = Account.builder()
					.userId("bbosong")
					.password("bbosong1!")
					.username("이송이")
					.email("bbosong@test.com")
					.phoneNumber("01098765432")
					.build();
				accountRepository.save(account5);

				Account account6 = Account.builder()
					.userId("chan")
					.password("0000")
					.username("이은찬")
					.email("chan@test.com")
					.phoneNumber("01033334444")
					.build();
				accountRepository.save(account6);

				Account account7 = Account.builder()
					.userId("puar12")
					.password("1234")
					.username("박철현")
					.email("puar12@test.com")
					.phoneNumber("01099999999")
					.build();
				accountRepository.save(account7);

				// 아파트 생성
				Apt apt1 = Apt.builder()
					.name("forest")
					.maxApartment(500)
					.maxDong(5)
					.maxHo(4)
					.maxFloor(25)
					.build();
				aptRepository.save(apt1);

				List<House> houseList1 = new ArrayList<>();
				List<House> houseList2 = new ArrayList<>();
				List<House> houseList3 = new ArrayList<>();

				// 아파트 동호수 객체 생성 - 첫 번째 1/3
				int oneThirdDong = apt1.getMaxDong() / 3;
				for (int i = 1; i <= oneThirdDong; i++) {
					for (int j = 1; j <= apt1.getMaxFloor(); j++) {
						for (int k = 1; k <= apt1.getMaxHo(); k++) {
							House houseTemp = House.builder()
								.dong(100 + i)
								.ho((j * 100) + k)
								.build();
							houseList1.add(houseTemp);
						}
					}
				}

				houseRepository.saveAll(houseList1);

				// 아파트 동호수 객체 생성 - 두 번째 1/3
				for (int i = oneThirdDong + 1; i <= oneThirdDong * 2; i++) {
					for (int j = 1; j <= apt1.getMaxFloor(); j++) {
						for (int k = 1; k <= apt1.getMaxHo(); k++) {
							House houseTemp = House.builder()
								.dong(100 + i)
								.ho((j * 100) + k)
								.build();
							houseList2.add(houseTemp);
						}
					}
				}

				houseRepository.saveAll(houseList2);

				// 아파트 동호수 객체 생성 - 세 번째 1/3
				for (int i = oneThirdDong * 2 + 1; i <= apt1.getMaxDong(); i++) {
					for (int j = 1; j <= apt1.getMaxFloor(); j++) {
						for (int k = 1; k <= apt1.getMaxHo(); k++) {
							House houseTemp = House.builder()
								.dong(100 + i)
								.ho((j * 100) + k)
								.build();
							houseList3.add(houseTemp);
						}
					}
				}

				houseRepository.saveAll(houseList3);
				// 관리자
				AptAccount aptAccount1 = AptAccount.builder()
					.account(account1)
					.apt(apt1)
					.authority(1)
					.status(true)
					.build();
				aptAccountRepository.save(aptAccount1);

				// 독서실 관리자
				AptAccount aptAccount2 = AptAccount.builder()
					.account(account2)
					.apt(apt1)
					.authority(2)
					.status(true)
					.build();
				aptAccountRepository.save(aptAccount2);

				//헬스장 관리자
				AptAccount aptAccount3 = AptAccount.builder()
					.account(account3)
					.apt(apt1)
					.authority(3)
					.status(true)
					.build();
				aptAccountRepository.save(aptAccount3);

				//주민1
				AptAccount aptAccount4 = AptAccount.builder()
					.account(account4)
					.apt(apt1)
					.status(true)
					.build();
				aptAccountRepository.save(aptAccount4);

				//주민2
				AptAccount aptAccount5 = AptAccount.builder()
					.account(account5)
					.apt(apt1)
					.build();
				aptAccountRepository.save(aptAccount5);

				//주민3
				AptAccount aptAccount6 = AptAccount.builder()
					.account(account6)
					.apt(apt1)
					.build();
				aptAccountRepository.save(aptAccount6);

				//주민4
				AptAccount aptAccount7 = AptAccount.builder()
					.account(account7)
					.apt(apt1)
					.build();
				aptAccountRepository.save(aptAccount7);

				//주민과 주거지 연결
				AptAccountHouse aptAccountHouse1 = AptAccountHouse.builder()
					.relationship("세대주")
					.user(aptAccount4)
					.house(houseRepository.findByDongAndHo(103, 2101))
					.build();
				aptAccountHouseRepository.save(aptAccountHouse1);

				// 독서실 생성
				Library library1 = Library.builder()
					.apart(apt1) // apt1.name : forest
					.name("forest library")
					.maxPeople(100)
					.seatingChart("librarySeat.png")
					.build();
				libraryRepository.save(library1);

				// 독서실 좌석 생성
				for (int i = 1; i <= library1.getMaxPeople(); i++) {
					Seat seatTemp = Seat.builder()
						.library(library1)
						.seatNumber(i)
						.build();
					seatRepository.save(seatTemp);
				}
			}
		};
	}
}