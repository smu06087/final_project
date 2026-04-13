package com.final_project.repository;

import com.final_project.entity.UserRanking;
import com.final_project.enums.Tier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRankingRepository extends JpaRepository<UserRanking, Long> {

    // 특정 유저 랭킹 조회
    Optional<UserRanking> findByMemberId(Long memberId);

    // 전체 랭킹 (점수 내림차순) — 페이징
    Page<UserRanking> findAllByOrderByTotalScoreDesc(Pageable pageable);

    // 특정 티어 유저 목록
    List<UserRanking> findByTierOrderByTotalScoreDesc(Tier tier);

    // 특정 점수보다 높은 유저 수 (내 순위 계산)
    long countByTotalScoreGreaterThan(int score);

    // 전체 순위 업데이트용 — totalScore 높은 순 전체 조회
    @Query("SELECT ur FROM UserRanking ur ORDER BY ur.totalScore DESC")
    List<UserRanking> findAllOrderByTotalScoreDesc();
}