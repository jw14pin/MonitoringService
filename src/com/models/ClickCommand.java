package com.models;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClickCommand implements Command {
	private String command = "click";
	private String word;
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

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public boolean execute() {
		// xTODO execute a click on a link or button
		//System.out.println("Current page:  "+driver.getPageSource());
		WebDriverWait wait = new WebDriverWait(driver, 10);  //waits a max of ten seconds
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(word)));
		//System.out.println("Current page:  "+driver.getPageSource());
		//System.out.println("Waited until found id in click");
		element.click();
		return true;
	}
	
}
