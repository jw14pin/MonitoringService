package com.dao.connector;

import org.springframework.jdbc.core.JdbcTemplate;

public class Connector {

	private JdbcTemplate jdbcTemplatePortal;

	public JdbcTemplate getJdbcTemplatePortal() {
		return jdbcTemplatePortal;
	}
	public void setJdbcTemplatePortal(JdbcTemplate jdbcTemplatePortal) {
		this.jdbcTemplatePortal = jdbcTemplatePortal;
	}

}
