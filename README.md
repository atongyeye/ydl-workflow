# workflow V1.0

ydl-workflow基于SAAS服务,完美整合springboot + activiti5 + MyBatis 通用 Mapper + 分页插件 PageInfo!

# 说明

本项目 fork 自 [peter.an](https://github.com/atongyeye) 的 [workflow](https://github.com/atongyeye/workflow.git)！


项目引入了下面依赖：
```xml
<dependency>
		<groupId>org.activiti</groupId>
		<artifactId>activiti-spring-boot-starter-basic</artifactId>
		<version>5.22.0</version>
</dependency>


# 扩展说明

1. 增加了审批配置，业务表单，审批抄送人，操作流程等扩展表

2 支持流程会签，驳回，消息通知审批人

3  生成了节点实体类，支持用户原生生成流程定义

4 支持扩展用户自定义流程


## 功能简介
1. 流程定义
2. 流程发布
3. 流程审批
4. 流程驳回
5. 查询待办任务
6. 查询审批历史
7. 查询流程定义



### 如何启动项目
workflow目前支持三种启动方式:
1. 在IDE里运行Application类中的main方法启动
2. 执行如下maven命令
```
clean package -Dmaven.test.skip=true
```
并从target目录中找到ydl-workflow-biz-1.0.0-SNAPSHOT.jar,并在jar包的目录下执行如下java命令
```
java -jar ydl-workflow-biz-1.0.0-SNAPSHOT.jar
```
3. 修改pom.xml中如下片段
```
<packaging>jar</packaging>
```
改为
```
<packaging>war</packaging>
```
并打包放入到tomcat中执行

### 注意
最新版项目最低支持jdk1.7

## 所用框架
1. activiti v5.2
2. jdk1.7 
3. mysql v5.5
4. springcloud v4.0
5. pagehelper v4.1.6



## 项目包结构说明
```
├─main
│  │  
│  ├─java
│  │   │
│  │   ├─com.yidouinc.ydl.workflow----------------项目主代码
│  │   │          │
│  │   │          ├─domain----------------项目实体类
│  │   │          │
│  │   │          ├─config----------------项目配置代码
│  │   │          │
│  │   │          ├─filter----------------项目过滤器
│  │   │          │
│  │   │          ├─mapper----------------项目映射数据库层
│  │   │          │
│  │   │          ├─service类----------------项目业务逻辑
│  │   │          │
│  │   │          └─Application类----------------以main方法启动springboot的类
│  │   │
│  │   └─generator----------------mybatis-plus Entity生成器
│  │
│  ├─resources----------------项目资源文件
│        │
│        ├─bpmn----------------流程定义文件
│        │ 
│        ├─application.properties----------------springboot项目配置
│        │ 
│        ├─mapper.xml----------------项目实体数据库映射文件
│   	 │
│        └─disconf.xml----------------项目disconf配置文件
│  
│  
```
注:SpringBoot项目默认不支持将静态资源和模板(web页面)放到webapp目录,但是个人感觉resources目录只放项目的配置更加简洁.

## 基于springboot启动方式

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages="com.yidouinc")
@ImportResource({"classpath:disconf.xml"})
public class Application {
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		loadLogBack();
	}

	
## 流程启动代码

/**
	 * 启动流程
	 * 
	 * @param dto
	 * @return
	 */
	public String startWorkflow(ProcInstanceDto dto) {
		ProcessInstance processInstance = null;
		if (dto.getStartUserId() != null) {
			identityService.setAuthenticatedUserId(dto.getStartUserId().toString());
		}
		OperResult result = actOperatingFormService.saveOperatingForm(dto);
		dto.setOperatingFormId(result.getId());
		String companyId = String.valueOf(dto.getCompanyId());
		processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(dto.getProcDefKey(), dto.getBusinessType() + "-" + dto.getBusinessKey(),
				companyId);
		dto.setProcInstId(processInstance.getId());
		workflowBusinessService.saveWorkflowRelateInfo(dto);
		return processInstance.getId();
	}
	
	
## 流程业务类注册
@Autowired
	private DataSource dataSource;
	
	@Autowired
	private DataSourceTransactionManager dataSourceTransactionManager;
	
	@Bean(name = "processEngineConfiguration")
	public SpringProcessEngineConfiguration getProcessEnginConfiguration() {
		SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
		processEngineConfiguration.setDataSource(dataSource);
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

