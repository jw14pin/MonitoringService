package com.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.exceptions.NoMoreTestsException;
import com.models.ChainTest;
import com.models.Command;
import com.models.DBTest;
import com.models.MachineStatus;
import com.models.Status;
import com.models.URLTest;
import com.properties.Loader;

@Component
@Path("/machine")
public class Machine {
	@Autowired
	private Chain chain;
	@Autowired
	private URL url;
	@Autowired
	private DB db;
	@Autowired
	private Loader loader;

	/*
	 * Test a url or db connection in a list of machines
	 */
	public List<MachineStatus> statusObject(List<String> machines, int index) throws NoMoreTestsException, JsonParseException, JsonMappingException, IOException, InterruptedException {	
		ArrayList<MachineStatus> machineStatuses = new ArrayList<MachineStatus>();
		
		//for the lazy typist
		//get all machines
		if(machines.size() == 1 && machines.get(0).equals("all")) {
			machines = loader.getMachineNames();
		}
		
		for(String machine:machines) {
			
			ArrayList<Status> statuses = new ArrayList<Status>();
			
			//note that index must be unique among the machine urls and dbs combined			
			List<URLTest> urlTests = loader.loadURLTests(machine);
			List<DBTest> dbTests = loader.loadDBTests(machine);
			List<ChainTest> chainTests = loader.loadChainTests(machine);
			
			//rather not output the machine in the raw output if there is nothing to test for it
			if(urlTests.size() == 0 && dbTests.size() == 0) {
				continue;
			}
			
			int i = 1;
			
			int count = 0;
			
			//do URL tests first
			//test each url for the machine
			for(URLTest urlTest:urlTests) {
				if(i == index) {
					url.statusObject(machine, urlTest.getName(), urlTest.getCred(), urlTest.getUrl(), urlTest.getVerifyWord(), statuses);
					count++;
					break;
				}
				i++;
			}
			
			//do chain tests
			//test each clickthrough
			for(ChainTest chainTest:chainTests) {
				if(i == index) {
					chain.statusObject(machine, chainTest.getName(), chainTest.getCommands(), statuses);
					count++;
					break;
				}
				i++;
			}
			
			//do DB tests
			//test each url for the machine
			for(DBTest dbTest:dbTests) {
				if(i == index) {				
					db.statusObject(machine, dbTest.getDb(), dbTest.getConnectionString(), dbTest.getVerifyQuery(), statuses);
					count++;
					break;
				}
				i++;
			}
			
			int totalWaitTime = 0;
			
			while(statuses.size() < count && totalWaitTime < 60000) {
				Thread.sleep(1000);
				totalWaitTime += 1000;
				
			}
			
			//if there are no more urls or dbs to test, then return status done
			if(i < index) {
				throw new NoMoreTestsException();
			}
			
			MachineStatus machineStatus = new MachineStatus("machine", machine, ""+true, statuses);
			machineStatuses.add(machineStatus);
		}
		
		return machineStatuses;
	}
	
	/*
	 * Test all urls and db connection on the list of machines
	 */
	public List<MachineStatus> statusObject(List<String> machines) throws JsonParseException, JsonMappingException, IOException, InterruptedException {	
		ArrayList<MachineStatus> machineStatuses = new ArrayList<MachineStatus>();
		
		//for the lazy typist
		//get all machines
		if(machines.size() == 0 || (machines.size() == 1 && machines.get(0).equals("all"))) {
			machines = loader.getMachineNames();
		}
		
		int count = 0;
		
		for(String machine:machines) {
			
			ArrayList<Status> statuses = new ArrayList<Status>();
			
			//note that index must be unique for the machine urls and dbs combined			
			List<URLTest> urlTests = loader.loadURLTests(machine);
			List<DBTest> dbTests = loader.loadDBTests(machine);
			List<ChainTest> chainTests = loader.loadChainTests(machine);
			
			count += urlTests.size() + chainTests.size() + dbTests.size();
			
			System.out.println("URL Tests size:  "+urlTests.size());
			System.out.println("DB Tests size:  "+dbTests.size());
			System.out.println("Chain Tests size:  "+chainTests.size());
			
			//rather not output the machine in the raw output if there is nothing to test for it
			if(urlTests.size() == 0 && dbTests.size() == 0) {
				continue;
			}
			
			//do URL tests first
			//test each url for the machine
			for(URLTest urlTest:urlTests) {
				System.out.println("starting thread for url test:  "+urlTest.getName());
				url.statusObject(machine, urlTest.getName(), urlTest.getCred(), urlTest.getUrl(), urlTest.getVerifyWord(), statuses);
				System.out.println("thread started for url test:  "+urlTest.getName());
			}
			
			//do chain tests
			//test each click-through
			for(ChainTest chainTest:chainTests) {
				System.out.println("starting thread for url test:  "+chainTest.getName());
				chain.statusObject(machine, chainTest.getName(), chainTest.getCommands(), statuses);
				System.out.println("thread started for url test:  "+chainTest.getName());
			}
			
			//do DB tests
			//test each url for the machine
			for(DBTest dbTest:dbTests) {	
				System.out.println("starting thread for db test:  "+dbTest.getDb());
				db.statusObject(machine, dbTest.getDb(), dbTest.getConnectionString(), dbTest.getVerifyQuery(), statuses);
				System.out.println("thread started for db test:  "+dbTest.getDb());
			}
			
			MachineStatus machineStatus = new MachineStatus("machine", machine, ""+true, statuses);
			machineStatuses.add(machineStatus);
		}
		
		int totalWaitTime = 0;
		
		int statusSize = 0;
		
		for(MachineStatus machineStatus:machineStatuses) {
			statusSize += machineStatus.getResources().size();
		}
		
		System.out.println("Condition:  "+statusSize+", "+count+", "+totalWaitTime);
		
		while(statusSize < count && totalWaitTime < 60000) {
			System.out.println("size:  "+statusSize+" count:  "+count);
			System.out.println("waiting...");
			Thread.sleep(1000);
			totalWaitTime += 1000;
			System.out.println("Condition now:  "+statusSize+", "+count+", "+totalWaitTime);
			
			statusSize = 0;
			
			for(MachineStatus machineStatus:machineStatuses) {
				statusSize += machineStatus.getResources().size();
			}
		}
		
		System.out.println("done!");
		
		
		
		return machineStatuses;
	}
	
	/*
	 * Test a single test on one machine
	 */
	public List<MachineStatus> statusObject(String test) throws JsonParseException, JsonMappingException, IOException, InterruptedException {	
		ArrayList<MachineStatus> machineStatuses = new ArrayList<MachineStatus>();
			
		ArrayList<Status> statuses = new ArrayList<Status>();
		
		//get a single test
		Map<String,Object> testMap = loader.loadTest(test);
		
		int count = 0;
		
		//this is totally hardcoded stuff...consider refactoring later
		if(testMap.get("TYPE").equals("url")) {
		
			//do URL tests first
			//test each url for the machine
			url.statusObject((String)testMap.get("MACHINE"), (String)testMap.get("NAME"), "", (String)testMap.get("URL"), (String)testMap.get("VERIFY_PHRASE"), statuses);
			count++;
		} else if(testMap.get("TYPE").equals("db")) {
			//do DB tests
			//test each url for the machine
			
			//USING NAME FIELD for DB name and VERIFY_PHRASE for verify sql FOR NOW...consider using connection string instead
			db.statusObject((String)testMap.get("MACHINE"), (String)testMap.get("NAME"), (String)testMap.get("URL"), (String)testMap.get("VERIFY_PHRASE"), statuses);
			count++;
		} else if(testMap.get("TYPE").equals("chain")) {
			ObjectMapper mapper = new ObjectMapper();
			
			mapper.enableDefaultTyping();  //enable polymorphic identification
			JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, Command.class);
			List<Command> commands = mapper.readValue((String)testMap.get("URL"), type);
			
			chain.statusObject((String)testMap.get("MACHINE"), (String)testMap.get("NAME"), commands, statuses);
			count++;
		}
		
		int totalWaitTime = 0;
		
		while(statuses.size() < count && totalWaitTime < 60000) {
			Thread.sleep(1000);
			totalWaitTime += 1000;
		}
		
		MachineStatus machineStatus = new MachineStatus("machine", (String)testMap.get("MACHINE"), ""+true, statuses);
		machineStatuses.add(machineStatus);
		
		return machineStatuses;
	}
}
