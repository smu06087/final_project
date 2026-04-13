package com.final_project.repository;

import com.final_project.entity.RankingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RankingHistoryRepository extends JpaRepository<RankingHistory, Long> {

    // 특정 유저의 랭킹 변동 이력 (최신순)
    List<RankingHistory> findByMemberIdOrderByRecordedAtDesc(Long memberId);

    // 특정 유저의 최근 N개 이력 (그래프용)
    List<RankingHistory> findTop30ByMemberIdOrderByRecordedAtDesc(Long memberId);

    // 특정 기간 이력 조회
    List<RankingHistory> findByMemberIdAndRecordedAtBetween(
        Long memberId, LocalDateTime from, LocalDateTime to
    );
}