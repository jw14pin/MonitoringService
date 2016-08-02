package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.dao.connector.Connector;
import com.exceptions.ExceptionHandler;

public class BaseDAO implements CommonDAO {

	private Connector connector;
	
	/*
	 * Purposefully these methods are not filled out yet
	 */
	@Override
	public int insert(String statement, Object[] params) {
		return connector.getJdbcTemplatePortal().update(statement, params);
	}
	
	@Override
	public int update(String statement, Object[] params) {
		return connector.getJdbcTemplatePortal().update(statement, params);
	}

	@Override
	public int delete(String statement, Object[] params) {
		return connector.getJdbcTemplatePortal().update(statement, params);
	}
	
	/*
	 * Does a db test
	 */
	@Override
	public String select(String connection, String statement) {
		String output = "";
		try {
			if(connection.equals("Portal")) {
				output = (String)connector.getJdbcTemplatePortal().queryForObject(statement, new Object[0], String.class);
			}
			return ""+(output.length() > 0);
		} catch(Exception e) {
			return new ExceptionHandler(e).getStackTrace();
		}
	}
	//NOTE:  only support oracle connections for now...
	public String select(String connection, String connectionString, String statement) {
		try {
			String driver = "oracle.jdbc.driver.OracleDriver";
			Class.forName(driver);
			Connection dbConnection = DriverManager.getConnection(connectionString);
			
			PreparedStatement ps = dbConnection.prepareStatement(statement);
			
			ResultSet result = ps.executeQuery();
			
			return ""+(result.next());
		} catch(Exception e) {
			return new ExceptionHandler(e).getStackTrace();
		}
	}
	@Override
	public Object select(String statement, Object[] params) {
		return connector.getJdbcTemplatePortal().queryForObject(statement, params, Object.class);
	}
	
	@Override
	public List<Map<String,Object>> select(String statement, Object[] params, boolean list) {
		return connector.getJdbcTemplatePortal().queryForList(statement, params);
	}
	
	@Override
	public List<? extends Object> select(String statement, Object[] params, Class<? extends Object> elementType) {
		return connector.getJdbcTemplatePortal().queryForList(statement, params, elementType);
	}
	
	public Connector getConnector() {
		return connector;
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
	}
}
