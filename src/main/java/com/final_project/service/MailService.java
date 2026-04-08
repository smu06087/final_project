package com.final_project.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        
        try {
            // true는 멀티파트 메시지(HTML + 첨부파일 등)를 사용하겠다는 의미입니다.
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            // 두 번째 인자인 true가 "이 텍스트는 HTML이다"라고 알려주는 핵심 설정입니다.
            helper.setText(htmlContent, true); 
            // setFrom은 yml 설정과 일치해야 하므로, 아예 지우거나 정확히 입력하세요.
            // helper.setFrom("smu06087@gmail.com"); 

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("메일 발송 실패: " + e.getMessage());
        }
    }
}