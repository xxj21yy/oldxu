package com.example.collectdata;

import java.io.File;
import java.io.Serializable;

public class WriteObject implements Serializable{

	
	private static final long serialVersionUID = -7915038926565545343L;
	
	private File file;
	private String data;
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	

}
