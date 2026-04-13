package com.final_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "SOLVED_PROBLEM",
    // ⭐ 핵심: 같은 문제 중복 점수 적립 방지
    uniqueConstraints = @UniqueConstraint(
        name        = "uq_solved_member_problem",
        columnNames = {"member_id", "problem_id"}
    ),
    indexes = {
        @Index(name = "idx_solved_member",  columnList = "member_id"),
        @Index(name = "idx_solved_problem", columnList = "problem_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SolvedProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solved_problem_seq")
    @SequenceGenerator(
        name            = "solved_problem_seq",
        sequenceName    = "SOLVED_PROBLEM_SEQ",
        allocationSize  = 1
    )
    private Long id;

    // ✅ Member ID만 보유
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    // ✅ Problem ID만 보유
    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(nullable = false)
    private Integer finalAttemptCount;  // 최초 AC까지 걸린 총 시도 횟수

    @Column(nullable = false)
    private Integer earnedScore;        // 보정 계수 적용 후 최종 획득 점수

    @Column(nullable = false)
    private Boolean isFirstTry;         // 1회 시도 성공 여부

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime solvedAt;     // 최초 정답 시각

    // ── 생성자 (빌더 패턴) ──────────────────────────────────
    @Builder
    public SolvedProblem(Long memberId, Long problemId,
                         Integer finalAttemptCount, Integer earnedScore,
                         Boolean isFirstTry) {
        this.memberId          = memberId;
        this.problemId         = problemId;
        this.finalAttemptCount = finalAttemptCount;
        this.earnedScore       = earnedScore;
        this.isFirstTry        = isFirstTry;
    }

    // ── 점수 보정 계수 계산 (static 유틸) ───────────────────
    public static double getAttemptBonus(int attemptCount) {
        if (attemptCount == 1)              return 1.5;
        else if (attemptCount == 2)         return 1.2;
        else if (attemptCount == 3)         return 1.0;
        else if (attemptCount <= 5)         return 0.8;
        else                                return 0.5;
    }
}
