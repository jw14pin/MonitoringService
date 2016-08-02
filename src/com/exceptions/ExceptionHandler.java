package com.exceptions;

public class ExceptionHandler {
	private Exception e;
	
	public ExceptionHandler(Exception e) {
		this.e = e;
	}
	
	public String getStackTrace() {
		e.printStackTrace();
		StackTraceElement[] stackTrace = e.getStackTrace();
		String output = e.getMessage()+":";
		for(StackTraceElement element:stackTrace) {
			String line = element.getFileName() != null ? element.getFileName()+":"+element.getLineNumber() : "Unknown Source";
			output += "at "+element.getClassName()+"."+element.getMethodName()+"("+line+")";
		}
		return output;
	}
}
