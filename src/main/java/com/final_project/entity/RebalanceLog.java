package com.final_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "REBALANCE_LOGS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RebalanceLog {

    @Id
    @UuidGenerator
    @Column(name = "ID", length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROBLEM_ID", nullable = false)
    private Problem problem;

    @Column(name = "BEFORE_DIFFICULTY", length = 10)
    private String beforeDifficulty;

    @Column(name = "AFTER_DIFFICULTY", length = 10)
    private String afterDifficulty;

    @Column(name = "STRATEGY_USED", length = 50)
    private String strategyUsed;

    @Column(name = "TRIGGER_REASON", columnDefinition = "CLOB")  // ✅ Oracle CLOB
    private String triggerReason;

    @Column(name = "AC_RATE_BEFORE")
    private Double acRateBefore;

    @CreationTimestamp
    @Column(name = "REBALANCED_AT", updatable = false)
    private LocalDateTime rebalancedAt;
}