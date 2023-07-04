package com.ll.townforest.boundedContext.apt.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AptAccountDTO {
	@NotBlank
	String aptName;
	@NotBlank
	String relationship;
	@Min(0)
	Integer dong;
	@Min(0)
	Integer ho;
}
