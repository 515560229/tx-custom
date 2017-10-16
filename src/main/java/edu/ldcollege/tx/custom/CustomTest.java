package edu.ldcollege.tx.custom;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ldcollege.tx.custom.service.ExpressionService;
import edu.ldcollege.tx.custom.service.UserService;
import edu.ldcollege.tx.custom.transactional.Abc;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CustomeAppConfig.class})
public class CustomTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserService userService;
	@Autowired
	private ExpressionService eService;
	@Autowired
	private Abc aService;
	
	//transaction result : commit
	@Test
	public void test01() {
		logger.info("=================================");
		try { userService.queryUser(); } catch (Exception ex) {};
		logger.info("=================================");
		try { eService.aUser(); } catch (Exception ex) {};
		logger.info("=================================");
		try { eService.queryUser(); } catch (Exception ex) {};
		logger.info("=================================");
		try { aService.transactionMethod1(); } catch (Exception ex) {};
		logger.info("=================================");
		try { aService.transactionMethod2(); } catch (Exception ex) {};
	}

}
