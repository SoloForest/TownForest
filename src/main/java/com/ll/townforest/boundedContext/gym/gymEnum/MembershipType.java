package com.ll.townforest.boundedContext.gym.gymEnum;

import lombok.Getter;

@Getter
public enum MembershipType {
	READY(0),
	STARTING(1),
	PAUSE(2),
	EXTENSION(3),
	RESTART(4);

	private int status;

	MembershipType(int status) {
		this.status = status;
	}
}
