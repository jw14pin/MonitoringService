package com.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.concurrent.DeterministicExecutor;
import org.jmock.lib.concurrent.Synchroniser;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.dao.CommonDAO;
import com.models.Command;
import com.models.Status;
import com.resources.Chain;

/*
 * Unit test suite for the chain class (it should allow us to test the chain class without implementing command or any DAO).
 * Note this is a little unorthodox since the Chain class calls the URLHandler class which calls the command methods.  It would usually be a direct call to another class.
 */
public class CommandsTest extends TestCase {
	private Mockery context;
	private Chain chain;
	
	@BeforeTest
  	public void beforeTest() {
		context = new Mockery() {{
			setThreadingPolicy(new Synchroniser());
		}};
		chain = new Chain();
  	}

  	@AfterTest
  	public void afterTest() {
  		
  	}
	
	@Test
	public void testOneCommandExecuted() {
		//set up
		final Command oneCommand = context.mock(Command.class, "oneCommand");
		
		DeterministicExecutor executor = new DeterministicExecutor();
		chain.setExecutor(executor);
		List<Command> commands = new ArrayList<Command>();
		commands.add(oneCommand);
		
		//expectation is that Command.execute() will be called
		context.checking(new Expectations() {
			{
				oneOf (oneCommand).setDriver(with(any(WebDriver.class)));
				oneOf (oneCommand).execute(); will(returnValue(true));	//how to pass in the same object parameters? Solution:  pass in the parameter two times, once for adding it to the object where the method is called and once for passing it to the method.
			}
		});
		
		chain.statusObject("machine", "name", commands, new ArrayList<Status>());
		
		executor.runUntilIdle();
		
		//verify
		context.assertIsSatisfied();
	}
	
	//xTODO (command should be a hollow implementation the unit tests are for the Chain class, not the Command class):  make each command instance an actual command implementation type so that each command is executed instead of just one null command.
	@Test
	public void testManyCommandsExecuted() {
		//set up
		final Command command1 = context.mock(Command.class, "command1");
		final Command command2 = context.mock(Command.class, "command2");
		final Command command3 = context.mock(Command.class, "command3");
		final Command command4 = context.mock(Command.class, "command4");
		final Command command5 = context.mock(Command.class, "command5");
		
		DeterministicExecutor executor = new DeterministicExecutor();
		chain.setExecutor(executor);
		List<Command> commands = new ArrayList<Command>();
		commands.add(command1);
		commands.add(command2);
		commands.add(command3);
		commands.add(command4);
		commands.add(command5);
		
		//expectation is that Command.execute() will be called
		context.checking(new Expectations() {
			{
				oneOf (command1).setDriver(with(any(WebDriver.class)));
				oneOf (command1).execute(); will(returnValue(true));	//we expect that only one of these will execute since they are null commands so the first one will be false and none of the others will execute (condition: false)
				oneOf (command2).setDriver(with(any(WebDriver.class)));
				//once execute is implemented, then the expectations will change because all commands here should return true
				oneOf (command2).execute(); will(returnValue(true));
				oneOf (command3).setDriver(with(any(WebDriver.class)));
				//once execute is implemented, then the expectations will change because all commands here should return true
				oneOf (command3).execute(); will(returnValue(true));
				oneOf (command4).setDriver(with(any(WebDriver.class)));
				//once execute is implemented, then the expectations will change because all commands here should return true
				oneOf (command4).execute(); will(returnValue(true));
				oneOf (command5).setDriver(with(any(WebDriver.class)));
				//once execute is implemented, then the expectations will change because all commands here should return true
				oneOf (command5).execute(); will(returnValue(true));
				
				//this sequence of expectations only occurs on return false.  Returning true will call execute() command again
			}
		});
		
		
		chain.statusObject("machine", "name", commands, new ArrayList<Status>());
		
		executor.runUntilIdle();
		
		//verify
		context.assertIsSatisfied();
	}
	
	//(to be done later) test that commands will be executed in the correct order
	@Test
	public void testExecutionOrder() {
		
	}
	
	//test inserting a command
	@Test
	public void testInsertCommand() {
		//expect that baseDAO.insert method will be called once. Ignore SQL exceptions thrown or verify one is thrown.  Just testing if the method will be called with valid parameters.
		final CommonDAO baseDAO = context.mock(CommonDAO.class, "insert");
		
		
		DeterministicExecutor executor = new DeterministicExecutor();
		chain.setExecutor(executor);
		chain.setBaseDAO(baseDAO);
		
		//the expectations below cannot be fulfilled since not referencing the same baseDAO object.  Need to manually inject object...as shown above...
		context.checking(new Expectations() {
			{
				oneOf (baseDAO).insert(with(any(String.class)), with(any(Object[].class))); will(returnValue(1));  //the only non-static dependency is the DAO.  The Parser is a static class and so cannot be tested directly.
			}
		});
		
		chain.insertObject("testType", "testMachine", "testName", "verify something", "0");
		
		executor.runUntilIdle();
		
		//verify
		context.assertIsSatisfied();
	}
	
	//test inserting a command and getting an SQL error is handled properly (this can only happen if the baseDAO is implemented.  Therefore this is not a realistic test.
	@Test
	public void testInsertCommandSQLError() {
		//expect that baseDAO.insert method will be called once with an SQL exception due to bad parameter.
		final CommonDAO baseDAO = context.mock(CommonDAO.class, "insertSQLError");
		
		
		DeterministicExecutor executor = new DeterministicExecutor();
		chain.setExecutor(executor);
		chain.setBaseDAO(baseDAO);
		
		//expectation is that Command.execute() will be called
		context.checking(new Expectations() {
			{
				oneOf (baseDAO).insert(with(any(String.class)), with(any(Object[].class)));  //the only non-static dependency is the DAO.  The Parser is a static class and so cannot be tested directly.
				
			}
		});
		
		String returnValue = chain.insertObject("*", "*", "*", "verify something", "0");
		
		executor.runUntilIdle();
		
		//verify that all expectations are met.
		context.assertIsSatisfied();
		
		//assert that there is an exception thrown
		assert(!returnValue.equals("true"));
	}
	
	//test inserting a command and getting a command parse error is handled properly (this can only be done after Parser is created...)
	//this test should be reserved for the Parser class
	@Test
	public void testInsertCommandParseError() {
		//expect that a parse exception will be returned and therefore no mocks needed here.
		
	}
	
	
	@Test
	public void testInsertCommandJSONException() {
		//expect that a JSON exception will be returned and so no mocks here either.
		
	}
	
}
