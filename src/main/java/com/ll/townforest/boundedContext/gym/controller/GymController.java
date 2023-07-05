package com.ll.townforest.boundedContext.gym.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.gym.entity.GymHistory;
import com.ll.townforest.boundedContext.gym.entity.GymMembership;
import com.ll.townforest.boundedContext.gym.entity.GymTicket;
import com.ll.townforest.boundedContext.gym.service.GymService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gym")
public class GymController {
	@Value("${custom.tossSecretKey.key}")
	private String secretKey;

	private final GymService gymService;

	private final Rq rq;

	@GetMapping("")
	@PreAuthorize("isAuthenticated()")
	public String gymMain(Model model) {

		AptAccount user = rq.getAptAccount();

		if (user == null || !user.isStatus())
			rq.historyBack("승인된 아파트 주민만 이용할 수 있습니다.");

		model.addAttribute("user", user);

		// 이용중인 이용권 정보
		GymMembership gymMembership = gymService.getMembership(user);
		model.addAttribute("gymMembership", gymMembership);

		if (gymMembership != null) {
			// 시작일 전 일 경우, 시작이 며칠 남았는지
			long beforeDays = ChronoUnit.DAYS.between(LocalDate.now(), gymMembership.getStartDate());
			model.addAttribute("beforeDays", beforeDays);
			// 이용권 총 며칠 남았는지
			long afterDays = ChronoUnit.DAYS.between(LocalDate.now(), gymMembership.getEndDate());
			model.addAttribute("afterDays", afterDays);

		}

		return "gym/gym";
	}

	@GetMapping("/locker")
	public String locker() {
		return "gym/locker";
	}

	@GetMapping("/pause")
	public String pause() {
		return "gym/pause";
	}

	@GetMapping("/refund")
	public String refund() {
		return "gym/refund";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/register")
	public String register(Model model) {

		AptAccount user = rq.getAptAccount();

		if (user == null || !user.isStatus())
			rq.historyBack("승인된 아파트 주민만 이용할 수 있습니다.");

		model.addAttribute("user", user);

		// 연장용 일 경우 startDate를 현제 가지고 있는 이용권의 종료일+1로 지정하기 위해 필요
		GymMembership membership = gymService.getMembership(user);
		model.addAttribute("membership", membership);

		// 아파트 여러개라면 현재 로그인한 사용자가 속한 Gym ID를 넘긴다.
		// 현재 하나이기에 1로 하드코딩
		List<GymTicket> gymTicketList = gymService.getGymTickets(1L);

		model.addAttribute("gymTicketList", gymTicketList);

		return "gym/register";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/register/success")
	public String paymentResult(
		Model model,
		@RequestParam(value = "orderId") String orderId,
		@RequestParam(value = "amount") Integer amount,
		@RequestParam(value = "paymentKey") String paymentKey)
		throws Exception {

		AptAccount user = rq.getAptAccount();

		if (user == null || !user.isStatus())
			rq.historyBack("승인된 아파트 주민만 이용할 수 있습니다.");

		// "orderId":"gym-type-2-36600264572790997_2023-07-12" 이런 형태, -type- 다음 숫자가 이용권 종류
		String[] parts = orderId.split("-type-");
		String[] subParts = parts[1].split("-");
		String ticketNumberStr = subParts[0];
		int ticketType = Integer.parseInt(ticketNumberStr);

		// "orderId":"gym-type-2-36600264572790997_2023-07-12" 이런 형태, 마지막 부분이 시작일자를 나타냄
		String[] parts2 = orderId.split("_");
		String dateString = parts2[1];
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = LocalDate.parse(dateString, formatter);

		// 실제 이용권 가격
		GymTicket gymTicket = gymService.getTicket(ticketType);

		if (orderId.startsWith("gym-") && !amount.equals(gymTicket.getPrice())) {
			throw new RuntimeException("해킹의심 : 실제 이용권 금액 %d원이 아닙니다.".formatted(gymTicket.getPrice()));
		}

		// yml 파일에 : 문자열 추가 불가로 여기서 추가
		StringBuilder sb = new StringBuilder(secretKey);
		sb.append(":");
		secretKey = sb.toString();

		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));
		String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

		// URL에 물어봄
		URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestProperty("Authorization", authorizations);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		JSONObject obj = new JSONObject();
		obj.put("orderId", orderId);
		obj.put("amount", amount);

		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(obj.toString().getBytes("UTF-8"));

		int code = connection.getResponseCode();
		boolean isSuccess = code == 200 ? true : false;
		model.addAttribute("isSuccess", isSuccess);

		InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

		Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject)parser.parse(reader);
		responseStream.close();
		model.addAttribute("responseStr", jsonObject.toJSONString());
		System.out.println(jsonObject.toJSONString());

		// 뷰에 보여줄 내용들을 위함
		model.addAttribute("gymTicket", gymTicket);
		model.addAttribute("startDate", startDate);
		LocalDate endDate = gymService.getEndDate(gymTicket, startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("amount", amount);

		if (((String)jsonObject.get("method")) == null) {
			model.addAttribute("code", (String)jsonObject.get("code"));
			model.addAttribute("message", (String)jsonObject.get("message"));
		}

		// TODO : 결제방식 추가 시 수정 필요 / method를 받아서 넣어주기
		if (isSuccess) {
			GymMembership membership = gymService.getMembership(user);

			if (membership == null)
				gymService.create(user, startDate, ticketType, "카드");
			else
				gymService.update(user, startDate, endDate, ticketType, "카드");
		}

		return "gym/success";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/register/fail")
	public String paymentResult(
		Model model,
		@RequestParam(value = "message") String message,
		@RequestParam(value = "code") Integer code
	) throws Exception {
		model.addAttribute("code", code);
		model.addAttribute("message", message);
		return "gym/fail";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/history")
	public String history(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

		AptAccount user = rq.getAptAccount();

		if (user == null || !user.isStatus())
			rq.historyBack("승인된 아파트 주민만 이용할 수 있습니다.");

		model.addAttribute("user", user);

		Page<GymHistory> gymHistories = gymService.getPersonalHistories(page, user.getId());

		model.addAttribute("paging", gymHistories);

		return "gym/history";
	}
}
