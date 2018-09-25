/**
 * 
 */
package com.yidouinc.ydl.workflow.config;

import javax.sql.DataSource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author JinMing
 *
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ActivitiConfig {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private DataSourceTransactionManager dataSourceTransactionManager;
	
	@Bean(name = "processEngineConfiguration")
	public SpringProcessEngineConfiguration getProcessEnginConfiguration() {
		SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
		processEngineConfiguration.setDataSource(dataSource);
//		processEngineConfiguration.setTransactionManager(new DataSourceTransactionManager(dataSource));
		processEngineConfiguration.setTransactionManager(dataSourceTransactionManager);
		processEngineConfiguration.setDatabaseSchemaUpdate("true");
		processEngineConfiguration.setJobExecutorActivate(false);
		return processEngineConfiguration;
	}
	
	@Bean(name = "processEngine")
	public ProcessEngineFactoryBean getProcessEngineFactoryBean() {
		ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
		processEngineFactoryBean.setProcessEngineConfiguration(this.getProcessEnginConfiguration());
		return processEngineFactoryBean;
	}
	
	@Bean(name = "repositoryService")
	public RepositoryService getRepositoryService() {
		return this.getProcessEnginConfiguration().getRepositoryService();
	}
	
	@Bean(name = "runtimeService")
	public RuntimeService getRuntimeService() {
		return this.getProcessEnginConfiguration().getRuntimeService();
	}
	
	@Bean(name = "taskService")
	public TaskService getTaskService() {
		return this.getProcessEnginConfiguration().getTaskService();
	}
	
	@Bean(name = "historyService")
	public HistoryService getHistoryService() {
		return this.getProcessEnginConfiguration().getHistoryService();
	}
	
	@Bean(name = "managementService")
	public ManagementService getManagementService() {
		return this.getProcessEnginConfiguration().getManagementService();
	}
	
	@Bean(name = "identityService")
	public IdentityService getIdentityService() {
		return this.getProcessEnginConfiguration().getIdentityService();
	}
	

//	@Override
//	@Bean
//	public PlatformTransactionManager annotationDrivenTransactionManager() {
//		return new DataSourceTransactionManager(dataSource);
//	}

}
