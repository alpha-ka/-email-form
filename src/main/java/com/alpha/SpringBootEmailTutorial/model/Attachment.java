package com.alpha.SpringBootEmailTutorial.model;

public class Attachment {

	private String fileName;
	
	private String filePath;

	
	public Attachment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Attachment(String fileName, String filePath) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return "Attachment [fileName=" + fileName + ", filePath=" + filePath + "]";
	}
	
	
}
