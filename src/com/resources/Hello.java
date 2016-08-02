package com.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class Hello {
	//Called if TEXT PLAIN is requested
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello, world!";
	}
	
	//Called if XML is requested
	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXMLHello() {
		return "<?xml version=\"1.0\"?>"+"<hello>Hello, World!"+"</hello>";
	}
	
	//Called if HTML is requested
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html><head><title>Hello World</title></head><body><h1>Hello World</h1></body></html>";
	}
	
	//Called if JSON is requested
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String sayJSONHello() {
		return "{\"Message\":\"Hello, World!\"}";
	}
}
