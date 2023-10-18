package com.pathfinder.server.diary.repository;

import com.pathfinder.server.diary.entity.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Page<Diary> findByArea1(String area1, Pageable pageable);
    Page<Diary> findByMemberMemberId(Long memberId, Pageable pageable);

    @Query("SELECT d FROM Diary d WHERE d.createdAt = :requestDate ORDER BY d.recommendedCount DESC")
    Page<Diary> findByTop3ByOrderedByRecommendedCountForDay(@Param("requestDate") Date requestDate, Pageable pageable);

    @Query("SELECT d FROM Diary d WHERE YEARWEEK(d.createdAt, 1) = :requestWeek ORDER BY d.recommendedCount DESC")
    Page<Diary> findByTop3ByOrderedByRecommendedCountForWeek(@Param("requestWeek") int requestWeek, Pageable pageable);

    @Query("SELECT d FROM Diary d WHERE YEAR(d.createdAt) = :requestYear AND MONTH(d.createdAt) = :requestMonth ORDER BY d.recommendedCount DESC")
    Page<Diary> findByTop3ByOrderedByRecommendedCountForMonth(@Param("requestYear") int requestYear, @Param("requestMonth") int requestMonth, Pageable pageable);

    @Query("SELECT d FROM Diary d WHERE YEAR(d.createdAt) = :requestYear ORDER BY d.recommendedCount DESC")
    Page<Diary> findByTop3ByOrderedByRecommendedCountForYear(@Param("requestYear") int requestYear, Pageable pageable);

}
