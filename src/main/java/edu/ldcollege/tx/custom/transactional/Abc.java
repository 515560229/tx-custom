package edu.ldcollege.tx.custom.transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class Abc {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public void transactionMethod1() {
		logger.info("transactionMethod1 throw RuntimeException. It must be rollback");
		if (5 / 0 == 1) {
			throw new RuntimeException("");
		}
	}
	
	@Transactional(noRollbackFor = RuntimeException.class)
	public void transactionMethod2() {
		logger.info("transactionMethod1 throw RuntimeException. It must be commit");
		if (5 / 0 == 1) {
			throw new RuntimeException("");
		}
	}
	
}
