package com.ll.townforest.boundedContext.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.library.entity.LibraryHistory;

public interface LibraryHistoryRepository extends JpaRepository<LibraryHistory, Long> {
}
