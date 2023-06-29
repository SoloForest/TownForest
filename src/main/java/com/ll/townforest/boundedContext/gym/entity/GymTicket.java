package com.ll.townforest.boundedContext.gym.entity;

import com.ll.townforest.boundedContext.apt.entity.Apt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GymTicket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/* 이용권 종료를 나타내는 변수
	1 : 1일권
	2 : 30일권
	3 : 60일권
	4 : 90일권
	 */
	@NotNull
	private Integer type;

	@NotBlank
	private String name;

	/* 이용권 금액을 나타내는 변수
	추후 관리자가 할인이벤트를 한다거나, 아니면 이용권을 추가하거나 할 때 사용
	 */
	@NotNull
	private Integer price;

	/* 할인가 정보 */
	private String content;

	@ManyToOne
	private Apt apt;
	@ManyToOne
	private Gym gym;

}
