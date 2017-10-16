package edu.ldcollege.tx.xml.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import edu.ldcollege.tx.domain.Course;

public class UserService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Course> queryUser() {
		logger.info("queryUser");
		return jdbcTemplate.query("select * from tbl_course", new BeanPropertyRowMapper<Course>(Course.class));
	}
	
}
