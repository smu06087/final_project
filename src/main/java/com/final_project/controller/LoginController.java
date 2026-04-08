package com.final_project.controller;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication) {
        if (authentication == null) return "home";

        String nick = "";
        String image = "";
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            // 1. 소셜 로그인 사용자 처리
            OAuth2User oauth2User = (OAuth2User) principal;
            Map<String, Object> attr = oauth2User.getAttributes();

            if (attr.get("kakao_account") != null) {
                Map<String, Object> account = (Map<String, Object>) attr.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) account.get("profile");
                nick = (String) profile.get("nickname");
                image = (String) profile.get("profile_image_url");
            } else if (attr.get("name") != null) {
                nick = (String) attr.get("name");
                image = (String) attr.get("picture");
            } else if (attr.get("response") != null) {
                Map<String, Object> response = (Map<String, Object>) attr.get("response");
                nick = (String) response.get("name");
                image = (String) response.get("profile_image");
            } else if (attr.get("login") != null) {
                nick = (String) attr.get("name");
                image = (String) attr.get("avatar_url");
            }
        } else if (principal instanceof UserDetails) {
            // 2. 로컬 로그인 사용자 처리
            UserDetails userDetails = (UserDetails) principal;
            nick = userDetails.getUsername(); // 로컬은 아이디를 닉네임으로 활용
            image = "/images/default_profile.png"; // 기본 이미지 설정
        }

        model.addAttribute("nickname", nick);
        model.addAttribute("profileImage", image);

        return "home";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/login";

        String nick = "", image = "", email = "", registration = "local";
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2User) {
            // 소셜 로그인 처리
            OAuth2User oauth2User = (OAuth2User) principal;
            Map<String, Object> attr = oauth2User.getAttributes();
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
            registration = authToken.getAuthorizedClientRegistrationId();

            if ("kakao".equals(registration)) {
                Map<String, Object> account = (Map<String, Object>) attr.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) account.get("profile");
                nick = (String) profile.get("nickname");
                image = (String) profile.get("profile_image_url");
                email = (String) account.get("email");
            } else if ("google".equals(registration)) {
                nick = (String) attr.get("name");
                image = (String) attr.get("picture");
                email = (String) attr.get("email");
            } else if ("naver".equals(registration)) {
                Map<String, Object> response = (Map<String, Object>) attr.get("response");
                nick = (String) response.get("name");
                image = (String) response.get("profile_image");
                email = (String) response.get("email");
            } else if ("github".equals(registration)) {
                nick = (String) attr.get("login");
                image = (String) attr.get("avatar_url");
                email = (String) attr.get("email");
            }
        } else if (principal instanceof UserDetails) {
            // 로컬 로그인 처리 (UserDetails는 이메일 정보를 기본적으로 안 가질 수 있으므로 DB 재조회 권장)
            UserDetails userDetails = (UserDetails) principal;
            nick = userDetails.getUsername();
            image = "/images/default_profile.png";
            email = "로컬 계정은 이메일 정보가 없습니다."; // 필요시 DB에서 가져오기
        }

        model.addAttribute("nickname", nick);
        model.addAttribute("profileImage", image);
        model.addAttribute("email", email);
        model.addAttribute("registration", registration);

        return "mypage";
    }
}