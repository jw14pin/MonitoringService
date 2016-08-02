package com.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dao.BaseDAO;
import com.dao.CommonDAO;
import com.models.Command;
import com.parsers.Parser;

@Component
@Path("/test")
public class Test {
	@Autowired
	private CommonDAO baseDAO;
	
	/*
	 * register a test (TODO:  change to POST request)
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/register")
	public String register(@QueryParam("type") String type, @QueryParam("machine") String machine, @QueryParam("name") String name, @QueryParam("url") String url, @QueryParam("verify_phrase") String verify_phrase, @QueryParam("timeout") String timeout) {		
		try {
			//System.out.println(type+":-:"+machine+":-:"+name+":-:"+url+":-:"+verify_phrase+":-:"+timeout);
			
			//connect to some db here to insert test document
			String statement = "insert into monitoring_tests (type, machine, name, url, verify_phrase, timeout) values (?,?,?,?,?,?)";
			Object[] params = {type, machine.toLowerCase(), name, url, verify_phrase, new Integer(timeout)};
			
			if(baseDAO.insert(statement, params) == 0) {
				//System.out.println("Test was not inserted");
				throw new Exception("Test was not inserted");
			}
			
			return "true";
		} catch(Exception e) {
			e.printStackTrace();
			return "false";
		}
	}
	
	/*
	 * get test information
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/info")
	public Map<String,Object> testInfo(@QueryParam("id") String id) {		
		try {
			//connect to some db here to insert test document
			String statement = "select * from monitoring_tests where test_id = ?";
			Object[] params = {id};
			
			List<Map<String,Object>> items = baseDAO.select(statement, params, true);
			
			if(items.size() == 0) {
				throw new Exception("No test found.");
			}
			
			Map<String,Object> test = items.get(0);
			
			if(test.get("TYPE").equals("chain")) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.enableDefaultTyping();  //enable polymorphic identification
				JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Command.class);
				List<Command> commands = mapper.reader(javaType).readValue((String)test.get("URL"));
				String commandString = Parser.convertToCommandString(commands);
				test.put("URL", commandString);
			}
			
			return test;
		} catch(Exception e) {
			e.printStackTrace();
			HashMap<String,Object> error = new HashMap<String,Object>();
			error.put("ErrorMessage", e.getMessage());
			error.put("ErrorTrace", e.getStackTrace());
			return error;
		}
	}
	
	/*
	 * update test
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update")
	public String update(@QueryParam("id") String id, @QueryParam("type") String type, @QueryParam("machine") String machine, @QueryParam("name") String name, @QueryParam("url") String url, @QueryParam("verify_phrase") String verify_phrase, @QueryParam("timeout") String timeout) {		
		try {
			if(type.equals("chain")) {
				List<Command> commandList = Parser.parseCommandString(url);
				
				ObjectMapper mapper = new ObjectMapper();
				mapper.enableDefaultTyping();  //enable polymorphic identification
				JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Command.class);
				url = mapper.writerWithType(javaType).writeValueAsString(commandList);  //need to test to make sure that the type information is being passed in...
			}
				
			//connect to some db here to update test document
			String statement = "update monitoring_tests set type = ?, machine = ?, name = ?, url = ?, verify_phrase = ?, timeout = ? where test_id = ?";
			Object[] params = {type, machine.toLowerCase(), name, url, verify_phrase, new Integer(timeout), id};
			
			if(baseDAO.update(statement, params) == 0) {
				throw new Exception("Test was not updated");
			}
			
			return "true";
		} catch(Exception e) {
			e.printStackTrace();
			return "false "+e.getMessage();
		}
	}
	
	/*
	 * delete test
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete")
	public String delete(@QueryParam("id") String id) {		
		try {
			//connect to some db here to delete test document
			String statement = "delete from monitoring_tests where test_id = ?";
			Object[] params = {id};
			
			if(baseDAO.delete(statement, params) == 0) {
				throw new Exception("Test was not deleted");
			}
			
			return "true";
		} catch(Exception e) {
			e.printStackTrace();
			return "false";
		}
	}
}
