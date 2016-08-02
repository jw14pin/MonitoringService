package com.resources;

import java.util.List;
import java.util.concurrent.Executor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dao.CommonDAO;
import com.models.Command;
import com.models.Status;
import com.parsers.Parser;
import com.url.URLHandler;

@Component
@Path("/chain")
public class Chain {
	@Autowired
	private CommonDAO baseDAO;
	private Executor executor;
	
	
	
	public CommonDAO getBaseDAO() {
		return baseDAO;
	}

	public void setBaseDAO(CommonDAO baseDAO) {
		this.baseDAO = baseDAO;
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	class ChainThread implements Runnable {
		String machine;
		String name;
		List<Command> commands;
		List<Status> returnList;
		
		ChainThread(String machine, String name, List<Command> commands, List<Status> returnList) {
			this.machine = machine;
			this.name = name;
			this.commands = commands;
			this.returnList = returnList;
		}
		
		//need a new url handler every time to prevent synchronization issues do not occur
		@Override
		public void run() {
			System.out.println("test: "+name+" start");
			returnList.add(new Status(machine, "chain", name, new URLHandler().seleniumChainTest(commands)));
			System.out.println("test: "+name+" end");
		}
	}
	
	/*
	 * gets the status for the Selenium chain test
	 */
	public synchronized void statusObject(String machine, String name, List<Command> commands, List<Status> returnList) {
		Thread thread = new Thread(new ChainThread(machine, name, commands, returnList));
		if(executor != null) {
			executor.execute(thread);
		}
		thread.start();

	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/insert")
	public String insertObject(@QueryParam("type") String type, @QueryParam("machine") String machine, @QueryParam("name") String name, @QueryParam("commands") String commands, @QueryParam("timeout") String timeout) {		
		try {
			List<Command> commandList = Parser.parseCommandString(commands);
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.enableDefaultTyping();  //enable polymorphic identification
			JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Command.class);
			String url = mapper.writerWithType(javaType).writeValueAsString(commandList);  //need to test to make sure that the type information is being passed in...
			
			//String url = commands;
			
			String statement = "insert into monitoring_tests (type, machine, name, url, timeout) values (?,?,?,?,?)";
			Object[] params = {type, machine.toLowerCase(), name, url, new Integer(timeout)};
			
			if(baseDAO.insert(statement, params) == 0) {
				throw new Exception("Test was not inserted");
			}
			
		} catch (Exception e) {
			return "false "+e.getMessage();
		}
		
		return "true";
	}
}
