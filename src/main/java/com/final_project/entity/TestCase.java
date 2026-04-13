package com.final_project.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "TEST_CASES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCase {

    @Id
    @UuidGenerator
    @Column(name = "ID", length = 36, nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROBLEM_ID", nullable = false)
    private Problem problem;

    @Column(name = "INPUT", columnDefinition = "CLOB", nullable = false)    // ✅ Oracle CLOB
    private String input;

    @Column(name = "EXPECTED", columnDefinition = "CLOB", nullable = false) // ✅ Oracle CLOB
    private String expected;

    @Column(name = "IS_SAMPLE", nullable = false)
    @Builder.Default
    private Boolean isSample = false;   // ✅ Oracle NUMBER(1) 자동 매핑
}
