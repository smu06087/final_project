package com.final_project.entity;

import jakarta.persistence.*;  
import lombok.*;  
import org.hibernate.annotations.CreationTimestamp;  
import org.hibernate.annotations.UpdateTimestamp;  
import org.hibernate.annotations.UuidGenerator;  

import java.time.LocalDateTime;  
import java.util.ArrayList;  
import java.util.List;  

@Entity
@Table(name = "PROBLEMS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {
	
	 @Id  
	    @UuidGenerator                          // ✅ Oracle: Hibernate 6 UUID 자동 생성  
	    @Column(name = "ID", length = 36, nullable = false, updatable = false)  
	    private String id;  

	    @Column(name = "TITLE", length = 255, nullable = false)  
	    private String title;  

	    @Column(name = "DESCRIPTION", columnDefinition = "CLOB")   // ✅ Oracle: TEXT → CLOB  
	    private String description;  

	    @Enumerated(EnumType.STRING)  
	    @Column(name = "DIFFICULTY", length = 10)  
	    private Difficulty difficulty;  

	    @Column(name = "QUALITY_SCORE")  
	    private Double qualityScore;  

	    @Enumerated(EnumType.STRING)  
	    @Column(name = "STATUS", length = 20)  
	    @Builder.Default  
	    private ProblemStatus status = ProblemStatus.PENDING;  

	    @Column(name = "TIME_LIMIT_MS")  
	    @Builder.Default  
	    private Integer timeLimitMs = 2000;  

	    @Column(name = "MEMORY_LIMIT_MB")  
	    @Builder.Default  
	    private Integer memoryLimitMb = 256;  

	    // ✅ Oracle 배열 미지원 → 별도 테이블로 분리  
	    @ElementCollection(fetch = FetchType.LAZY)  
	    @CollectionTable(  
	        name = "PROBLEM_TAGS",  
	        joinColumns = @JoinColumn(name = "PROBLEM_ID")  
	    )  
	    @Column(name = "TAG", length = 50)  
	    @Builder.Default  
	    private List<String> algorithmTags = new ArrayList<>();  

	    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)  
	    @Builder.Default  
	    private List<TestCase> testCases = new ArrayList<>();  

	    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)  
	    @Builder.Default  
	    private List<Submission> submissions = new ArrayList<>();  

	    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)  
	    @Builder.Default  
	    private List<RebalanceLog> rebalanceLogs = new ArrayList<>();  

	    @CreationTimestamp  
	    @Column(name = "CREATED_AT", updatable = false)  
	    private LocalDateTime createdAt;  

	    @UpdateTimestamp  
	    @Column(name = "UPDATED_AT")  
	    private LocalDateTime updatedAt;  

	    // ─── 열거형 ───────────────────────────────────────  
	    public enum Difficulty {  
	        Easy, Medium, Hard  
	    }  

	    public enum ProblemStatus {  
	        PENDING,        // 생성됨, 검증 대기  
	        VALIDATING,     // 검증 중  
	        PUBLISHED,      // 배포됨  
	        REBALANCING,    // 리밸런싱 중  
	        DEPRECATED      // 폐기됨  
	    }

}
