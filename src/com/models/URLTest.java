package com.models;

public class URLTest {
	private String url;
	private String verifyWord;
	private String cred;
	private String name;
	
	public URLTest(String name, String url, String verifyWord, String cred) {
		this.name = name;
		this.url = url;
		this.verifyWord = verifyWord;
		this.cred = cred;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCred() {
		return cred;
	}
	public void setCred(String cred) {
		this.cred = cred;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVerifyWord() {
		return verifyWord;
	}
	public void setVerifyWord(String verifyWord) {
		this.verifyWord = verifyWord;
	}
	
	
}
