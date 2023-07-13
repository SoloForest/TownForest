package com.ll.townforest.boundedContext.account.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.event.EventAccountWithdraw;
import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.account.dto.AccountDTO;
import com.ll.townforest.boundedContext.account.entity.Account;
import com.ll.townforest.boundedContext.account.repository.AccountRepository;
import com.ll.townforest.boundedContext.apt.DTO.EditForm;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.repository.AptAccountHouseRepository;
import com.ll.townforest.boundedContext.apt.repository.AptAccountRepository;
import com.ll.townforest.boundedContext.gym.entity.GymMembership;
import com.ll.townforest.boundedContext.gym.repository.GymMembershipRepository;
import com.ll.townforest.boundedContext.maintenance.repository.VehicleRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final AccountRepository accountRepository;
	private final AptAccountRepository aptAccountRepository;
	private final AptAccountHouseRepository aptAccountHouseRepository;
	private final GymMembershipRepository gymMembershipRepository;
	private final VehicleRepository vehicleRepository;
	private final ApplicationEventPublisher publisher;

	public Optional<Account> findByUsername(String username) {
		return accountRepository.findByUsername(username);
	}

	private Optional<Account> findByEmail(String email) {
		return accountRepository.findByEmail(email);
	}

	private Optional<Account> findByPhoneNumber(String phoneNumber) {
		return accountRepository.findByPhoneNumber(phoneNumber);
	}

	public RsData<Account> join(AccountDTO dto) {
		if (findByUsername(dto.getUsername()).isPresent()) {
			return RsData.of("F-1", "사용할 수 없는 아이디입니다.<br>다른 아이디를 입력해 주세요.");
		}

		if (findByEmail(dto.getEmail()).isPresent()) {
			return RsData.of("F-2", "사용할 수 없는 이메일입니다.<br>다른 이메일을 입력해 주세요.");
		}

		if (findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
			return RsData.of("F-3", "사용할 수 없는 휴대전화번호입니다.<br>다른 휴대전화번호를 입력해 주세요.");
		}

		String password = passwordEncoder.encode(dto.getPassword());

		Account account = Account
			.builder()
			.username(dto.getUsername())
			.password(password)
			.fullName(dto.getFullName())
			.email(dto.getEmail())
			.phoneNumber(dto.getPhoneNumber())
			.build();

		accountRepository.save(account);

		return RsData.of("S-1", "회원 가입이 완료되었습니다.", account);
	}

	@Transactional
	public RsData<Account> edit(Account account, EditForm editForm) {

		if (findByEmail(editForm.getEmail()).isPresent() && !findByEmail(editForm.getEmail()).get().equals(account)) {
			return RsData.of("F-1", "사용할 수 없는 이메일입니다.<br>다른 이메일을 입력해 주세요.");
		}

		if (findByPhoneNumber(editForm.getPhoneNumber()).isPresent() && !findByPhoneNumber(
			editForm.getPhoneNumber()).get().equals(account)) {
			return RsData.of("F-2", "사용할 수 없는 휴대전화번호입니다.<br>다른 휴대전화번호를 입력해 주세요.");
		}

		String password = passwordEncoder.encode(editForm.getPassword());

		Account editAccount = account.toBuilder()
			.password(password)
			.email(editForm.getEmail())
			.phoneNumber(editForm.getPhoneNumber())
			.build();

		accountRepository.save(editAccount);

		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(account.getUsername(), editForm.getPassword())
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return RsData.of("S-1", "회원 정보를 수정하였습니다.");
	}

	public boolean confirmPassword(Account account, String password) {
		return passwordEncoder.matches(password, account.getPassword());
	}

	@Transactional
	public RsData<Account> withdraw(Account account, HttpServletRequest request, HttpServletResponse response) {
		Optional<AptAccount> aptAccount = aptAccountRepository.findByAccount(account);
		if (aptAccount.isPresent()) {
			// 회원의 헬스장 이용권 상태가 이용 대기중일 시 탈퇴 불가
			List<GymMembership> gymMembershipList = gymMembershipRepository.findByUserAndStatus(aptAccount.get(), 0);
			if (gymMembershipList.size() != 0)
				return RsData.of("F-1", "죄송합니다.<br>아직 유효한 헬스장 이용권이 남아있어 계정 탈퇴를 진행할 수 없습니다.");

			Optional<AptAccountHouse> optionalAptAccountHouse = aptAccountHouseRepository.findByUser(aptAccount.get());
			if (optionalAptAccountHouse.isPresent()) {
				AptAccountHouse aptAccountHouse = optionalAptAccountHouse.get().toBuilder()
					.status(true)
					.build();
				aptAccountHouseRepository.save(aptAccountHouse);
			}

			// 계정 탈퇴 시 이벤트 발생 -> 이용권 삭제 처리를 위함
			// AptAccount가 있어야 Gym 이용이 가능하니, 없을땐 이벤트 발생x
			publisher.publishEvent(new EventAccountWithdraw(this, aptAccount.get()));
		}

		Optional<AptAccountHouse> optionalAptAccountHouse = aptAccountHouseRepository.findByUser(aptAccount.get());
		if (optionalAptAccountHouse.isPresent()) {
			String userName = optionalAptAccountHouse.get().getUser().getAccount().getFullName();
			vehicleRepository.deleteAllByName(userName); // 동일한 이름 차량정보 삭제
		}

		accountRepository.delete(account);

		// 데이터 삭제 후 로그아웃
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return RsData.of("S-1", "정상적으로 회원 탈퇴가 처리되었습니다.<br>그동안 저희 서비스를 이용해주셔서 감사합니다.");
	}
}