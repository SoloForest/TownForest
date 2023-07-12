package com.ll.townforest.boundedContext.gym.eventListener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.event.EventAccountWithdraw;
import com.ll.townforest.boundedContext.gym.service.GymService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class GymEventListener {
	private final GymService gymService;

	@EventListener
	public void listen(EventAccountWithdraw event) {
		gymService.whenAccountWithdraw(event.getAptAccount());
	}
}
