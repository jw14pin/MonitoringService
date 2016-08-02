package com.models;

public class DBTest {
	private String db;
	private String connectionString;
	private String verifyQuery;
	
	public DBTest(String db, String connectionString, String verifyQuery) {
		super();
		this.db = db;
		this.connectionString = connectionString;
		this.verifyQuery = verifyQuery;
	}
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public String getVerifyQuery() {
		return verifyQuery;
	}
	public void setVerifyQuery(String verifyQuery) {
		this.verifyQuery = verifyQuery;
	}
}
