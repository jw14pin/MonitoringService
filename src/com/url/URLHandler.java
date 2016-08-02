package com.url;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.models.Command;
import com.properties.PropertiesLoader;

public class URLHandler {
	//handle test requests
	public String testURL(String cred, String url, String params, String verifyWord) {  //verify word makes sure you are on the right page
		try {
			
			url = addCredentials(cred, url);
			
			String seleniumResult = seleniumTest(url, verifyWord);	//selenium test
			
			//String urlResult = urlTest(url, params, verifyWord);	//HttpURLConnection test
			
			return new Boolean(seleniumResult) ? ""+true : seleniumResult;  //these are pretty much redundant tests so either one getting the page is fine.
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public String addCredentials(String cred, String url) {
		List<String> credentials = PropertiesLoader.getCredentials(cred == null ? "" : cred);
		
		return url.replace("%USERNAME%", credentials.get(0) == null ? "" : credentials.get(0)).replace("%PASSWORD%", credentials.get(1) == null ? "" : credentials.get(1));
	}
	
	//test using the HttpURLConnection class
	public String urlTest(String url, String params, String verifyWord) {
		try {
			//System.out.println("url test start");
			
			URL address = new URL(url);
			HttpURLConnection connection = (HttpURLConnection)address.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", ""+Integer.toString(params.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US"); 
		
			connection.setConnectTimeout(4000);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
		
			//Send request
			DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
			wr.writeBytes (params);
			wr.flush ();
			wr.close ();
		
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
			StringBuilder builder = new StringBuilder();
			String line;
		
			while((line = reader.readLine()) != null) {
				builder.append(line+'\n');
			}
			
			boolean code = (HttpURLConnection.HTTP_OK == connection.getResponseCode());
			
			//System.out.println("url test done");
			
			//System.out.println("CODE:  "+connection.getResponseCode()+(code));
		
			return code && builder.toString().contains(verifyWord) ? ""+code : ""+false;
		} catch(Exception e) {
			//System.out.println(e.getMessage());
			
			//System.out.println("url test done");
			
			return e.getMessage();
		}
	}
	
	//test using Selenium
	public String seleniumTest(String url, String word) {
		WebDriver driver = null;
		final String word2 = word;
		
		try {
			
			//System.out.println("url test start");
			
			driver = new HtmlUnitDriver();
			
//			WebDriverWait wait = new WebDriverWait(driver, 10);
			
//			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
//				       .withTimeout(10, TimeUnit.SECONDS)
//				       .pollingEvery(5, TimeUnit.SECONDS)
//				       .ignoring(NoSuchElementException.class);
			
			
			driver.get(url);
			
			Thread.sleep(100);
			
			String urlCurrent = driver.getCurrentUrl();
			
			if(urlCurrent != url) {
				driver.get(url);
			}
			
//			wait.until(new Function<WebDriver, WebElement>() {
//				public WebElement apply(WebDriver driver) {
//					return driver.findElement(By.xpath("//*[contains(.,'"+word2+"')]"));
//				}
//			});
			
			String result = ""+driver.getPageSource().contains(word);
			
			assert result.equals("true");
			
			driver.close();
			
			//System.out.println("url test done");
			
			return result;
		} catch(Exception e) {
			if(driver != null) {
				driver.close();
			}
			//System.out.println(e.getMessage());
			
			System.out.println("url test done");
			
			return e.getMessage();
		}
	}
	
	//test a set of linked pages using Selenium
	/* Client outline:
	 * 	-client clicks on register and selects the option URLChain
	 *  -the client now sees several options including URL, click element identifier, and verify word for a particular page
	 *  -after filling out the information, the client has the option to add a new set with the same options as above for a new page
	 *  -once the client is done creating the chain, the client registers the test
	 */
	public String seleniumChainTest(List<Command> commands) {
		WebDriver driver = null;
		
		try {
			
			driver = new HtmlUnitDriver();
			
			boolean success = true;
			
			System.out.println("Num of commands:  "+commands.size());
			
			//if one is false, then the whole test reports false...
			for(Command command:commands) {
				command.setDriver(driver);
				System.out.println("driver added");
				success = success && command.execute();
				System.out.println("one command done.  Result:  "+success);
			}
			
			return ""+success;
		} catch(Exception e) {
			if(driver != null) {
				driver.close();
			}
			//System.out.println(e.getMessage());
			
			return e.getMessage();
		}
	}
}
