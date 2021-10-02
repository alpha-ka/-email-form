package com.alpha.SpringBootEmailTutorial.controller;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.alpha.SpringBootEmailTutorial.model.Email;
import com.alpha.SpringBootEmailTutorial.service.EmailService;
import com.alpha.SpringBootEmailTutorial.service.NotificationService;
import com.alpha.SpringBootEmailTutorial.service.SaveAttachmentandSendMail;

@Controller
public class EmailController {
	
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private SaveAttachmentandSendMail saveAttachmentandSendMail;
	
	@Autowired
	private EmailService emailService;
	
	Logger logger=LoggerFactory.getLogger(EmailController.class);

	@GetMapping("/email-form")
	public String showEmailForm(Model model)
	{
		model.addAttribute("email",new Email());
		return "email-form";
	}
	
	
	@PostMapping("/sendMail")
	public String sendMail(@Valid Email email ,BindingResult bindingResult) throws IOException
	{
		
		if(bindingResult.hasErrors())
		{
			logger.error(""+email);
	 
			return "/email-form";
		}
		logger.info(""+email);
		
		
		//notificationService.SimpleMail(email.getTo(), email.getSubject(), email.getBody());
		logger.info(""+email);
		saveAttachmentandSendMail.saveFiles(email);
		logger.info(notificationService.toString());
		return "redirect:/email-form";
		
	}
	
	@GetMapping("/get-Attachments")
	public String downloadAttachment() throws MessagingException, IOException
	{
		emailService.DownloadAttachment();
		
		return "redirect:/email-form";
	}
	
 
}
