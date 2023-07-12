package com.ll.townforest.base.event;

import org.springframework.context.ApplicationEvent;

import com.ll.townforest.boundedContext.apt.entity.AptAccount;

import lombok.Getter;

@Getter
public class EventAccountWithdraw extends ApplicationEvent {
	private final AptAccount aptAccount;

	public EventAccountWithdraw(Object source, AptAccount aptAccount) {
		super(source);
		this.aptAccount = aptAccount;
	}
}
