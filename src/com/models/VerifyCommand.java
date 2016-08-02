package com.models;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VerifyCommand implements Command {
	private String command = "verify";
	private String phrase;
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
	
	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	@Override
	public boolean execute() {
		WebDriverWait wait = new WebDriverWait(driver, 10);  //waits a max of ten seconds
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(.,'"+phrase+"')]")));
		return true;
	}
	
}
