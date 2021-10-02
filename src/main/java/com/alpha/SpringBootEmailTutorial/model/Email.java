package com.alpha.SpringBootEmailTutorial.model;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

public class Email {

	private String from;

	@NotBlank(message = "To address cannot be empty")
	private String to;
	@NotBlank(message = "Subject cannot be empty")
	private String subject;
	@NotBlank(message = "Body cannot be empty")
	private String body;

	private MultipartFile[] files;

	private List<Attachment> attachments;

	public Email() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Email(String from, @NotBlank(message = "To address cannot be empty") String to,
			@NotBlank(message = "Subject cannot be empty") String subject,
			@NotBlank(message = "Body cannot be empty") String body, MultipartFile[] files,
			List<Attachment> attachments) {
		super();
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.body = body;
		this.files = files;
		this.attachments = attachments;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public MultipartFile[] getFiles() {
		return files;
	}

	public void setFiles(MultipartFile[] files) {
		this.files = files;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public String toString() {
		return "Email [from=" + from + ", to=" + to + ", subject=" + subject + ", body=" + body + ", files="
				+ Arrays.toString(files) + ", attachments=" + attachments + "]";
	}

}
