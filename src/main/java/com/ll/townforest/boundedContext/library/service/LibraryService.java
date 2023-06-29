package com.ll.townforest.boundedContext.library.service;

import org.springframework.stereotype.Service;

import com.ll.townforest.boundedContext.library.repository.LibraryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class LibraryService {
	private final LibraryRepository libraryRepository;
}
