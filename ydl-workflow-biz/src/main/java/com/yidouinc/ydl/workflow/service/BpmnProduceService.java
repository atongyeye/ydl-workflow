package com.yidouinc.ydl.workflow.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yidouinc.ydl.workflow.bpmnDto.BpmnEndEvent;
import com.yidouinc.ydl.workflow.bpmnDto.BpmnEventListener;
import com.yidouinc.ydl.workflow.bpmnDto.BpmnExclusiveGateway;
import com.yidouinc.ydl.workflow.bpmnDto.BpmnModleDto;
import com.yidouinc.ydl.workflow.bpmnDto.BpmnMultiCharInfo;
import com.yidouinc.ydl.workflow.bpmnDto.BpmnParallelGateWay;
import com.yidouinc.ydl.workflow.bpmnDto.BpmnSequenceFlow;
import com.yidouinc.ydl.workflow.bpmnDto.BpmnStartEvent;
import com.yidouinc.ydl.workflow.bpmnDto.BpmnUserTask;
import com.yidouinc.ydl.workflow.dto.ProcDefDto;

/**
 * @author kk
 *
 */
@Service
public class BpmnProduceService {

	// log日志
	private static Logger logger = Logger.getLogger(BpmnProduceService.class);

	@Autowired
	WorkflowRepositoryService wfRepositoryService;

	/**
	 * 生成bpmn文件
	 * 
	 * @param modelDto
	 */
	public void produceBpmnJson(BpmnModleDto modelDto, Long companyId) {
		/** 第一步:初始化bpmnModel 与 Process **/
		BpmnModel model = new BpmnModel();
		Process process = new Process();

		/** 第二步:参数塞值 **/
		process.setId(modelDto.getProcessId());
		process.setName(modelDto.getProcessName());
		// 设置开始节点
		process.addFlowElement(createStartEvent(modelDto.getStartEvent()));
		// 设置结束节点
		process.addFlowElement(createEndEvent(modelDto.getEndEvent()));

		// 设置userTask
		List<BpmnUserTask> usertaskList = modelDto.getUsertaskList();
		for (BpmnUserTask userTask : usertaskList) {
			process.addFlowElement(createUserTask(userTask));
		}

		// 设置连接线
		List<BpmnSequenceFlow> flowList = modelDto.getFlowList();
		for (BpmnSequenceFlow seFlow : flowList) {
			process.addFlowElement(createSequenceFlow(seFlow));
		}

		// 设置排他网关
		List<BpmnExclusiveGateway> exclusiveGatewayList = modelDto.getExclusiveGatewayList();
		if (CollectionUtils.isNotEmpty(exclusiveGatewayList)) {
			for (BpmnExclusiveGateway beGateWay : exclusiveGatewayList) {
				process.addFlowElement(createExclusiveGateway(beGateWay));
			}
		}

		// 设置并行网关
		List<BpmnParallelGateWay> parGateWayList = modelDto.getParGateWayList();
		if (CollectionUtils.isNotEmpty(parGateWayList)) {
			for (BpmnParallelGateWay bpGateWay : parGateWayList) {
				process.addFlowElement(createParallelGateWay(bpGateWay));
			}
		}
		model.addProcess(process);

		// 生成bpmn的xml文件
		modelToXml(model, modelDto.getProcessName(), companyId);

		// //设置坐标信息
		// List<BpmnGraphInfo> graphList = modelDto.getGraphList();
		// if(CollectionUtils.isNotEmpty(graphList)){
		// for(BpmnGraphInfo bgInfo:graphList){
		// process.addFlowElement(createGraphicInfo(bgInfo));
		// }
		// }

	}

	/**
	 * 设置起始节点
	 * 
	 * @return
	 */
	public static StartEvent createStartEvent(BpmnStartEvent bsEvent) {
		StartEvent startEvent = new StartEvent();
		startEvent.setId(bsEvent.getId());
		startEvent.setName(bsEvent.getName());
		// startEvent.setInitiator(bsEvent.getInitiator());
		startEvent.setInitiator("applyUserId");
		return startEvent;
	}

	/**
	 * 设置结束节点
	 * 
	 * @return
	 */
	public static EndEvent createEndEvent(BpmnEndEvent beEvent) {
		EndEvent endEvent = new EndEvent();
		endEvent.setId(beEvent.getId());
		endEvent.setName(beEvent.getName());
		List<ActivitiListener> list = new ArrayList<ActivitiListener>();
		if (CollectionUtils.isNotEmpty(beEvent.getLisenerList())) {
			for (BpmnEventListener task : beEvent.getLisenerList()) {
				ActivitiListener listener = new ActivitiListener();
				listener.setEvent(task.getEvent());
				// Spring配置以变量形式调用无法写入，只能通过继承TaskListener方法，
				listener.setImplementationType("class");
				listener.setImplementation(task.getClassName());
				list.add(listener);
			}
			endEvent.setExecutionListeners(list);
		}
		return endEvent;
	}

	/**
	 * 创建节点任务 使用监听设置处理人 *
	 * 
	 * @param id
	 *            任务id标识 * @param name 任务名称 * @param taskListenerList
	 *            监听的集合,TaskListener实现类的的具体路径例：com.sky.bluesky.activiti.utils.
	 *            MangerTaskHandlerCandidateUsers
	 * @return
	 */
	public static UserTask createUserTask(BpmnUserTask buTask) {
		UserTask userTask = new UserTask();
		userTask.setId(buTask.getId());
		userTask.setName(buTask.getName());
		String[] candidateUsers = buTask.getAssignee();
		if (candidateUsers.length > 1) {
			userTask.setCandidateUsers(Arrays.asList(candidateUsers));
		} else if (candidateUsers.length == 1) {
			userTask.setAssignee(candidateUsers[0]);
		}
		if (buTask.getDocumentation() != null) {
			userTask.setDocumentation(buTask.getDocumentation());
		}
		List<ActivitiListener> list = new ArrayList<ActivitiListener>();
		if (CollectionUtils.isNotEmpty(buTask.getLisenerList())) {
			for (BpmnEventListener task : buTask.getLisenerList()) {
				ActivitiListener listener = new ActivitiListener();
				listener.setEvent(task.getEvent());
				// Spring配置以变量形式调用无法写入，只能通过继承TaskListener方法，
				listener.setImplementationType("class");
				listener.setImplementation(task.getClassName());
				list.add(listener);
			}
			userTask.setTaskListeners(list);
		}
		if (buTask.getCharInfo() != null) {
			MultiInstanceLoopCharacteristics mlc = new MultiInstanceLoopCharacteristics();
			BpmnMultiCharInfo bmCharInfo = buTask.getCharInfo();
			mlc.setId(bmCharInfo.getUserTaskId());
			mlc.setElementVariable(bmCharInfo.getElementVariable());
			mlc.setSequential(Boolean.parseBoolean(bmCharInfo.getSequential()));
			if (bmCharInfo.getInputDataItem() != null) {
				mlc.setInputDataItem(bmCharInfo.getInputDataItem());
			}
			if (bmCharInfo.getCondition() != null) {
				mlc.setCompletionCondition(bmCharInfo.getCondition());
			}
			userTask.setLoopCharacteristics(mlc);
		}
		return userTask;
	}

	/**
	 * 设置连线
	 * 
	 * @param from
	 *            从哪里出发
	 * @param to
	 *            连接到哪里
	 * @return
	 */
	public static SequenceFlow createSequenceFlow(BpmnSequenceFlow bsFlow) {
		SequenceFlow flow = new SequenceFlow();
		flow.setId(bsFlow.getId());
		flow.setName(bsFlow.getName());
		flow.setSourceRef(bsFlow.getSourceRef());
		flow.setTargetRef(bsFlow.getTargetRef());
		if (null != bsFlow.getConditionExpression() && !"".equals(bsFlow.getConditionExpression())) {
			flow.setConditionExpression(bsFlow.getConditionExpression());
		}
		return flow;
	}

	/**
	 * 排他网关节点
	 * 
	 * @param id
	 * @return
	 */
	public static ExclusiveGateway createExclusiveGateway(BpmnExclusiveGateway beGatWay) {
		ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
		exclusiveGateway.setId(beGatWay.getId());
		exclusiveGateway.setName(beGatWay.getName());
		return exclusiveGateway;
	}

	/**
	 * 并行网关节点
	 * 
	 * @param id
	 * @return
	 */
	public static ParallelGateway createParallelGateWay(BpmnParallelGateWay bpGatWay) {
		ParallelGateway parallelGateway = new ParallelGateway();
		parallelGateway.setId(bpGatWay.getId());
		parallelGateway.setName(bpGatWay.getName());
		return parallelGateway;
	}

	/**
	 * 设置坐标信息
	 * 
	 * @param bgInfo
	 * @return
	 */
	// private GraphicInfo createGraphicInfo(BpmnGraphInfo bgInfo) {
	// GraphicInfo graphicInfo = new GraphicInfo();
	// graphicInfo.setX(100);
	// graphicInfo.setY(100);
	// graphicInfo.setWidth(100);
	// graphicInfo.setHeight(100);
	// return graphicInfo;
	// }

	/**
	 * model转xml文件
	 * 
	 * @param model
	 */
	public void modelToXml(BpmnModel model, String bpmnName, Long companyId) {
		// BpmnAutoLayout bal = new BpmnAutoLayout(model);
		// bal.execute();
		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		byte[] convertToXML = bpmnXMLConverter.convertToXML(model);
		// 将xmlStr生成.bpmn文件生成到公共目录
		StringBuffer outputFile = new StringBuffer(System.getProperty("user.dir"));// 转为bpmb文件之后,输出的路径
		outputFile.append("/bpmnTemplate/");
		outputFile.append(bpmnName);// 根据某个规则来定义pdf名字
		outputFile.append(".bpmn");
		boolean boo = true;
		try {
			File file = new File(outputFile.toString());
			File fileParent = file.getParentFile();
			if (!fileParent.exists()) {
				fileParent.mkdirs();
			}
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(outputFile.toString());
			fos.write(convertToXML);
			fos.close();
		} catch (IOException e1) {
			boo = false;
			logger.info("创建bpmn的file出错！");
		}
		// 保存路径到数据库中
		if (boo) {
			ProcDefDto dto = new ProcDefDto();
			dto.setCompanyId(companyId);
			dto.setDeployPath(outputFile.toString());
			String deployId = wfRepositoryService.deployProcessByInputStream(dto);
			if (StringUtils.isNotBlank(deployId)) {
				logger.info(String.format("工作流{%s}启动成功", deployId));
			} else {
				logger.error(String.format("工作流部署失败,返回的deployId值为{%s}", deployId));
			}
		}
	}
}
