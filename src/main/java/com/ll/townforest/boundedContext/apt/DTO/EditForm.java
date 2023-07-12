package com.ll.townforest.boundedContext.apt.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditForm {
	@NotBlank
	@Size(min = 8, max = 15)
	String password;
	@NotBlank
	@Pattern(regexp = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$", message = "이메일 형식이 아닙니다.")
	String email;
	@NotBlank
	@Size(min = 10, max = 11)
	String phoneNumber;
}
