package com.ll.townforest.boundedContext.apt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.apt.entity.Apt;

public interface AptRepository extends JpaRepository<Apt, Long> {
	Optional<Apt> findByName(String name);
}
