package com.final_project.repository;

import com.final_project.entity.RankingSubmission;
import com.final_project.enums.SubmissionResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankingSubmissionRepository extends JpaRepository<RankingSubmission, Long> {

    // 특정 유저의 특정 문제 제출 횟수 (attemptNumber 계산용)
    int countByMemberIdAndProblemId(Long memberId, Long problemId);

    // 특정 유저의 모든 제출 이력 (최신순)
    List<RankingSubmission> findByMemberIdOrderBySubmittedAtDesc(Long memberId);

    // 특정 유저의 특정 문제 제출 이력 (시도 순서 확인용)
    List<RankingSubmission> findByMemberIdAndProblemIdOrderBySubmittedAtAsc(Long memberId, Long problemId);

    // 특정 유저가 특정 문제를 ACCEPTED한 적 있는지 확인
    boolean existsByMemberIdAndProblemIdAndResult(Long memberId, Long problemId, SubmissionResult result);

    // 특정 문제의 전체 제출 수 (리밸런싱용)
    long countByProblemId(Long problemId);

    // 특정 문제의 ACCEPTED 수 (정답률 계산용)
    long countByProblemIdAndResult(Long problemId, SubmissionResult result);
}