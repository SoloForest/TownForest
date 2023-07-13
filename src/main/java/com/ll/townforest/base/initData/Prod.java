package com.ll.townforest.base.initData;

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
import com.ll.townforest.boundedContext.apt.entity.House;
import com.ll.townforest.boundedContext.apt.repository.AptAccountRepository;
import com.ll.townforest.boundedContext.apt.repository.AptRepository;
import com.ll.townforest.boundedContext.apt.repository.HouseRepository;
import com.ll.townforest.boundedContext.gym.entity.Gym;
import com.ll.townforest.boundedContext.gym.entity.GymTicket;
import com.ll.townforest.boundedContext.gym.repository.GymRepository;
import com.ll.townforest.boundedContext.gym.repository.GymTicketRepository;
import com.ll.townforest.boundedContext.library.entity.Library;
import com.ll.townforest.boundedContext.library.entity.Seat;
import com.ll.townforest.boundedContext.library.repository.LibraryRepository;
import com.ll.townforest.boundedContext.library.repository.SeatRepository;

@Configuration
@Profile("prod")
public class Prod {
	@Bean
	CommandLineRunner initData(
		PasswordEncoder passwordEncoder,
		AccountRepository accountRepository,
		AptAccountRepository aptAccountRepository,
		AptRepository aptRepository,
		HouseRepository houseRepository,
		LibraryRepository libraryRepository,
		SeatRepository seatRepository,
		GymRepository gymRepository,
		GymTicketRepository gymTicketRepository
	) {
		return new CommandLineRunner() {
			@Override
			@Transactional
			public void run(String... args) throws Exception {
				List<Account> accountList = new ArrayList<>();
				// 계정 생성
				if (accountRepository.count() == 0) {
					Account account1 = Account.builder()
						.username("admin")
						.password(passwordEncoder.encode("admin1!"))
						.fullName("admin")
						.email("admin@test.com")
						.phoneNumber("01012345678")
						.build();
					accountList.add(account1);

					Account account2 = Account.builder()
						.username("library")
						.password(passwordEncoder.encode("library1!"))
						.fullName("library_admin")
						.email("forestLibrary@test.com")
						.phoneNumber("01056781234")
						.build();
					accountList.add(account2);

					Account account3 = Account.builder()
						.username("gym")
						.password(passwordEncoder.encode("gym1!"))
						.fullName("gym_admin")
						.email("gym@test.com")
						.phoneNumber("01012341234")
						.build();
					accountList.add(account3);

					accountRepository.saveAll(accountList);
				} else {
					accountList = accountRepository.findAll();
				}

				List<Apt> aptList = new ArrayList<>();

				// 아파트 생성
				if (aptRepository.count() == 0) {
					Apt apt1 = Apt.builder()
						.name("forest")
						.maxApartment(500)
						.maxDong(5)
						.maxHo(4)
						.maxFloor(25)
						.build();

					aptList.add(apt1);
					aptRepository.save(apt1);
				} else {
					aptList = aptRepository.findAll();
				}

				List<House> houseList1 = new ArrayList<>();
				List<House> houseList2 = new ArrayList<>();
				List<House> houseList3 = new ArrayList<>();

				Apt apt1 = aptList.get(0);
				// 아파트 동호수 객체 생성
				if (houseRepository.count() == 0) {
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

					// 아파트 동호수 객체 생성 - 세 번째 1/3 - 꼭대기층은 게스트 하우스로 지정
					for (int i = oneThirdDong * 2 + 1; i <= apt1.getMaxDong(); i++) {
						for (int j = 1; j <= apt1.getMaxFloor(); j++) {
							for (int k = 1; k <= apt1.getMaxHo(); k++) {
								House houseTemp = House.builder()
									.dong(100 + i)
									.ho((j * 100) + k)
									.apt(apt1)
									.build();
								if (j == apt1.getMaxFloor()) {
									houseTemp = houseTemp.toBuilder().type(1).build();
								}
								houseList3.add(houseTemp);
							}
						}
					}

					houseRepository.saveAll(houseList3);
				}

				List<AptAccount> aptAccountList = new ArrayList<>();
				// 관리자 지정
				if (aptAccountRepository.count() == 0) {

					// 관리자
					AptAccount aptAccount1 = AptAccount.builder()
						.account(accountList.get(0))
						.apt(apt1)
						.authority(1)
						.status(true)
						.build();

					aptAccountList.add(aptAccount1);

					// 독서실 관리자
					AptAccount aptAccount2 = AptAccount.builder()
						.account(accountList.get(1))
						.apt(apt1)
						.authority(2)
						.status(true)
						.build();

					aptAccountList.add(aptAccount2);

					//헬스장 관리자
					AptAccount aptAccount3 = AptAccount.builder()
						.account(accountList.get(2))
						.apt(apt1)
						.authority(3)
						.status(true)
						.build();

					aptAccountList.add(aptAccount3);

					aptAccountRepository.saveAll(aptAccountList);
				} else
					aptAccountList = aptAccountRepository.findAll();

				List<Library> libraries = new ArrayList<>();

				// 독서실 생성
				if (libraryRepository.count() == 0) {
					Library library1 = Library.builder()
						.apart(apt1) // apt1.name : forest
						.name("forest library")
						.maxPeople(100)
						.seatingChart("librarySeat.png")
						.build();

					libraries.add(library1);
					libraryRepository.saveAll(libraries);
				}

				List<Seat> seatList = new ArrayList<>();

				// 독서실 좌석 생성
				if (seatRepository.count() == 0) {
					for (int i = 1; i <= libraries.get(0).getMaxPeople(); i++) {
						Seat seatTemp = Seat.builder()
							.library(libraries.get(0))
							.seatNumber(i)
							.build();
						seatList.add(seatTemp);
					}

					seatRepository.saveAll(seatList);
				}

				List<Gym> gymList = new ArrayList<>();

				if (gymRepository.count() == 0) {

					Gym gym1 = Gym.builder()
						.apt(apt1)
						.name("forestGym")
						.build();
					gymList.add(gym1);
					gymRepository.saveAll(gymList);
				} else
					gymList = gymRepository.findAll();

				List<GymTicket> gymTicketList = new ArrayList<>();
				if (gymTicketRepository.count() == 0) {

					GymTicket gymTicket1 = GymTicket.builder()
						.price(1000)
						.apt(apt1)
						.gym(gymList.get(0))
						.days(0)
						.name("1일권")
						.build();

					gymTicketList.add(gymTicket1);

					GymTicket gymTicket2 = GymTicket.builder()
						.price(30000)
						.apt(apt1)
						.gym(gymList.get(0))
						.days(29)
						.name("30일권")
						.build();

					gymTicketList.add(gymTicket2);

					GymTicket gymTicket3 = GymTicket.builder()
						.price(54000)
						.content("10% 할인가")
						.apt(apt1)
						.gym(gymList.get(0))
						.days(59)
						.name("60일권")
						.build();

					gymTicketList.add(gymTicket3);

					GymTicket gymTicket4 = GymTicket.builder()
						.price(72000)
						.content("20% 할인가")
						.apt(apt1)
						.gym(gymList.get(0))
						.days(89)
						.name("90일권")
						.build();

					gymTicketList.add(gymTicket4);

					gymTicketRepository.saveAll(gymTicketList);
				}
			}
		};
	}
}