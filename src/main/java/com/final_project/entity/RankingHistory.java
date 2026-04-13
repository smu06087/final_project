package com.final_project.entity;

import com.final_project.enums.Tier;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "RANKING_HISTORY",
    indexes = {
        @Index(name = "idx_ranking_history_member",   columnList = "member_id"),
        @Index(name = "idx_ranking_history_recorded", columnList = "recorded_at")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RankingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ranking_history_seq")
    @SequenceGenerator(
        name            = "ranking_history_seq",
        sequenceName    = "RANKING_HISTORY_SEQ",
        allocationSize  = 1
    )
    private Long id;

    // ✅ Member ID만 보유
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    private Integer previousRank;       // 이전 순위

    private Integer currentRank;        // 현재 순위

    private Integer previousScore;      // 이전 점수

    private Integer currentScore;       // 현재 점수

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Tier previousTier;          // 이전 티어

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Tier currentTier;           // 현재 티어

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;   // 스냅샷 시각 (매일 자정)

    // ── 빌더 ────────────────────────────────────────────────
    @Builder
    public RankingHistory(Long memberId,
                          Integer previousRank, Integer currentRank,
                          Integer previousScore, Integer currentScore,
                          Tier previousTier, Tier currentTier) {
        this.memberId      = memberId;
        this.previousRank  = previousRank;
        this.currentRank   = currentRank;
        this.previousScore = previousScore;
        this.currentScore  = currentScore;
        this.previousTier  = previousTier;
        this.currentTier   = currentTier;
    }
}