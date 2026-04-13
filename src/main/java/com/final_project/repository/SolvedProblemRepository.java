package com.final_project.repository;

import com.final_project.entity.SolvedProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SolvedProblemRepository extends JpaRepository<SolvedProblem, Long> {

    // 이미 푼 문제인지 확인 (중복 점수 적립 방지 핵심)
    boolean existsByMemberIdAndProblemId(Long memberId, Long problemId);

    // 특정 유저의 모든 풀이 기록
    List<SolvedProblem> findByMemberIdOrderBySolvedAtDesc(Long memberId);

    // 특정 유저의 1회 성공 횟수
    int countByMemberIdAndIsFirstTryTrue(Long memberId);

    // 특정 유저가 푼 총 문제 수
    int countByMemberId(Long memberId);

    // 특정 유저 + 특정 문제 풀이 기록 조회
    Optional<SolvedProblem> findByMemberIdAndProblemId(Long memberId, Long problemId);
}
