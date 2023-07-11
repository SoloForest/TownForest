package com.ll.townforest.boundedContext.notice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.townforest.base.rq.Rq;
import com.ll.townforest.base.rsData.RsData;
import com.ll.townforest.boundedContext.apt.entity.Apt;
import com.ll.townforest.boundedContext.apt.entity.AptAccount;
import com.ll.townforest.boundedContext.notice.entity.Notice;
import com.ll.townforest.boundedContext.notice.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
	private final NoticeRepository noticeRepository;

	private final Rq rq;

	public Page<Notice> getNotices(int page, Apt apt) {
		Pageable pageable = PageRequest.of(page, 5);
		return noticeRepository.findAllByAptIdOrderByIdDesc(apt.getId(), pageable);
	}

	public Notice getNoticeById(Long id) {
		return noticeRepository.findById(id).get();
	}

	@Transactional
	public RsData create(AptAccount writer, String content, String title) {

		Apt apt = writer.getApt();

		if (apt == null) {
			return RsData.of("F-1", "관리중인 아파트가 없어 등록할 수 없습니다.");
		}

		Notice notice = Notice.builder()
			.apt(apt)
			.writer(writer)
			.content(content)
			.title(title)
			.build();

		noticeRepository.save(notice);

		return RsData.of("S-1", "등록 성공");
	}

	@Transactional
	public RsData modify(Notice notice, String title, String content) {

		Notice tmp = notice.toBuilder()
			.content(content)
			.title(title)
			.build();

		noticeRepository.save(tmp);

		return RsData.of("S-1", "수정 성공");
	}

	@Transactional
	public RsData delete(Notice notice) {
		noticeRepository.delete(notice);

		return RsData.of("S-1", "공지글 삭제 성공");
	}

	public List<Notice> getNoticeTop5LatestByUser_AptId(AptAccount user) {
		Long aptId = user.getApt().getId();
		return noticeRepository.findTop5ByAptIdOrderByCreateDateDesc(aptId);
	}
}
