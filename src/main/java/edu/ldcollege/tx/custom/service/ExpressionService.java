package edu.ldcollege.tx.custom.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExpressionService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void queryUser() {
		logger.info("Expression transaction effect and It must be commit.");
	}
	
	public void aUser() {
		logger.info("Expression transaction no effect.");
	}
	
}
