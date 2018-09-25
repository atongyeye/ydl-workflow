/**
 * 
 */
package com.yidouinc.ydl.workflow.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.yidouinc.ydl.workflow.dto.ProcDefDto;
import com.yidouinc.ydl.workflow.dto.ProcInstanceDto;
import com.yidouinc.ydl.workflow.query.WorkflowQuery;

/**
 * @author angq
 *
 */
@Service
@Transactional
public class WorkflowRepositoryService {

	private static Logger log = LoggerFactory.getLogger(WorkflowRepositoryService.class);

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private ProcessEngineConfiguration processEngineConfiguration;


	/**
	 * 获取公司所有流程定义
	 * 
	 * @param companyId
	 * @return
	 */
	public List<ProcDefDto> getProcessDefinitionByCompany(long companyId) {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(String.valueOf(companyId))
				.latestVersion().list();
		if (CollectionUtils.isNotEmpty(list)) {
			return null;
		}
		List<ProcDefDto> procDefList = new ArrayList<ProcDefDto>();
		for (ProcessDefinition pd : list) {
			ProcDefDto pdDto = new ProcDefDto();
			pdDto.setId(pd.getId());
			pdDto.setKey(pd.getKey());
			pdDto.setName(pd.getName());
			pdDto.setCompanyId(Long.valueOf(pd.getTenantId()));
			pdDto.setVersion(pd.getVersion());
			procDefList.add(pdDto);
		}
		return procDefList;
	}

	/**
	 * 
	 * @param deployPath
	 * @return
	 * @return String 返回类型
	 */
	public String deployProcess(ProcDefDto dto) {

		DeploymentBuilder deployment = repositoryService.createDeployment();
		if (dto.getCompanyId() != null) {
			deployment.tenantId(dto.getCompanyId().toString());
		}
		String deployId = this.deployProcessByInputStream(dto);
		return deployId;
	}

	public String deployProcessFile(ProcDefDto dto, InputStream inStream) throws IOException {
		DeploymentBuilder deployment = repositoryService.createDeployment();
		if (dto.getCompanyId() != null) {
			deployment.tenantId(dto.getCompanyId().toString());
		}
		String deployId = deployment.addInputStream(dto.getDeployPath(), inStream).deploy().getId();
		return deployId;
	}

	/**
	 * 得到流程定义
	 * 
	 * @param processDefinitionId
	 * @return
	 * @return ProcessDefinitionEntity 返回类型
	 */
	public ProcessDefinitionEntity getProceDefById(String processDefinitionId) {
		return (ProcessDefinitionEntity) ((RepositoryService) repositoryService).getProcessDefinition(processDefinitionId);
	}

	/**
	 * 查询流程定义列表
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageInfo<ProcDefDto> queryProcessList(WorkflowQuery query) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
		if (query.getCompanyId() != null) {// 设置租户过滤
			processDefinitionQuery.processDefinitionTenantId(query.getCompanyId().toString());
		}
		List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();
		List<ProcDefDto> pds = new ArrayList<ProcDefDto>();
		if (CollectionUtils.isNotEmpty(processDefinitionList)) {
			for (ProcessDefinition processDefinition : processDefinitionList) {
				ProcDefDto pd = new ProcDefDto();
				pd.setId(processDefinition.getId());
				pd.setName(processDefinition.getName());
				pd.setKey(processDefinition.getKey());
				pd.setVersion(processDefinition.getVersion());
				pd.setCompanyId(query.getCompanyId());
				pd.setDeployPath(processDefinition.getResourceName());
				pd.setDeploymentId(processDefinition.getDeploymentId());
				pd.setDescription(processDefinition.getDescription());
				pds.add(pd);
			}
		}

		PageInfo page = new PageInfo(processDefinitionList);
		page.setList(pds);
		return page;
	}

	/**
	 * 获取流程信息
	 */
	public List<Map<String, String>> getWorkFlowList(Long companyId) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			List<ProcDefDto> proList = this.getProcessDefinitionByCompany(companyId);
			for (ProcDefDto pro : proList) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("proKey", pro.getKey());
				map.put("proName", pro.getName());
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询流程列表，我发起的流程实例数
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageInfo<ProcDefDto> getWorkFlowMyPage(WorkflowQuery query) {
		List<ProcDefDto> proList = this.getProcessDefinitionByCompany(query.getCompanyId());
		PageInfo page = new PageInfo(proList);
		int total = (int) getProcessDefinitionCountByCompany(query.getCompanyId());
		page.setTotal(total);
		page.setPageNum(query.getCurrentPage());
		return page;
	}

	private long getProcessDefinitionCountByCompany(long companyId) {
		long count = repositoryService.createProcessDefinitionQuery().processDefinitionTenantId(String.valueOf(companyId)).latestVersion().count();
		return count;
	}

	public String deployProcessByInputStream(ProcDefDto dto) {
		String deployId = "";
		DeploymentBuilder deployment = repositoryService.createDeployment();
		if (dto.getCompanyId() != null) {
			deployment.tenantId(dto.getCompanyId().toString());
		}
		String deployPath = dto.getDeployPath();
		File file = new File(deployPath);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			DeploymentBuilder deploy = deployment.addInputStream(file.getName(), is);
			deployId = deploy.deploy().getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deployId;
	}

	public ProcessInstance getProcessInstanceById(String processInstanceId) {
		return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	}

	/**
	 * 
	 * 获得流程图
	 * 
	 * @param processInstanceId
	 * @return
	 * @return byte[] 返回类型
	 */
	public byte[] getProcessInstanceDiagram(ProcInstanceDto dto) {
		ProcessInstance processInstance = getProcessInstanceById(dto.getProcInstId());

		ProcessDefinitionEntity pde = (ProcessDefinitionEntity) ((RepositoryService) repositoryService)
				.getProcessDefinition(processInstance.getProcessDefinitionId());

		if (pde != null && pde.isGraphicalNotationDefined()) {
			BpmnModel bpmnModel = repositoryService.getBpmnModel(pde.getId());
			ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
			InputStream resource = diagramGenerator.generateDiagram(bpmnModel, "png", runtimeService.getActiveActivityIds(processInstance.getId()),
					Collections.<String> emptyList(), "宋体", "宋体", "宋体", processEngineConfiguration.getClassLoader(), 1.0);

			try {
				byte[] responseBytes = IOUtils.toByteArray(resource);
				return responseBytes;

			} catch (Exception e) {
				log.error("Error exporting diagram", e);
			}
		}
		return null;
	}

	public boolean deployProcessFile(String filepath, String fileName, Long companyId) {
		InputStream fileInputStream = null;
		try {

			File file = new File(filepath);
			// 配置文件指向系统文件
			if (file.isAbsolute() && file.exists()) {
				fileInputStream = new FileInputStream(filepath);
			} else {
				// 文件名指向资源文件
				fileInputStream = getClass().getClassLoader().getResourceAsStream(filepath);
			}
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				if (companyId != null) {
					repositoryService.createDeployment().tenantId(String.valueOf(companyId)).addZipInputStream(zip).deploy();
				} else {
					repositoryService.createDeployment().addZipInputStream(zip).deploy();
				}

			} else {
				if (companyId == null) {
					repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
				} else {
					repositoryService.createDeployment().tenantId(String.valueOf(companyId)).addInputStream(fileName, fileInputStream).deploy();
				}

			}
		} catch (Exception e) {
			log.error("error on deploy process, because of file input stream", e);
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 删除流程
	 */
	public void delDeployment(String ids) {
		String[] id = ids.split(",");
		for (String processDefinitionId : id) {
			// 级联删除,会删除和当前规则相关的所有信息，包括历史
			ProcessDefinition pd = repositoryService.getProcessDefinition(processDefinitionId);
			if (pd != null) {
				repositoryService.deleteDeployment(pd.getDeploymentId());
				// repositoryService.deleteDeployment(pd.getDeploymentId(),
				// true);
			}
		}
		// // 普通删除，如果当前规则下有正在执行的流程，则抛异常
		// repositoryService.deleteDeployment(deploymentId);
	}

	/**
	 * 物理删除流程
	 * 
	 * @param procDefId
	 */
	public void removeDeployment(String procDefId) {
			// 级联删除,会删除和当前规则相关的所有信息，包括历史
			ProcessDefinition pd = repositoryService.getProcessDefinition(procDefId);
			if (pd != null) {
				 repositoryService.deleteDeployment(pd.getDeploymentId(),
				 true);
			}
	}
}
