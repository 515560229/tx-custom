package edu.ldcollege.tx.annotation.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ldcollege.tx.domain.Course;

@Service
public class UserService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Transactional
	public List<Course> queryUser() {
		logger.info("queryUser");
		System.out.println(jdbcTemplate.getClass());
		return jdbcTemplate.query("select * from tbl_course", new BeanPropertyRowMapper<Course>(Course.class));
	}
	
}
