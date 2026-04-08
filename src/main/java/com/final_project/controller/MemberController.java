package com.final_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.final_project.dto.MemberDTO;
import com.final_project.mapper.MemberMapper;
import com.final_project.service.MailService;

@Controller
public class MemberController {  //김동욱 수정 되나요? 조예진 테스트 신동엽 테스트

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- 기존 회원가입 로직 ---
    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }

    @PostMapping("/join")
    public String join(MemberDTO member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        // 기본값 세팅 (필요시)
        member.setRegistrationId("local");
        member.setRole("ROLE_USER");
        
        memberMapper.insertMember(member);
        return "redirect:/login";
    }

    // --- 신규 정보 수정 로직 ---

 // 1. 정보 수정 페이지 이동
    @GetMapping("/member/edit")
    public String editForm(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";

        String username = "";
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            // 로컬 로그인 유저
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            // 소셜 로그인 유저 (이름이나 ID 필드는 서비스마다 다를 수 있으나 보통 getName() 사용)
            // 만약 구글/카카오 아이디가 provider_id와 매핑되어 있다면 그 값을 가져와야 함
            OAuth2User oauth2User = (OAuth2User) principal;
            
            // 소셜 유저의 경우 고유 식별값(ID)을 찾아야 합니다.
            // 이전에 LoginController에서 했던 방식처럼 처리하거나 간단하게 getName() 사용
            username = oauth2User.getName(); 
        }

        // DB에서 최신 정보 조회
        MemberDTO member = memberMapper.findByProviderId(username);
        model.addAttribute("member", member);
        
        return "member/edit";
    }

    // 2. 정보 수정 처리
    @PostMapping("/member/edit")
    public String update(MemberDTO member, Authentication authentication) {
        String username = "";
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            username = ((OAuth2User) principal).getName();
        }
        
        member.setProviderId(username);

        // 비밀번호 수정 로직 (소셜 유저는 비번이 없으므로 로컬 유저일 때만 주로 작동)
        if (member.getPassword() != null && !member.getPassword().trim().isEmpty()) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        } else {
            MemberDTO origin = memberMapper.findByProviderId(username);
            member.setPassword(origin.getPassword());
        }

        memberMapper.updateMember(member);
        
        return "redirect:/home";
    }
    
    // 회원 탈퇴 
    @PostMapping("/member/delete")
    public String delete(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // memberMapper.deleteMember(userDetails.getUsername()); // 삭제 쿼리 호출
        return "redirect:/logout"; // 탈퇴 후 로그아웃 처리
    }
    
    @Autowired
    private MailService mailService; // 위에서 만든 서비스 주입
    // 비밀번호 찾기 페이지 이동
    
    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "member/forgot-password";
    }
    
    // 비밀번호 재설정 실행
    @PostMapping("/reset-password")
    // @RequestParam 뒤에 ("이름")을 명시적으로 붙여줍니다.
    public String resetPassword(@RequestParam("providerId") String providerId, 
                                @RequestParam("email") String email) {
        
        // 1. DB에서 유저 조회
        MemberDTO member = memberMapper.findByProviderId(providerId);
        
        // 2. 유저가 존재하고 이메일이 일치하는지 확인
        if (member != null && member.getEmail().equals(email)) {
            // 3. 임시 비밀번호 생성 (8자리 랜덤문자열)
            String tempPw = java.util.UUID.randomUUID().toString().substring(0, 8);
            
            // 4. DB에 암호화해서 저장 (비밀번호 업데이트 쿼리 필요)
            member.setPassword(passwordEncoder.encode(tempPw));
            memberMapper.updateMember(member); // 기존 updateMember 재사용
            
            // 5. 실제 메일 발송
            String loginUrl = "http://localhost:8080/login";
            String mailContent = "<div style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                               + "<h3>[Minion] 임시 비밀번호 안내</h3>"
                               + "<p>안녕하세요. 요청하신 임시 비밀번호는 <b>[" + tempPw + "]</b> 입니다.</p>"
                               + "<p>보안을 위해 로그인 후 반드시 비밀번호를 변경해 주세요.</p>"
                               + "<br>"
                               + "<a href='" + loginUrl + "' "
                               + "style='background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;'>"
                               + "로그인하러 가기</a>"
                               + "</div>";

            mailService.sendEmail(email, "[Minion] 임시 비밀번호 안내", mailContent);
            
            return "redirect:/login?mailSent=true";
        }
        
        return "redirect:/forgot-password?error=not_found";
    }
}