package com.dao;

import java.util.List;
import java.util.Map;

public interface CommonDAO {
	public int insert(String statement, Object[] params);
	
	public int update(String statement, Object[] params);
	
	public int delete(String statement, Object[] params);
	
	public String select(String connection, String connectionString, String statement);
	
	public String select(String connection, String statement);
	
	public Object select(String statement, Object[] params);
	
	public List<Map<String,Object>> select(String statement, Object[] params, boolean list);

	public List<? extends Object> select(String statement, Object[] params, Class<? extends Object> elementType);
}
