package com.models;

import org.openqa.selenium.WebDriver;

public class StubCommand implements Command {
	private String command = "stub";
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


	@Override
	public boolean execute() {
		return true;
	}
}
