package com.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dao.BaseDAO;
import com.dao.CommonDAO;
import com.models.Command;
import com.models.Status;
import com.resources.Chain.ChainThread;
import com.url.URLHandler;

@Component
@Path("/db")
public class DB {
	@Autowired
	private CommonDAO baseDAO;

	class DBThread implements Runnable {
		String machine;
		String db; 
		String connectionString;
		String verifyQuery;
		List<Status> returnList;
		
		DBThread(String machine, String db, String connectionString, String verifyQuery, List<Status> returnList) {
			this.machine = machine;
			this.db = db;
			this.connectionString = connectionString;
			this.verifyQuery = verifyQuery;
			this.returnList = returnList;
		}
		
		//need a new url handler every time to prevent synchronization issues do not occur
		@Override
		public void run() {
			System.out.println("test: "+db+" start");
			returnList.add(new Status(machine, "db", db, baseDAO.select(db, connectionString, "select sysdate from dual")));
			System.out.println("test: "+db+" end");
		}
	}
	
	/*
	 * gets the status for a db
	 */
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public Status statusObject(@QueryParam("machine") String machine, @QueryParam("db") String db) {
//		return new Status(machine, "db", db, baseDAO.select(db, "select sysdate from dual"));
//	}
	
	/*
	 * verify query must always be given by service and not client for security reasons
	 */
	public synchronized void statusObject(String machine, String db, String connectionString, String verifyQuery, List<Status> returnList) {
		Thread thread = new Thread(new DBThread(machine, db, connectionString, verifyQuery, returnList));
		thread.start();
	}
}
