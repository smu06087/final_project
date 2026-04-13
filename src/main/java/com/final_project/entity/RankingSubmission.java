package com.final_project.entity;

import com.final_project.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "RANKING_SUBMISSION",   // ✅ SUBMISSION → RANKING_SUBMISSION 으로 변경
    indexes = {
        @Index(name = "idx_rk_sub_member",  columnList = "member_id"),
        @Index(name = "idx_rk_sub_problem", columnList = "problem_id"),
        @Index(name = "idx_rk_sub_result",  columnList = "result")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RankingSubmission {   // ✅ 클래스명도 변경

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rk_submission_seq")
    @SequenceGenerator(
        name           = "rk_submission_seq",
        sequenceName   = "RANKING_SUBMISSION_SEQ",   // ✅ 시퀀스명도 변경
        allocationSize = 1
    )
    private Long id;

    // ✅ Member, Problem 모두 ID만 보유 (타팀 테이블 객체 참조 없음)
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SubmissionResult result;        // ACCEPTED / WRONG_ANSWER / ...

    @Column(length = 20)
    private String language;               // JAVA, PYTHON, CPP 등

    @Column(nullable = false)
    private Integer attemptNumber;         // 해당 문제에 대한 n번째 시도

    private Long executionTime;            // 실행 시간 (ms)

    private Long memoryUsed;               // 메모리 사용량 (KB)

    @Column(nullable = false)
    private Integer earnedScore;           // 이번 제출 획득 점수 (최초 AC만 적립, 나머지 0)

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @Builder
    public RankingSubmission(Long memberId, Long problemId,
                             SubmissionResult result, String language,
                             Integer attemptNumber, Long executionTime,
                             Long memoryUsed, Integer earnedScore) {
        this.memberId      = memberId;
        this.problemId     = problemId;
        this.result        = result;
        this.language      = language;
        this.attemptNumber = attemptNumber;
        this.executionTime = executionTime;
        this.memoryUsed    = memoryUsed;
        this.earnedScore   = earnedScore;
    }
}
