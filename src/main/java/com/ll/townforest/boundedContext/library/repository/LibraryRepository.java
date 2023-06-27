package com.ll.townforest.boundedContext.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.library.entity.Library;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}
