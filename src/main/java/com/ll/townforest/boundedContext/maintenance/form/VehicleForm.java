package com.ll.townforest.boundedContext.maintenance.form;

import java.time.LocalDateTime;

import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleForm {
	private String vehicleNumber;
	@Size(min = 2, max = 17, message = "2자이상 17자이하로 입력해주세요.")
	private String name;
	private AptAccount user;
	private AptAccountHouse aptHouse;
	private LocalDateTime date = LocalDateTime.now();
}