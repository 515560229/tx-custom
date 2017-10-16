package edu.ldcollege.tx.custom.tx;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.transaction.annotation.AbstractTransactionManagementConfiguration;
import org.springframework.transaction.config.TransactionManagementConfigUtils;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class CustomTransactionManagementConfiguration extends AbstractTransactionManagementConfiguration {
	
	@Autowired
	private Environment environment;
	
	@Bean(name = TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME)
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public Advisor transactionAdvisor(TransactionAttributeSource txAttrSource,
			TransactionInterceptor txInterceptor) {
		CustomTransactionAdvisor advisor = new CustomTransactionAdvisor(new CustomTransactionPointcut());
		if (this.enableTx == null) {
			throw new IllegalArgumentException("@CustomTransactionManagement is to be imported ");
		}
//		advisor.setExpression(this.enableTx.getString("expression"));
		advisor.setExpression(environment.getProperty("transaction.pointcut.expression"));
		advisor.setOrder(this.enableTx.<Integer>getNumber("order"));
		advisor.setAdvice(txInterceptor);
		return advisor;
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public TransactionAttributeSource transactionAttributeSource() throws IOException {
		if (this.enableTx == null) {
			throw new IllegalArgumentException("@CustomTransactionManagement is to be imported ");
		}
		NameMatchTransactionAttributeSource nameMatchTransactionAttributeSource = new NameMatchTransactionAttributeSource();
		Properties attrProperties = new Properties();
		attrProperties.load(new ByteArrayInputStream(environment.getProperty("transaction.attributes").getBytes()));
		nameMatchTransactionAttributeSource.setProperties(attrProperties);
		
		return new CustomTransactionAttributeSource(nameMatchTransactionAttributeSource);
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public TransactionInterceptor transactionInterceptor() throws IOException {
		TransactionInterceptor interceptor = new TransactionInterceptor();
		interceptor.setTransactionAttributeSource(transactionAttributeSource());
		if (this.txManager != null) {
			interceptor.setTransactionManager(this.txManager);
		}
		return interceptor;
	}
	
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableTx = AnnotationAttributes
				.fromMap(importMetadata.getAnnotationAttributes(CustomTransactionManagement.class.getName(), false));
		if (this.enableTx == null) {
			throw new IllegalArgumentException("@CustomEnableTransactionManagement is not present on importing class "
					+ importMetadata.getClassName());
		}
	}

}
