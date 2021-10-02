package com.alpha.SpringBootEmailTutorial.service;

 

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.alpha.SpringBootEmailTutorial.AttachmentReceiver.EmailAttachmentReceiver;
import com.alpha.SpringBootEmailTutorial.model.Attachment;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	

	
	@Value("${spring.mail.username}")
	private String from;
	
	@Value("${spring.signature}")
	private String signature;
	
	@Value("${spring.companyid}")
	private String company;

	@Value("${spring.location}")
	private String location;
	
	private String baseUrl;
	
	@Value("${spring.mail.properties.mail.transport.protocol}")
	private String protocol;
	@Value("${spring.mail.host}")
	private String host ;
	@Value("${spring.mail.port}")
	private String port ;

    // your email id 
	@Value("${spring.mail.username}")
	private String username ;//Put here Gmail Username without @ sign
    //Your Email Password For Account
	@Value("${spring.mail.password}")
	private String password ;
	
	@Value("${filepath}")
	private String filepath;
	

	Logger logger=LoggerFactory.getLogger(NotificationServiceImpl.class);

	@Override
	public void sendNotificationWithTemplate(String to, String subject, String body,List<Attachment> attachments) {
		// TODO Auto-generated method stub
		
		baseUrl=ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		
		logger.info("Preparing template....");
		Map<String, Object> templateVariables=new HashMap<String, Object>();
		templateVariables.put("body", body);
		templateVariables.put("signature", signature);
		templateVariables.put("location", location);
		templateVariables.put("url", baseUrl);
		
		Context context=new Context();
		context.setVariables(templateVariables);
		
		String template=templateEngine.process("mail-template", context);
		
		logger.info("Template prepared....");
		
		MimeMessage message=javaMailSender.createMimeMessage();
		try {
		MimeMessageHelper messageHelper=new MimeMessageHelper(message,  true, "UTF-8");
		
		messageHelper.setFrom(from);
		messageHelper.setTo(to);
		messageHelper.setSubject(subject);
		messageHelper.setText(template, true);
		
		
		for(Attachment attachment:attachments)
		{
			File file=new File(attachment.getFilePath()+attachment.getFileName());
			messageHelper.addAttachment(attachment.getFileName(),file );
		}
		
		
		messageHelper.addInline("logo.png", new ClassPathResource("/static/images/logo.png"), "image/png");
		}
		catch(MessagingException e)
		{
			System.out.println(e.getMessage());
		}
		
		javaMailSender.send(message);
	
		
		logger.info("HTML mail initiated....");
		
		
		
		
		
	}


	@Override
	public String toString() {
		return "NotificationServiceImpl [from=" + from + ", signature=" + signature + ", company=" + company
				+ ", location=" + location + ", baseUrl=" + baseUrl + ", protocol=" + protocol + ", host=" + host
				+ ", port=" + port + ", username=" + username + ", password=" + password + "]";
	}


	@Override
	public void SimpleMail(String to, String subject, String body) {
		// TODO Auto-generated method stub
		logger.info("Mail started");
		
		SimpleMailMessage smm=new SimpleMailMessage();
		smm.setFrom(from);
		smm.setTo(to);
		smm.setSubject(subject);
		smm.setText(body);
		
		javaMailSender.send(smm);
		
		logger.info("Mail successfully sent");
	}

 


	 
}
