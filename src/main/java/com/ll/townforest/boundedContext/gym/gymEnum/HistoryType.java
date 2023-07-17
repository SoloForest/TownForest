package com.ll.townforest.boundedContext.gym.gymEnum;

import lombok.Getter;

@Getter
public enum HistoryType {
	PAYMENT(0),
	EXTENSION(1),
	PAUSE(2),
	RESTART(3),
	EXPIRATION(4);

	private int status;

	HistoryType(int status) {
		this.status = status;
	}
}
