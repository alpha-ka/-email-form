package com.alpha.SpringBootEmailTutorial.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.SpringBootEmailTutorial.controller.EmailController;
import com.alpha.SpringBootEmailTutorial.model.Attachment;
import com.alpha.SpringBootEmailTutorial.model.Email;

@Service
public class SaveAttachmentandSendMail {
	@Value("${file.upload-dir}")
	private String FILE_DIRECTORY;
	
	@Autowired
	private NotificationService notificationService;

	
	Logger log=LoggerFactory.getLogger(SaveAttachmentandSendMail.class);
	public void saveFiles(Email email)   {

		List<Attachment> attachments=new ArrayList<Attachment>();
		String attachFiles=null; 
		MultipartFile[] files=email.getFiles();
		for(MultipartFile file : files)
		{
			
			
			File myFile=new File(FILE_DIRECTORY+file.getOriginalFilename());
			try {
			myFile.createNewFile();
			FileOutputStream fos=new FileOutputStream(myFile);
			fos.write(file.getBytes());
			fos.close();
			}
			catch(IOException e)
			{System.out.println(e);}
			 
			
			
			Attachment attachment=new Attachment();
			attachment.setFileName(file.getOriginalFilename());
			attachment.setFilePath(FILE_DIRECTORY);
			attachments.add(attachment);
		 
		}
				
		email.setAttachments(attachments);
		log.info(">>>>>>>>>>>"+email);
		notificationService.sendNotificationWithTemplate(email.getTo(), email.getSubject(), email.getBody(), attachments);
	}

}
