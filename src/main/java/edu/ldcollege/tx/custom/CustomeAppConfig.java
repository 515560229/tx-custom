package edu.ldcollege.tx.custom;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.Propagation;

import edu.ldcollege.tx.custom.tx.CustomTransactionManagement;
import edu.ldcollege.tx.custom.tx.CustomTransactionManagement.TransactionAttribute;

@PropertySource("classpath:jdbc.properties")
@Configuration
@ComponentScan
@CustomTransactionManagement(expression = "execution(* edu.ldcollege.tx.custom.service.*.*(..))", transactionAttributes = {
		@TransactionAttribute(methodName = "query*", propagation = Propagation.REQUIRED),
		})
public class CustomeAppConfig {
	@Value("${jdbc.url}")
	private String url;
	@Value("${jdbc.password}")
	private String password;
	@Value("${jdbc.username}")
	private String username;
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
	
	@Bean
	public DataSourceTransactionManager txManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
