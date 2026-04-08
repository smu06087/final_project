package com.final_project.dto;

import lombok.Data;
import java.util.Date;

@Data
public class MemberDTO {
    private int mno;
    private String providerId;    // 소셜 고유 ID
    private String nickname;
    private String email;
    private String profileImage;
    private String registrationId; // kakao, google 등
    private String role;
    private Date createDate;
    private String password;
}