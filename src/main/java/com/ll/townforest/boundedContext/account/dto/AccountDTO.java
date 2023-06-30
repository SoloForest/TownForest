package com.ll.townforest.boundedContext.account.dto;

import jakarta.validation.constraints.NotBlank;
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
	String email;
	@NotBlank
	@Size(min = 11, max = 16)
	String phoneNumber;
}
