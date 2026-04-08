package com.final_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.final_project.dto.MemberDTO;
import com.final_project.mapper.MemberMapper;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. DB에서 해당 아이디로 사용자 조회
        MemberDTO member = memberMapper.findByProviderId(username);
        
        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다: " + username);
        }

        // 2. Role 데이터 검증 (Null 방어)
        String role = member.getRole();
        if (role == null || role.isEmpty()) {
            role = "ROLE_USER"; // DB에 권한이 없으면 기본 권한 부여
        }

        // 3. Spring Security의 User 객체 생성 및 반환
        return User.builder()
                .username(member.getProviderId())
                .password(member.getPassword()) // 암호화된 비밀번호
                .roles(role.replace("ROLE_", "")) // "ROLE_USER" -> "USER"
                .build();
    }
}