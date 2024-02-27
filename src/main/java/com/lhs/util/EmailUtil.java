package com.lhs.util;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.lhs.dto.EmailDto;

@Component // 스프링이 빈으로 등록
public class EmailUtil {
	
	@Autowired
    private JavaMailSender mailSender;

    public EmailUtil(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
    
	public String sendMail(EmailDto email) {
        try {
        	
        	System.out.println("mailSender : : : "+ mailSender);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setTo(email.getReceiver());
            messageHelper.setText(email.getText());
            messageHelper.setFrom(email.getFrom());
            messageHelper.setSubject(email.getSubject());	// 메일제목은 생략이 가능하다

            mailSender.send(message);

        } catch(Exception e){
            System.out.println(e);
            return "Error";
        }
        return "Sucess";
    }
	
//  public String sendHtmlMail(EmailDto email) {
//  try {
//      MimeMessage message = mailSender.createMimeMessage();
//      MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
//      messageHelper.setTo(email.getReceiver());
//      // HTML 형식으로 본문 설정
//      messageHelper.setText(email.getText(), true);
//      messageHelper.setFrom(email.getFrom());
//      messageHelper.setSubject(email.getSubject());
//
//      mailSender.send(message);
//
//  } catch(Exception e){
//      System.out.println(e);
//      return "Error";
//  }
//  return "Success";
//}
    
}
