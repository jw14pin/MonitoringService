package com.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;

import com.dao.BaseDAO;
import com.dao.CommonDAO;
import com.models.Command;
import com.models.DBTest;
import com.models.URLTest;
import com.models.ChainTest;

public class DatabaseLoader implements Loader {
	@Autowired
	private CommonDAO baseDAO;
	
	private String machineSQL = "select distinct machine from monitoring_tests";
	private String testNamesSQL = "select distinct name, test_id from monitoring_tests";
	private String urlSQL = "select * from monitoring_tests where machine = ? and type = 'url'";
	private String chainSQL = "select * from monitoring_tests where machine = ? and type= 'chain'";
	private String testSQL = "select * from monitoring_tests where test_id = ?";
	private String dbSQL = "select * from monitoring_tests where machine = ? and type='db'";
	
	@Override
	public List<ChainTest> loadChainTests(String machine) throws JsonParseException, JsonMappingException, IOException {
		List<ChainTest> tests = new ArrayList<ChainTest>();

		Object[] params = {machine.toLowerCase()};
		List<Map<String, Object>> rows = baseDAO.select(chainSQL, params, true);
		
		ObjectMapper mapper = new ObjectMapper();
		
		//map Map to List<Command> object
		for(Map<String,Object> row:rows) {
			ChainTest chainTest = new ChainTest();
			//get the commands from the json string in the url field
			mapper.enableDefaultTyping();  //enable polymorphic identification
			JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, Command.class);
			List<Command> commands = mapper.readValue((String)row.get("URL"), type);
			
			chainTest.setCommands(commands);
			
			chainTest.setName((String)row.get("NAME"));
			
			tests.add(chainTest);			
		}
		
		return tests;
	}
	
	@Override
	public List<URLTest> loadURLTests(String machine) {
		ArrayList<URLTest> urlTests = new ArrayList<URLTest>();
		Object[] params = {machine.toLowerCase()};
		List<Map<String, Object>> tests = baseDAO.select(urlSQL, params, true);
		
		//map Map to URLTest object
		for(Map<String,Object> test:tests) {
			urlTests.add(new URLTest((String)test.get("NAME"), (String)test.get("URL"), (String)test.get("VERIFY_PHRASE"), ""));
		}
		
		return urlTests;
	}

	@Override
	public List<DBTest> loadDBTests(String machine) {
		ArrayList<DBTest> dbTests = new ArrayList<DBTest>();
		Object[] params = {machine.toLowerCase()};
		List<Map<String, Object>> tests = baseDAO.select(dbSQL, params, true);
		
		//map Map to URLTest object
		for(Map<String,Object> test:tests) {
			dbTests.add(new DBTest((String)test.get("NAME"), (String)test.get("URL"), (String)test.get("VERIFY_PHRASE")));
		}
		
		return dbTests;
	}

	@Override
	public List<String> getMachineNames() {
		return (List<String>)baseDAO.select(machineSQL, new Object[0], String.class);
	}
	
	@Override
	public List<Map<String,Object>> getTestNames() {
		return baseDAO.select(testNamesSQL, new Object[0], true);
	}
	
	@Override
	public Map<String,Object> loadTest(String id) {
		Object[] params = {id};
		List<Map<String, Object>> tests = baseDAO.select(testSQL, params, true);
		
		return tests.get(0);
	}
	
}
