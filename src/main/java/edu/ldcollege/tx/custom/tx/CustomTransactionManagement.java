package edu.ldcollege.tx.custom.tx;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Import(CustomTransactionManagementConfigurationSelector.class)
public @interface CustomTransactionManagement {
	
	String expression() default "";
	
	TransactionAttribute[] transactionAttributes();
	
	boolean proxyTargetClass() default false;

	AdviceMode mode() default AdviceMode.PROXY;

	int order() default Ordered.LOWEST_PRECEDENCE;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({})
	public @interface TransactionAttribute {
		
		String methodName();
		
		boolean readOnly() default false;
		
		Isolation isolation() default Isolation.DEFAULT;
		
		Propagation propagation() default Propagation.REQUIRED;
		
		Class<? extends Throwable>[] rollbackFor() default {};
		
		Class<? extends Throwable>[] noRollbackFor() default {};
		
	}
}
