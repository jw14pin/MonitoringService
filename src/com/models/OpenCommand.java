package com.models;

import org.openqa.selenium.WebDriver;

public class OpenCommand implements Command {
	private String command = "open";
	private String url;
	private WebDriver driver;
	
	
	
	
	public WebDriver getDriver() {
		return driver;
	}

	@Override
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	@Override
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean execute() {
		// xTODO execute an open on a URL
		//System.out.println("EXECUTING:  "+url);
		driver.get(url);
		//System.out.println("TRUE");
		return true;
	}

}
