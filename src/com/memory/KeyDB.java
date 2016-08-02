package com.memory;

import java.util.ArrayList;

public class KeyDB {
	
	private static ArrayList<String> keys;  //TODO:  need to expire old keys
	
	public static synchronized void addKey(String key) {
		if(keys == null) {
			keys = new ArrayList<String>();
		}
		keys.add(key);
	}
	
	public static synchronized boolean containsKey(String key) {
		if(keys == null) {
			keys = new ArrayList<String>();
		}
		return keys.contains(key);
	}
}
