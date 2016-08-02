package com.resources;

import java.util.List;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.models.Status;
import com.url.URLHandler;

@Component
@Path("/url")
public class URL {
	
	class URLThread implements Runnable {
		String machine;
		String name; 
		String cred;
		String url;
		String verifyWord;
		List<Status> returnList;
		
		URLThread(String machine, String name, String cred, String url, String verifyWord, List<Status> returnList) {
			this.machine = machine;
			this.name = name;
			this.cred = cred;
			this.url = url;
			this.verifyWord = verifyWord;
			this.returnList = returnList;
		}
		
		//need a new url handler every time to prevent synchronization issues do not occur
		@Override
		public void run() {
			returnList.add(new Status(machine, "url", name, new URLHandler().testURL(cred, url, "", verifyWord)));
		}
	}
	
	/*
	 * gets the status for a url
	 */
	public synchronized void statusObject(String machine, String name, String cred, String url, String verifyWord, List<Status> returnList) {
		
		Thread thread = new Thread(new URLThread(machine, name, cred, url, verifyWord, returnList));
		thread.start();
	}
}
