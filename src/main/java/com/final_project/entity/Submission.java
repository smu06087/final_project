package com.final_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "SUBMISSIONS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @UuidGenerator
    @Column(name = "ID", length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROBLEM_ID", nullable = false)
    private Problem problem;

    @Column(name = "USER_ID", length = 36)
    private String userId;

    @Column(name = "LANGUAGE", length = 20)
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(name = "VERDICT", length = 20)
    private Verdict verdict;

    @Column(name = "RUNTIME_MS")
    private Integer runtimeMs;

    @Column(name = "MEMORY_MB")
    private Double memoryMb;

    @Column(name = "CODE", columnDefinition = "CLOB")   // ✅ 제출 코드 저장
    private String code;

    @CreationTimestamp
    @Column(name = "SUBMITTED_AT", updatable = false)
    private LocalDateTime submittedAt;

    // ─── 채점 결과 열거형 ──────────────────────────────
    public enum Verdict {
        ACCEPTED,           // 정답
        WRONG_ANSWER,       // 오답
        TIME_LIMIT,         // 시간 초과
        MEMORY_LIMIT,       // 메모리 초과
        RUNTIME_ERROR,      // 런타임 에러
        COMPILE_ERROR,      // 컴파일 에러
        PENDING             // 채점 대기
    }
}
