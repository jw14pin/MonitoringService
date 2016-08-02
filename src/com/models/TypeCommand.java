package com.models;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TypeCommand implements Command {
	private String command = "type";
	private String word;
	private String characters;
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
	

	public String getCharacters() {
		return characters;
	}



	public void setCharacters(String characters) {
		this.characters = characters;
	}



	@Override
	public boolean execute() {
		// xTODO execute a typing command on a textbox
		WebDriverWait wait = new WebDriverWait(driver, 10);  //waits a max of ten seconds
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(word)));
		element.sendKeys(characters);
		return true;
	}

}
