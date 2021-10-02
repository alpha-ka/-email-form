package com.alpha.SpringBootEmailTutorial.service;

import java.util.List;

import com.alpha.SpringBootEmailTutorial.model.Attachment;

public interface NotificationService {

	void SimpleMail(String to,String subject,String body);
	void sendNotificationWithTemplate(String to , String subject,String body,List<Attachment> attachments);
  
	
	
}
