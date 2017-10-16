package edu.ldcollege.tx.custom.tx;

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.transaction.annotation.Transactional;

public class CustomTransactionPointcut implements Pointcut, MethodMatcher {
	
	private AnnotationClassFilter transactionalClassFilter = new AnnotationClassFilter(Transactional.class);
	private AnnotationMethodMatcher transactionalMethodMatcher = new AnnotationMethodMatcher(Transactional.class);
	private AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
	
	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		boolean match = transactionalClassFilter.matches(targetClass);
		if (!match) {
			match = transactionalMethodMatcher.matches(method, targetClass);
		}
		if (!match) {
			match = aspectJExpressionPointcut.matches(method, targetClass);
		}
		return match;
	}
	
	@Override
	public boolean matches(Method method, Class<?> targetClass, Object... args) {
		boolean match = transactionalClassFilter.matches(targetClass);
		if (!match) {
			match = transactionalMethodMatcher.matches(method, targetClass);
		}
		if (!match) {
			match = aspectJExpressionPointcut.matches(method, targetClass, args);
		}
		return match;
	}

	public String getExpression() {
		return this.aspectJExpressionPointcut.getExpression();
	}

	public void setExpression(String expression) {
		this.aspectJExpressionPointcut.setExpression(expression);
	}

	@Override
	public ClassFilter getClassFilter() {
		return ClassFilter.TRUE;
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return this;
	}

	@Override
	public boolean isRuntime() {
		return false;
	}

	public void setLocation(String location) {
		this.aspectJExpressionPointcut.setLocation(location);
	}

	public String getLocation() {
		return this.aspectJExpressionPointcut.getLocation();
	}

	public void setParameterNames(String... names) {
		this.aspectJExpressionPointcut.setParameterNames(names);
	}

	public void setParameterTypes(Class<?>... types) {
		this.aspectJExpressionPointcut.setParameterTypes(types);
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.aspectJExpressionPointcut.setBeanFactory(beanFactory);
	}
	
}
