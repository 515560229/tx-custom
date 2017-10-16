package edu.ldcollege.tx.custom.tx;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;

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
