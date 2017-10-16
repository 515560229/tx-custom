package edu.ldcollege.tx.custom.tx;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Import(CustomTransactionManagementConfigurationSelector.class)
public @interface CustomTransactionManagement {
	
	AdviceMode mode() default AdviceMode.PROXY;

}
