package com.models;

import org.openqa.selenium.WebDriver;

public interface Command {
	public void setDriver(WebDriver driver);
	public String getCommand();
	public boolean execute();  //executes the command and returns true = success or false = failure
}
