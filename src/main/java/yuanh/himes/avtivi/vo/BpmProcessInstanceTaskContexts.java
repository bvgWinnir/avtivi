package yuanh.himes.avtivi.vo;

import java.util.Map;

/**
 * @Author: guochen
 * @Description:
 * @Date:Created in 3:18 PM 7/3/2020
 */
public class BpmProcessInstanceTaskContexts {
    private String taskId;
    private Map<String, Object> bpmContexts;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId.trim();
    }

    public Map<String, Object> getBpmContexts() {
        return bpmContexts;
    }

    public void setBpmContexts(Map<String, Object> bpmContexts) {
        this.bpmContexts = bpmContexts;
    }

    @Override
    public String toString() {
        return "BpmProcessInstanceTaskContexts{" +
                "taskId='" + taskId + '\'' +
                ", bpmContexts=" + bpmContexts +
                '}';
    }
}
