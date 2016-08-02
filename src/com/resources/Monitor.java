package com.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.exceptions.NoMoreTestsException;
import com.models.ErrorMessage;
import com.properties.Loader;

@Component
@Path("/monitor")
public class Monitor {
	@Autowired
	private Machine machine;
	@Autowired
	private Loader loader;

	/*
	 * assumes access to all machines
	 * gets the aggregated statuses of all machines, urls, dbs, etc...
	 * should check for connection to all servers first before checking server specific connections
	 */
	public List<?> statusJSON() {
		System.out.println("Run all");
		
		try {
			return machine.statusObject(new ArrayList<String>(0));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<?> statusJSONSingle(String test) {
		System.out.println("Run one test");
		
		try {
			return machine.statusObject(test);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * assumes access to all machines
	 * gets the aggregated statuses of all machines, urls, dbs, etc...
	 * should check for connection to all servers first before checking server specific connections
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<?> statusJSON(@QueryParam("machine") List<String> machines,  @QueryParam("test") String test, @QueryParam("index") int index) {
		if(test != null && !test.equals("")) {
			return statusJSONSingle(test);
		} else if(machines == null || machines.size() == 0) {
			return statusJSON();
		} else {
		
			System.out.println(machines.size());
			
			try {
				//under certain conditions all machine tests should be returned instead...
				if(index == 0 || (machines.size() == 1 && machines.get(0).toLowerCase().equals("all"))) {
					return machine.statusObject(machines);
				}
				return machine.statusObject(machines, index);
			} catch(NoMoreTestsException e) {  //this catches any exception where there are no more tests to do for a particular machine and reports to the client
				ArrayList<ErrorMessage> errorList = new ArrayList<ErrorMessage>(1);
				ErrorMessage error = new ErrorMessage("NoMoreTests");
				errorList.add(error);
				return errorList;
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/*
	 * Get list of machines (NOTE:  not key-value pairs, but returning an appropriate list...)
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/machinelist")
	public List<String> machineList() {	
		return loader.getMachineNames();
	}
	
	/*
	 * Get list of test names (NOTE:  not key-value pairs, but returning an appropriate list...)
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/testnamelist")
	public List<Map<String,Object>> testNameList() {	
		return loader.getTestNames();
	}
}
