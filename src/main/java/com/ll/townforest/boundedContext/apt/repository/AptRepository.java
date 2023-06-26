package com.ll.townforest.boundedContext.apt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.apt.entity.Apt;

public interface AptRepository extends JpaRepository<Apt, Long> {
}
