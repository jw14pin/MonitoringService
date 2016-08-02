package com.models;

import java.util.List;

public class MachineStatus {
	private String type;
	private String name;
	private String status;
	private List<Status> resources;
	
	public MachineStatus(String type, String name, String status,
			List<Status> resources) {
		super();
		this.type = type;
		this.name = name;
		this.status = status;
		this.resources = resources;
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
	public List<Status> getResources() {
		return resources;
	}
	public void setResources(List<Status> resources) {
		this.resources = resources;
	}
	
}
