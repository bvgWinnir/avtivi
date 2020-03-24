package yuanh.himes.avtivi.service;


import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import yuanh.himes.avtivi.vo.TaskVo;

import java.io.InputStream;
import java.util.List;

/**
 * @Author: guochen
 * @Description:
 * @Date:Created in 1:18 PM 7/3/2020
 */
public interface Workservice {
    /**
     * create by: guochen
     * description: 部署流程
     * create time: 2020/3/14 23:51
     *
      * @Param: null
     * @return
     */
    void deploy(String processesPath, String processesName, String processesCategory);


    /**
     * create by: guochen
     * description: 运行流程
     * create time: 2020/3/14 23:34
     * 
      * @Param: null
     * @return 
     */
    ProcessInstance runProcess(String processKey);

    /**
     * create by: guochen
     * description: 查询代理人的任务情况
     * create time: 2020/3/14 23:32
     * 
      * @Param: null
     * @return 
     */
    List<Task> queryTask(String assignee);

    /**
     * create by: guochen
     * description: 完成任务
     * create time: 2020/3/14 23:32
     * 
      * @Param: null
     * @return 
     */
    void complieTask(String taskId, TaskVo data);

    /**
     * create by: guochen
     * description: 查看流程的具体定义
     * create time: 2020/3/14 23:32
     * 
      * @Param: null
     * @return 
     */
    List<ProcessDefinition> queryProcessDefination(String processDefikey);

    /**
     * create by: guochen
     * description: 查看流程实例的状态
     * create time: 2020/3/14 23:32
     * 
      * @Param: null
     * @return 
     */
    List<ProcessInstance> queryProcessInstanceState();

    /**
     * create by: guochen
     * description: 删除流程定义
     * create time: 2020/3/14 23:32
     *
      * @Param: null
     * @return
     */
    void deleteProcessDefi(String deploymentId);

    /**
     * create by: guochen
     * description: 查看历史流程实例信息
     * create time: 2020/3/14 23:31
     * 
      * @Param: null
     * @return 
     */
    List<HistoricProcessInstance> queryHistoryProcInst();

    /**
     * create by: guochen
     * description: 查询流程实例历史执行信息
     * create time: 2020/3/14 23:23
     *
      * @Param: processInstanceId
     * @return List<HistoricTaskInstance>
     */
    List<HistoricTaskInstance> queryHistoryTask(String processInstanceId);

    /**
     * create by: guochen
     * description: 上传流程
     * create time: 2020/3/14 23:24
     *
     * @Param: inputStream
     * @Param: trim
     * @Param: trim1
     * @return org.activiti.engine.repository.ProcessDefinitionQuery
     */
    ProcessDefinitionQuery deployProcessDefinition(InputStream inputStream, String trim, String trim1);

    /**
     * create by: guochen
     * description: 删除正常运行的流程
     * create time: 2020/3/14 23:32
     * 
      * @Param: null
     * @return 
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason);
}
