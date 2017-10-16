package edu.ldcollege.tx.custom.tx;

import java.lang.reflect.Method;

import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttribute;

@SuppressWarnings("serial")
public class CustomTransactionAttributeSource extends AnnotationTransactionAttributeSource {
	private NameMatchTransactionAttributeSource attributeSource;
	public CustomTransactionAttributeSource(NameMatchTransactionAttributeSource attributeSource) {
		this.attributeSource = attributeSource;
	}
	@Override
	public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
		TransactionAttribute txAttr = super.getTransactionAttribute(method, targetClass);//从方法注解上获取@Transactional识别
		if (txAttr == null) {
			txAttr = attributeSource.getTransactionAttribute(method, targetClass);
		}
		return txAttr;
	}
}
