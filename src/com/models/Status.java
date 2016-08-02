package com.models;


public class Status {
	private String machine;
	private String type;
	private String name;
	private String status;
	
	public Status(String machine, String type, String name, String status) {
		super();
		this.machine = machine;
		this.type = type;
		this.name = name;
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMachine() {
		return machine;
	}
	public void setMachine(String machine) {
		this.machine = machine;
	}	
	
}
