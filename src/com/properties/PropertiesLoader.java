package com.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.models.ChainTest;
import com.models.Command;
import com.models.DBTest;
import com.models.URLTest;

public class PropertiesLoader implements Loader {	
	@Override
	public List<URLTest> loadURLTests(String machine) {
		Properties prop = new Properties();
		ArrayList<URLTest> urlTests = new ArrayList<URLTest>();
		 
    	try {
            //load a properties file    		
    		loadProp(prop, "nodes.properties");
    		
    		int i = 1;
    		
    		System.out.println(machine);
    		
    		while(prop.containsKey(machine+".url."+i)) {
    			System.out.println((String)prop.getProperty(machine+".url."+i));
    			
    			urlTests.add(new URLTest((String)prop.getProperty(machine+".url."+i), (String)prop.getProperty(machine+".url."+i), (String)prop.getProperty(machine+".verify."+i), (String)(prop.getProperty(machine+".cred."+i) == null ? "" : prop.getProperty(machine+".cred."+i))));
    			
    			i++;
    		}
 
    		return urlTests;
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
    	
    	return null;
	}
	
	@Override
	public List<DBTest> loadDBTests(String machine) {
		Properties prop = new Properties();
		ArrayList<DBTest> dbTests = new ArrayList<DBTest>();
		
    	try {
            //load a properties file    		
    		loadProp(prop, "nodes.properties");
    		
    		int i = 1;
    		
    		System.out.println(machine);
    		
    		while(prop.containsKey(machine+".db."+i)) {
    			System.out.println((String)prop.getProperty(machine+".db."+i));
    			
    			
    			//connection string is never filled in here for property files
    			dbTests.add(new DBTest((String)prop.getProperty(machine+".db."+i), "", (String)prop.getProperty(machine+".dbverify."+i)));
    			
    			i++;
    		}
 
    		return dbTests;
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
    	
    	return null;
	}
	
	@Override
	public List<String> getMachineNames() {
		Properties prop = new Properties();
		ArrayList<String> machines = new ArrayList<String>();
		 
    	try {
            //load a properties file
    		loadProp(prop, "nodes.properties");
    		
    		Set<Object> keys = prop.keySet();
    		
    		for(Object key:keys) {
    			String stringedKey = (String)key;
    			String name = stringedKey.substring(0, stringedKey.indexOf('.'));
    			if(!machines.contains(name)) {
    				machines.add(name);
    			}
    		}
 
    		return machines;
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
    	
    	return null;
	}
	
	public static List<String> getCredentials(String cred) {
		Properties prop = new Properties();
		ArrayList<String> credentials = new ArrayList<String>();
		 
    	try {
            //load a properties file    		
    		loadProp(prop, "general.properties");
    		
    		String username = (String)prop.getProperty(cred+".username");
    		String password = (String)prop.getProperty(cred+".password");
 
    		credentials.add(username);
    		credentials.add(password);
    		
    		return credentials;
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
    	
    	return null;
	}
	
	private static void loadProp(Properties prop, String filename) throws IOException {
		prop.load(PropertiesLoader.class.getClassLoader().getResourceAsStream(filename));
	}

	@Override
	public Map<String, Object> loadTest(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getTestNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ChainTest> loadChainTests(String machine)
			throws JsonParseException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
