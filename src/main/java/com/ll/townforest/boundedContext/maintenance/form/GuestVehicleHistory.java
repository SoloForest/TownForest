package com.ll.townforest.boundedContext.maintenance.form;

import com.ll.townforest.boundedContext.maintenance.entity.Vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestVehicleHistory extends Vehicle {
	private String address;

	public GuestVehicleHistory(Vehicle vehicle) {
		super(vehicle);
	}
}
