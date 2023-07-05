package com.ll.townforest.boundedContext.maintenance.form;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestVehicleDTO {
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime selectedDate;
	@Size(max = 10)
	@NotBlank
	private String vehicleNumber;
	@NotBlank
	private String name;
}
