package com.ll.townforest.boundedContext.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {
	@NotBlank
	@Size(min = 5, max = 20)
	String username;
	@NotBlank
	@Size(min = 8, max = 15)
	String password;
	@NotBlank
	@Size(min = 2, max = 10)
	String fullName;
	@NotBlank
	@Pattern(regexp = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$", message = "이메일 형식이 아닙니다.")
	String email;
	@NotBlank
	@Size(min = 10, max = 11)
	String phoneNumber;
}
