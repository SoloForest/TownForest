package com.ll.townforest.boundedContext.gym.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ll.townforest.boundedContext.gym.entity.GymHistory;

public interface GymHistoryRepository extends JpaRepository<GymHistory, Long> {
	Page<GymHistory> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

	Page<GymHistory> findAllByGymId(Long id, Pageable pageable);

	@Query("select "
		+ "distinct q "
		+ "from GymHistory q "
		+ "left outer join AptAccount u1 on q.user=u1 "
		+ "left outer join Account a on u1.account=a "
		+ "left outer join Gym g on q.gym=g "
		+ "where "
		+ "a.phoneNumber like %:kw% ")
	Page<GymHistory> findAllByPhoneNumber(@Param("kw") String kw, Pageable pageable);

	@Query("select "
		+ "distinct q "
		+ "from GymHistory q "
		+ "left outer join AptAccount u1 on q.user=u1 "
		+ "left outer join Account a on u1.account=a "
		+ "left outer join Gym g on q.gym=g "
		+ "where "
		+ "a.fullName like %:kw% ")
	Page<GymHistory> findAllByFullName(@Param("kw") String kw, Pageable pageable);

	// 관리자 화면에서 인원수를 나타내기 위한 리스트
	List<GymHistory> findByGymId(Long gymId);
}
