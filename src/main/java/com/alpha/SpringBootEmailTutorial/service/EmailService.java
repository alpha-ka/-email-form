package com.alpha.SpringBootEmailTutorial.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alpha.SpringBootEmailTutorial.AttachmentReceiver.EmailAttachmentReceiver;

@Service
public class EmailService {

	// @Value("${spring.mail.properties.mail.transport.protocol}")
	private String protocol = "imap";
	// @Value("${spring.mail.host}")
	private String host = "imap.gmail.com";
	// @Value("${spring.mail.port}")
	private String port = "993";

	// your email id
	@Value("${spring.mail.username}")
	private String username;// Put here Gmail Username without @ sign
	// Your Email Password For Account
	@Value("${spring.mail.password}")
	private String password;

	@Value("${savepath}")
	private String saveDirectory;

	public void DownloadAttachment() throws MessagingException, IOException {

		final Properties properties = new Properties();

		// server setting
		properties.put(String.format("mail.%s.host", protocol), host);
		properties.put(String.format("mail.%s.port", protocol), port);

		// SSL setting
		properties.setProperty(String.format("mail.%s.socketFactory.class", protocol),
				"javax.net.ssl.SSLSocketFactory");
		properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
		properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), port);

		final Session session = Session.getInstance(properties, null);
		final Store store = session.getStore(protocol);
		// connect
		store.connect(host, username, password);
		session.setDebug(true);

		// opens the inbox folder
		Folder folder = store.getFolder("INBOX");
		//folder.open(Folder.READ_ONLY);
		folder.open(Folder.READ_WRITE);

		   // creates a search criterion
        SearchTerm searchCondition = new SearchTerm() {
            @Override
            public boolean match(Message message) {
                try {
                    if (message.getSubject().contains("Students")) {
                        return true;
                    }
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        };
        Message[] arrayMessages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
		//Message[] arrayMessages =  folder.search(searchCondition);

		for (int i = 0; i < arrayMessages.length; i++) {
			Message message = arrayMessages[i];
			  if (message.getSubject().contains("Students"))
			System.out.println(downloadAttachments(message));
		}

	}

	public List<String> downloadAttachments(Message message) throws IOException, MessagingException {
		List<String> downloadedAttachments = new ArrayList<String>();
		 
			Multipart multiPart = (Multipart) message.getContent();
			int numberOfParts = multiPart.getCount();
			for (int partCount = 0; partCount < numberOfParts; partCount++) {
				MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
				if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
					String file = part.getFileName();
					part.saveFile(saveDirectory + File.separator + part.getFileName());
					downloadedAttachments.add(file);
				}
			}
       
		
		return downloadedAttachments;
	}
}
