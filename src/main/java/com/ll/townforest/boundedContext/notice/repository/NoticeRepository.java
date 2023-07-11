package com.ll.townforest.boundedContext.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.townforest.boundedContext.notice.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	Page<Notice> findAllByAptIdOrderByIdDesc(Long id, Pageable pageable);
}