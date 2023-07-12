package com.ll.townforest.base.event;

import org.springframework.context.ApplicationEvent;

import com.ll.townforest.boundedContext.account.entity.Account;

import lombok.Getter;

@Getter
public class EventAccountWithdraw extends ApplicationEvent {
	private final Account account;

	public EventAccountWithdraw(Object source, Account account) {
		super(source);
		this.account = account;
	}
}
