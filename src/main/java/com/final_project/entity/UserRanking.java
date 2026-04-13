package com.final_project.entity;

import com.final_project.enums.Tier;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "USER_RANKING",
    indexes = {
        @Index(name = "idx_user_ranking_score", columnList = "total_score DESC"),
        @Index(name = "idx_user_ranking_tier",  columnList = "tier")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UserRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_ranking_seq")
    @SequenceGenerator(
        name            = "user_ranking_seq",
        sequenceName    = "USER_RANKING_SEQ",
        allocationSize  = 1
    )
    private Long id;

    // ✅ Member 1:1 — ID만 보유, UNIQUE 보장
    @Column(name = "member_id", nullable = false, unique = true)
    private Long memberId;

    @Column(nullable = false)
    private Integer totalScore;             // 누적 총점

    @Column(nullable = false)
    private Integer solvedEasy;             // 푼 Easy 문제 수

    @Column(nullable = false)
    private Integer solvedMedium;           // 푼 Medium 문제 수

    @Column(nullable = false)
    private Integer solvedHard;             // 푼 Hard 문제 수

    @Column(nullable = false)
    private Integer solvedTotal;            // 전체 푼 문제 수

    @Column(nullable = false)
    private Integer firstTrySolvedCount;    // 1회 시도 성공 횟수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Tier tier;                      // BRONZE ~ RUBY

    private Integer globalRank;             // 전체 순위 (배치 집계)

    private LocalDateTime lastUpdatedAt;    // 마지막 갱신 시각

    // ── 신규 유저 기본값으로 생성 ───────────────────────────
    public static UserRanking createNew(Long memberId) {
        UserRanking ur = new UserRanking();
        ur.memberId             = memberId;
        ur.totalScore           = 0;
        ur.solvedEasy           = 0;
        ur.solvedMedium         = 0;
        ur.solvedHard           = 0;
        ur.solvedTotal          = 0;
        ur.firstTrySolvedCount  = 0;
        ur.tier                 = Tier.BRONZE;
        ur.globalRank           = null;
        ur.lastUpdatedAt        = LocalDateTime.now();
        return ur;
    }

    // ── 점수 적립 + 티어 자동 갱신 ─────────────────────────
    public void addScore(int score, String difficulty, boolean isFirstTry) {
        this.totalScore += score;
        this.solvedTotal++;

        switch (difficulty.toUpperCase()) {
            case "EASY"   -> this.solvedEasy++;
            case "MEDIUM" -> this.solvedMedium++;
            case "HARD"   -> this.solvedHard++;
        }

        if (isFirstTry) this.firstTrySolvedCount++;

        this.tier            = Tier.fromScore(this.totalScore); // 티어 자동 재계산
        this.lastUpdatedAt   = LocalDateTime.now();
    }

    // ── 전체 순위 갱신 (배치 전용) ──────────────────────────
    public void updateGlobalRank(int rank) {
        this.globalRank    = rank;
        this.lastUpdatedAt = LocalDateTime.now();
    }
}
