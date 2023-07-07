package com.ll.townforest.base.initData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.ll.townforest.boundedContext.gym.entity.Gym;
import com.ll.townforest.boundedContext.gym.entity.GymHistory;
import com.ll.townforest.boundedContext.gym.entity.GymMembership;
import com.ll.townforest.boundedContext.gym.entity.GymTicket;
import com.ll.townforest.boundedContext.gym.repository.GymHistoryRepository;
import com.ll.townforest.boundedContext.gym.repository.GymMembershipRepository;
import com.ll.townforest.boundedContext.gym.repository.GymRepository;
import com.ll.townforest.boundedContext.gym.repository.GymTicketRepository;
import com.ll.townforest.boundedContext.gym.service.GymService;
import com.ll.townforest.boundedContext.library.entity.Library;
import com.ll.townforest.boundedContext.library.entity.Seat;
import com.ll.townforest.boundedContext.library.repository.LibraryRepository;
import com.ll.townforest.boundedContext.library.repository.SeatRepository;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
	@Bean
	CommandLineRunner initData(
		PasswordEncoder passwordEncoder,
		AccountRepository accountRepository,
		AptAccountRepository aptAccountRepository,
		AptRepository aptRepository,
		HouseRepository houseRepository,
		AptAccountHouseRepository aptAccountHouseRepository,
		LibraryRepository libraryRepository,
		SeatRepository seatRepository,
		GymRepository gymRepository,
		GymTicketRepository gymTicketRepository,
		GymService gymService,
		GymHistoryRepository gymHistoryRepository,
		GymMembershipRepository gymMembershipRepository
	) {
		return new CommandLineRunner() {
			@Override
			@Transactional
			public void run(String... args) throws Exception {
				Account account1 = Account.builder()
					.username("admin")
					.password(passwordEncoder.encode("admin1!"))
					.fullName("admin")
					.email("admin@test.com")
					.phoneNumber("01012345678")
					.build();
				accountRepository.save(account1);

				Account account2 = Account.builder()
					.username("library")
					.password(passwordEncoder.encode("library1!"))
					.fullName("library_admin")
					.email("forestLibrary@test.com")
					.phoneNumber("01056781234")
					.build();
				accountRepository.save(account2);

				Account account3 = Account.builder()
					.username("gym")
					.password(passwordEncoder.encode("gym1!"))
					.fullName("gym_admin")
					.email("gym@test.com")
					.phoneNumber("01012341234")
					.build();
				accountRepository.save(account3);

				Account account4 = Account.builder()
					.username("yujin11006")
					.password(passwordEncoder.encode("1234"))
					.fullName("방유진")
					.email("yujin11006@test.com")
					.phoneNumber("01000000000")
					.build();
				accountRepository.save(account4);

				Account account5 = Account.builder()
					.username("bbosong")
					.password(passwordEncoder.encode("bbosong1!"))
					.fullName("이송이")
					.email("bbosong@test.com")
					.phoneNumber("01098765432")
					.build();
				accountRepository.save(account5);

				Account account6 = Account.builder()
					.username("chan")
					.password(passwordEncoder.encode("0000"))
					.fullName("이은찬")
					.email("chan@test.com")
					.phoneNumber("01033334444")
					.build();
				accountRepository.save(account6);

				Account account7 = Account.builder()
					.username("puar12")
					.password(passwordEncoder.encode("1234"))
					.fullName("박철현")
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
								.apt(apt1)
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
								.apt(apt1)
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
								.apt(apt1)
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
					.house(houseRepository.findByAptAndDongAndHo(apt1, 103, 2101).get())
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

				Gym gym1 = Gym.builder()
					.apt(apt1)
					.name("forestGym")
					.build();
				gymRepository.save(gym1);

				GymTicket gymTicket1 = GymTicket.builder()
					.price(1000)
					.type(1)
					.apt(apt1)
					.gym(gym1)
					.days(0)
					.name("1일권")
					.build();

				gymTicketRepository.save(gymTicket1);

				GymTicket gymTicket2 = GymTicket.builder()
					.price(30000)
					.type(2)
					.apt(apt1)
					.gym(gym1)
					.days(29)
					.name("30일권")
					.build();

				gymTicketRepository.save(gymTicket2);

				GymTicket gymTicket3 = GymTicket.builder()
					.price(54000)
					.type(3)
					.content("10% 할인가")
					.apt(apt1)
					.gym(gym1)
					.days(59)
					.name("60일권")
					.build();

				gymTicketRepository.save(gymTicket3);
				GymTicket gymTicket4 = GymTicket.builder()
					.price(72000)
					.type(4)
					.content("20% 할인가")
					.apt(apt1)
					.gym(gym1)
					.days(89)
					.name("90일권")
					.build();
				gymTicketRepository.save(gymTicket4);

				gymService.create(aptAccount4, LocalDate.now(), 3, "카드");

				Account tmp1111 = Account.builder()
					.username("test" + 0)
					.password(passwordEncoder.encode("1234"))
					.fullName("test" + 0)
					.email("test" + 0 + "@test.com")
					.phoneNumber("010123456" + 00)
					.build();

				accountRepository.save(tmp1111);

				AptAccount tmp222 = AptAccount.builder()
					.account(tmp1111)
					.apt(apt1)
					.status(true)
					.build();

				aptAccountRepository.save(tmp222);

				gymService.create(tmp222, LocalDate.now(), 3, "카드");

				List<GymHistory> gymHistoryList = new ArrayList<>();

				for (int i = 0; i < 50; i++) {
					GymHistory tmp = GymHistory.builder()
						.user(aptAccount4)
						.gym(gym1)
						.apt(apt1)
						.startDate(LocalDate.now())
						.endDate(LocalDate.now())
						.paymentDate(LocalDateTime.now())
						.price(1000)
						.paymentMethod("카드")
						.status(0)
						.build();
					gymHistoryList.add(tmp);
				}

				gymHistoryRepository.saveAll(gymHistoryList);

				List<Account> tmpList = new ArrayList<>();
				List<AptAccount> tmpAptAccountList = new ArrayList<>();

				for (int i = 1; i < 50; i++) {
					String tmpNum = "" + i;
					if (i < 10)
						tmpNum = "0" + tmpNum;

					Account tmp = Account.builder()
						.username("test" + i)
						.password(passwordEncoder.encode("1234"))
						.fullName("test" + i)
						.email("test" + i + "@test.com")
						.phoneNumber("010123456" + tmpNum)
						.build();

					tmpList.add(tmp);
				}

				accountRepository.saveAll(tmpList);

				for (Account account : tmpList) {
					AptAccount tmp2 = AptAccount.builder()
						.account(account)
						.apt(apt1)
						.build();
					tmpAptAccountList.add(tmp2);
				}

				aptAccountRepository.saveAll(tmpAptAccountList);

				List<GymMembership> gymTicketList22 = new ArrayList<>();

				for (AptAccount aptAccount : tmpAptAccountList) {
					GymMembership membership = GymMembership.builder()
						.user(aptAccount)
						.gym(gym1)
						.apt(apt1)
						.startDate(LocalDate.now())
						.endDate(LocalDate.now())
						.paymentDate(LocalDateTime.now())
						.status(1)
						.build();
					gymTicketList22.add(membership);
				}

				gymMembershipRepository.saveAll(gymTicketList22);
			}
		};
	}
}