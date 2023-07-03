package com.ll.townforest.boundedContext.maintenance.form;

import com.ll.townforest.boundedContext.apt.entity.AptAccount;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleForm {
	private String vehicleNumber;
	@Size(min = 2, max = 17)
	private String name;
	private AptAccount user;
}
