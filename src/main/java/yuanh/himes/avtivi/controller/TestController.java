package yuanh.himes.avtivi.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yuanh.himes.avtivi.service.Workservice;
import yuanh.himes.avtivi.vo.TaskVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @Author: guochen
 * @Description:
 * @Date:Created in 1:18 PM 7/3/2020
 */
@Slf4j
@RequestMapping("/api/activiti/test")
@RestController
@Api(tags = "activiti")
public class TestController {

    @Autowired
    Workservice workservice;

    @ApiOperation(value = "上传流程")
    @PostMapping(value = "/deployments", consumes = "multipart/form-data", produces = "application/json;charset=utf-8")
    public void deployProcessDefinition(@RequestParam("file") MultipartFile zipBpmFile,
                                        @RequestParam("deploymentName") String deploymentName,
                                        @RequestParam("tenantId") String tenantId, HttpServletResponse httpServletResponse){
        try {
            log.debug("deployProcessDefinition, deploymentName: {}, tenantId: {}.", deploymentName, tenantId);
            workservice.deployProcessDefinition(zipBpmFile.getInputStream(), deploymentName.trim(), tenantId.trim());
        } catch (Exception e) {
            log.error("deployProcessDefinition failed.", e);
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }
    @GetMapping("/deploy")
    @ApiOperation(value = "流程部署")
    public void deploy() {
        workservice.deploy("","","");
    }

    @PostMapping("/run")
    @ApiOperation(value = "启动流程")
    public  void runProcess(@RequestParam(value = "processKey", required = true) String processKey) {
        workservice.runProcess(processKey);
    }

    @GetMapping("/query")
    @ApiOperation(value = "查询任务")
    public void queryTask(@RequestParam("assginName")String assginName) {
        workservice.queryTask(assginName);
    }

    @PostMapping("/complete")
    @ApiOperation(value = "执行任务")
    public void completeTask(@RequestParam("taskId")String taskId, @RequestBody TaskVo taskVo) {
        workservice.complieTask(taskId, taskVo);
    }

    @GetMapping("/queryProcess")
    @ApiOperation(value = "查询流程定义")
    public void queryProcessDefination(@RequestParam("processDefikey")String  processDefikey){
        workservice.queryProcessDefination(processDefikey);
        workservice.queryProcessInstanceState();

    }

    @DeleteMapping("/processDefi")
    @ApiOperation(value = "删除流程部署")
    public void deleteProcessDefi(@RequestParam("deploymentId")String deploymentId){
        workservice.deleteProcessDefi(deploymentId);
    }

    @DeleteMapping("/processInstance")
    @ApiOperation(value = "删除正常运行的流程")
    void deleteProcessInstance(@RequestParam("processInstanceId")String processInstanceId, @RequestParam("deleteReason") String deleteReason){
       workservice.deleteProcessInstance(processInstanceId, deleteReason);
    }

    @GetMapping("/historyInstance")
    @ApiOperation(value = "查看历史流程实例信息")
    public Object queryHistoryProcInst(){
        return workservice.queryHistoryProcInst();
    }
    @GetMapping("/historyInstanceCompleteTask")
    @ApiOperation(value = "查询流程实例历史执行信息")
    public Object queryHistoryTask(String processInstanceId){
       return workservice.queryHistoryTask(processInstanceId);
    }


}
