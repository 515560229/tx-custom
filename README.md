# Spring约定事务配置与注解方式的完美统一

## 约定事务配置实现

以往约定事务的配置实现,如下xml代码.

```xml
	<tx:advice id="txAdvice" transaction-manager="transactionManager">
		<tx:attributes>
      <tx:method name="update" propagation="REQUIRED" />
			<tx:method name="*" propagation="REQUIRED" />
		</tx:attributes>
	</tx:advice>

	<aop:config>
		<aop:pointcut id="interceptorPointCuts" expression="execution(* edu.ldcollege.tx.xml.service.*.*(..))" />
		<aop:advisor advice-ref="txAdvice" pointcut-ref="interceptorPointCuts" />
	</aop:config>
	
	<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
		<property name="dataSource" ref="dataSource" />
	</bean>
	
	<bean id="userService" class="edu.ldcollege.tx.xml.service.UserService">
		<property name="jdbcTemplate" ref="jdbcTemplate" />
	</bean>
```

## 基于注解事务的实现

1. 开启事务支持. @EnableTransactionManagement
2. 在需要事务的类或方法上添加注解. @Transactional(...)

分析一下以上两种实现
第一种:  约定事务配置. 省去了多个事务类或方法中声明事务及事务属性.如果项目中有多种不同事务实现需求.如: 传播特性或隔离级别不同,就需要配置多个关于attribute的方法策略配置;另外,spring4以后推荐基于注解的配置方式
第二种:  更精细级别的事务配置, 需要在每个事务类或方法上配置.又略显繁琐,加大开发工作量

<h3>有不没有两种方式结合的方式, 并且全注解实现呢.即: 用约定实现大部分事务管理,又能精细化控制到特殊的事务方法的事务属性配置呢??</h3>

## Spring约定事务配置与注解方式的完美统一实现

1. 开启自定义的事务支持.@CustomTransactionManagement
2. 配置通用事务属性和AspectJ表达式
```properties
transaction.pointcut.expression = execution(* edu.ldcollege.tx.custom.service.*.*(..))
#see org.springframework.transaction.interceptor.TransactionAttributeEditor.setAsText(String)
transaction.attributes = \
query* = PROPAGATION_REQUIRED,ISOLATION_DEFAULT\n\
update* = PROPAGATION_REQUIRED,ISOLATION_DEFAULT
```
3. 特殊的事务方法控制使用@Transactional控制

## 关键实现代码说明
1. 自定义注解.开启自定义事务支持
```java
@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Import(CustomTransactionManagementConfigurationSelector.class)
public @interface CustomTransactionManagement {
	
	AdviceMode mode() default AdviceMode.PROXY;

}
```

2. 声明该注解后会自动导入事务相关的基础设施Bean,参见AutoProxyRegistrar和CustomTransactionManagementConfiguration类. 
目前仅支持proxy方式
```java
public class CustomTransactionManagementConfigurationSelector extends
		AdviceModeImportSelector<CustomTransactionManagement> {

	@Override
	protected String[] selectImports(AdviceMode adviceMode) {
		if (adviceMode == AdviceMode.PROXY) {
			return new String[] { AutoProxyRegistrar.class.getName(),
					CustomTransactionManagementConfiguration.class.getName() };
		}
		return null;
	}

}
```
3. CustomTransactionManagementConfiguration
```java
@Configuration
public class CustomTransactionManagementConfiguration extends AbstractTransactionManagementConfiguration {
	
	@Autowired
	private Environment environment;
	
	//事务Aop的Advisor(切面和拦截器)
	@Bean(name = TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME)
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public Advisor transactionAdvisor(TransactionAttributeSource txAttrSource,
			TransactionInterceptor txInterceptor) {
		CustomTransactionAdvisor advisor = new CustomTransactionAdvisor(new CustomTransactionPointcut());
		if (this.enableTx == null) {
			throw new IllegalArgumentException("@CustomTransactionManagement is to be imported ");
		}
		advisor.setExpression(environment.getProperty("transaction.pointcut.expression"));
		advisor.setOrder(this.enableTx.<Integer>getNumber("order"));
		advisor.setAdvice(txInterceptor);
		return advisor;
	}
	
	//声明事务属性Bean
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

	//声明事务方法的拦截器
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
			throw new IllegalArgumentException("@CustomTransactionManagement is not present on importing class "
					+ importMetadata.getClassName());
		}
	}

}
```

4. 事务方法的切面
```java
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
```

