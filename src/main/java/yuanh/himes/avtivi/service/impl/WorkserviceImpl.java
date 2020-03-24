package yuanh.himes.avtivi.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuanh.himes.avtivi.listener.ApproveResultEnum;
import yuanh.himes.avtivi.listener.TaskComment;
import yuanh.himes.avtivi.service.Workservice;
import yuanh.himes.avtivi.vo.TaskVo;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * @Author: guochen
 * @Description:
 * @Date:Created in 1:18 PM 7/3/2020
 */
@Slf4j
@Service
public class WorkserviceImpl implements Workservice {

    private static Logger logger = LoggerFactory.getLogger(WorkserviceImpl.class);

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    @Autowired

    @Resource
    private FormService formService;

    @Override
    public void deploy(String processesPath,String processesName,String processesCategory) {

        //部署流程
        Deployment deployment = repositoryService.createDeployment() //创建一个部署的构造器
                .addClasspathResource(StringUtils.isEmpty(processesPath)?"processes/MyProcess.bpmn":processesPath) //从类路径中添加资源
                .name(StringUtils.isEmpty(processesName)?"工单流程11":processesName) //设置部署的名称
                .category(StringUtils.isEmpty(processesCategory)?"办公类别11":processesCategory) //设置部署的类别
                .deploy();
       log.info("部署ID:"+deployment.getId());
       log.info("部署名称"+deployment.getName());
    }

    @Override
    public ProcessInstance runProcess(String  procsessKey) {
        //String processDefiKey = "ResumeApplyProcess";


        //获取流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(procsessKey);
       log.info("流程执行对象的id:"+processInstance.getId());
       log.info("流程实例id:"+processInstance.getProcessInstanceId());//流程实例id
       log.info("流程定义id:"+processInstance.getProcessDefinitionId());//输出流程定义的id
        return processInstance;
    }

    @Override
    public List<Task> queryTask(String assignee) {
        //创建任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        //查看办理人的任务列表
        List<Task> taskList = taskQuery.taskAssignee(assignee).list();
        if(!taskList.isEmpty()){
             for(Task task:taskList){
                log.info("任务办理人："+task.getAssignee());
                log.info("任务id:" + task.getId());
                log.info("任务名称："+ task.getName());
             }
             return  taskList;
        }
        return new ArrayList<>(16);
    }


    @Override
    public void complieTask(String taskId,TaskVo data) {

        Map<String,Object> var = new HashMap<>();
        var.put("approvers1",data.getApprovers1());
        var.put("approvers2",data.getApprovers2());
        var.put("comment",data.getComment());
        var.put("result",data.getResult());
     //   var.put("taskComments", data.getTaskComments());
       // formService.saveFormData(taskId,var);

                    //退件-->直接结束流程：只删除运行中的实例，历史实例保存记录
            Integer result = data.getResult();
            logger.debug("result:{}", result);
            if(ApproveResultEnum.REFUSE.getId().equals(result)){
                HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery()
                        .taskId(taskId)
                        .singleResult();
                logger.debug("deleteProcessInstance:ProcessInstanceId{}", task.getProcessInstanceId());
                Map<String, TaskComment> taskComments = (Map<String, TaskComment>) taskService.getTaskComments(taskId);
                TaskComment taskComment = new TaskComment();
                taskComment.setAssignee(task.getAssignee());
                taskComment.setResult(ApproveResultEnum.idOf(result).getCode());
                taskComment.setComment(data.getComment());
                taskComments.put(taskId,taskComment);

                Map<String, Object> variables;
                variables = taskComments.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> (Object) entry.getValue()));
                taskService.setVariables(taskId, variables);
                //taskService.setVariable(taskId,"taskComments", variables);
                runtimeService.deleteProcessInstance(task.getProcessInstanceId(), data.getComment());
//                Map<String, TaskComment> taskComments = (Map<String, TaskComment>)task.getProcessVariables().get("taskComments");
//                Integer result1 = data.getResult();
//
//                //添加每个task的审批历史
//                TaskComment taskComment = new TaskComment();
//                taskComment.setAssignee(task.getAssignee());
//                taskComment.setResult(ApproveResultEnum.idOf(result).getCode());
//                taskComment.setComment(data.getComment());
//                //add it to map
//                taskComments.put( taskId, taskComment);
//                //update taskComments
                HistoricProcessInstance process = historyService.createHistoricProcessInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId())
                        .includeProcessVariables()
                        .singleResult();

               log.info("此时获取的变量信息："+ process.getProcessVariables());
            }else{
                taskService.complete(taskId,var);

               log.info("当前任务已执行完");
            }
    }

    @Override
    public List<ProcessDefinition> queryProcessDefination(String processDefikey) {

        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefikey) //流程定义的key
                .latestVersion()
                .orderByProcessDefinitionVersion().desc() //按照版本降序排序
                .list();
        if(!processDefinitionList.isEmpty()){
            for(ProcessDefinition processDefinition: processDefinitionList){
               log.info("流程定义的id:"+ processDefinition.getId());
               log.info("流程定义的Key:"+ processDefinition.getKey());
               log.info("流程定义的版本："+ processDefinition.getVersion());
               log.info("流程定义部署的id:"+ processDefinition.getDeploymentId());
               log.info("流程定义的名称:"+processDefinition.getName());
            }
            return processDefinitionList;
        }
        return new ArrayList<>(16);

    }

    @Override
    public List<ProcessInstance> queryProcessInstanceState() {
        //获取所有流程实例
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery()
                .list();
        if(!processInstanceList.isEmpty()){
            for(ProcessInstance processInstance: processInstanceList){
               log.info("当前正在运行的流程实例");
               log.info("该实例id："+processInstance.getProcessInstanceId());
               log.info("该实例名字："+processInstance.getName());
               log.info("该实例对象id:"+processInstance.getId());
            }
            return processInstanceList;
        }
        else {
                   log.info("当前无流程实例");
                    return new ArrayList<>(16);
        }
    }

    @Override
    public void deleteProcessDefi(String deploymentId) {
       repositoryService.deleteDeployment(deploymentId);
    }

    @Override
    public List<HistoricProcessInstance> queryHistoryProcInst() {
        List<HistoricProcessInstance> list = historyService
                .createHistoricProcessInstanceQuery()
                .list();
        if(!list.isEmpty()){
            for(HistoricProcessInstance temp:list){
               log.info("历史流程实例id:" + temp.getId());
               log.info("历史流程定义的id:"+ temp.getProcessDefinitionId());
               log.info("历史流程实例开始时间--结束时间:"+ temp.getStartTime() + "-->"+ temp.getEndTime());
            }
            return list;
        }
        return new ArrayList<>(16);

    }

    @Override
    public List<HistoricTaskInstance> queryHistoryTask(String processInstanceId) {
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        if(!taskInstanceList.isEmpty()){
            for(HistoricTaskInstance historicTaskInstance:taskInstanceList){
               log.info("历史流程实例任务id:"+ historicTaskInstance.getId());
               log.info("历史流程定义的id:"+ historicTaskInstance.getProcessDefinitionId());
               log.info("历史流程实例任务名称：" +historicTaskInstance.getName());
               log.info("历史流程实例任务处理人："+historicTaskInstance.getAssignee());
            }
            return taskInstanceList;
        }
        return new ArrayList<>();
    }

    @Override
    public ProcessDefinitionQuery deployProcessDefinition(InputStream zipBpmFileInputStream, String deploymentName, String tenantId) {
        try {
            logger.debug("deployProcessDefine, deploymentName: {}, tenantId: {}.", deploymentName, tenantId);
            // 发布新版本
            Deployment deployment = repositoryService.createDeployment()
                    .addZipInputStream(new ZipInputStream(zipBpmFileInputStream)) //
                    .name(deploymentName)
                    .tenantId(tenantId)
                    .deploy();
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId());
           log.info("processDefinitionQuery.list().size()--->"+processDefinitionQuery.list().size());
            return processDefinitionQuery;
        } catch (Exception e) {
            logger.error("deployProcessDefine failed.", e);
            return null;
        }
    }

    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        logger.debug("deleteProcessInstance, processInstanceId: {}, deleteReason: {}.", processInstanceId,
                deleteReason);
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }


}
