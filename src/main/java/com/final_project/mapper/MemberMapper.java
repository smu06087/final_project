package com.final_project.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.final_project.dto.MemberDTO;

@Mapper
public interface MemberMapper {
	// 이미 가입한 소셜 계정인지 확인
    MemberDTO findByProviderId(String providerId);
    
    // 신규 소셜 회원 저장
    void insertMember(MemberDTO member);

	void updateMember(MemberDTO member);
}
