package edu.ldcollege.tx.custom.tx;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractGenericPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

@SuppressWarnings("serial")
public class CustomTransactionAdvisor extends AbstractGenericPointcutAdvisor implements BeanFactoryAware {
	
	private CustomTransactionPointcut pointcut;
	
	public CustomTransactionAdvisor(CustomTransactionPointcut pointcut) {
		this.pointcut = pointcut;
	}
	
	public void setExpression(String expression) {
		this.pointcut.setExpression(expression);
	}

	public String getExpression() {
		return this.pointcut.getExpression();
	}

	public void setLocation(String location) {
		this.pointcut.setLocation(location);
	}

	public String getLocation() {
		return this.pointcut.getLocation();
	}

	public void setParameterNames(String... names) {
		this.pointcut.setParameterNames(names);
	}

	public void setParameterTypes(Class<?>... types) {
		this.pointcut.setParameterTypes(types);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.pointcut.setBeanFactory(beanFactory);
	}
	
	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}
	
}
