package com.alpha.SpringBootEmailTutorial.AttachmentReceiver;


 
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import java.io.*;
import java.util.Arrays;
import java.util.Properties;

 @Service
public class DownloadEmailWithAttachment {

    /**
     * Returns a Properties object which is configured for a POP3/IMAP server
     *
     * @param protocol either "imap" or "pop3"
     * @param host
     * @param port
     * @return a Properties object
     */
	
	  static Logger logger=LoggerFactory.getLogger(DownloadEmailWithAttachment.class);
	
    public static Properties getServerProperties(String protocol, String host, String port) {
        final Properties properties = new Properties();

        
        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);

        // SSL setting
        properties.setProperty(String.format("mail.%s.socketFactory.class", protocol), "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
        properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), port);

        return properties;
    }

    public void downloadAttachmentEmail(String protocol, String host, String port, String username, String password) throws MessagingException, IOException {

        final Session session = Session.getInstance(getServerProperties(protocol, host, port), null);
        final Store store = session.getStore(protocol);
        //connect
        store.connect(host, username, password);

        // Your INBOX Folder --->>
        final Folder folder;
        folder = store.getFolder("INBOX");

        folder.open(Folder.READ_ONLY);
        //folder.open(Folder.READ_WRITE);

        // Fetch unseen messages from inbox folder
        // Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), true));

        final Message[] messages = folder.getMessages();

        // Sort messages from recent to oldest
        Arrays.sort(messages, (m1, m2) -> {
            try {
                return m2.getSentDate().compareTo(m1.getSentDate());
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        logger.info("Found message {}", messages.length);
        for (Message message : messages) {
            logger.info(String.format("From %s , Subject : %s, Date Send [%s]", message.getFrom()[0].toString(), message.getSubject(),
                    DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(message.getSentDate())));
            final Object content = message.getContent();
            if (content instanceof Multipart) {
                handleMultipart((Multipart) content);
            } else {
                handlePart(message);
            }
        }

        folder.close(false);
        store.close();
    }

   

    public static void handleMultipart(Multipart multipart) throws MessagingException, IOException {
        for (int i = 0; i < multipart.getCount(); i++) {
            handlePart(multipart.getBodyPart(i));
        }
    }

    public static void handlePart(Part part) throws MessagingException, IOException {
        final String disposition = part.getDisposition();
        final String contentType = part.getContentType();
        if (disposition == null) {
        	 logger.info("content-type: " + contentType);
            if ((contentType.length() >= 10) && (contentType.toLowerCase().substring(0, 10).equals("text/plain"))) {
                part.writeTo(System.out);
            } else {
            	 logger.info("Other body: " + contentType);
                part.writeTo(System.out);
            }
        } else if (disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
        	 logger.info("Attachment: " + part.getFileName() + " : " + contentType);
            saveFile(part.getFileName(), part.getInputStream());
        } else if (disposition.equalsIgnoreCase(Part.INLINE)) {
        	 logger.info("Inline: " + part.getFileName() + " : " + contentType);
            saveFile(part.getFileName(), part.getInputStream());
        } else {
        	 logger.info("Other: " + disposition);
        }
    }

    public static void saveFile(String filename, InputStream input) throws IOException {
        if (filename == null) {
            filename = File.createTempFile("MailAttacheFile", ".out").getName();
        }

        final File tmpInFolder = new File(FileUtils.getTempDirectory(), System.currentTimeMillis() + RandomStringUtils.randomAlphabetic(10));
        createFolder(tmpInFolder);

        logger.info("downloading attachment..." + filename);
        File file = new File(tmpInFolder, filename);

        for (int i = 0; file.exists(); i++) {
            file = new File(filename + i);
        }

        try (OutputStream outputStream = new FileOutputStream(file)) {
            IOUtils.copy(input, outputStream);
        }
        logger.info("done attachment..." + file.getAbsolutePath());
    }

    public static void createFolder(File folder) throws IOException {
        if (!folder.mkdirs()) {
            if (!folder.exists() || !folder.isDirectory()) {
                throw new IOException("could not read input stream");
            }
        }
    }
    
//     public static void main(String args[]) throws Exception {
//        // for POP3
//        String protocol = "pop3";
//        String host = "pop.gmail.com";
//        String port = "995";
//
//        // your email id 
//        String username = "mytesting@gmail.com"; //Put here Gmail Username without @ sign
//        //Your Email Password For Account
//        String password = "*********";
//
//        DownloadEmailWithAttachment downloadEmailWithAttachment = new DownloadEmailWithAttachment();
//        downloadEmailWithAttachment.downloadAttachmentEmail(protocol, host, port, username, password);
//
//    }
}
