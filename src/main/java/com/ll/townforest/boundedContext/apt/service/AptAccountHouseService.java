package com.ll.townforest.boundedContext.apt.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.boundedContext.apt.entity.AptAccountHouse;
import com.ll.townforest.boundedContext.apt.repository.AptAccountHouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AptAccountHouseService {
	private final AptAccountHouseRepository aptAccountHouseRepository;

	public List<AptAccountHouse> findAptAccountHouse(int sortCode) {

		return switch (sortCode) {
			case 2 -> aptAccountHouseRepository.findByUser_StatusTrue();
			case 3 -> aptAccountHouseRepository.findByUser_StatusFalse();
			default -> aptAccountHouseRepository.findAllDesc();
		};
	}
}
