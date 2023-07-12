package com.ll.townforest.boundedContext.home.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketForm {
	@NotNull(message = "공백일 수 없습니다.")
	private Integer days;
	@NotBlank(message = "공백일 수 없습니다.")
	private String name;
	@NotNull
	@Min(1)
	private Integer price;
	private String content;
}
