package yuanh.himes.avtivi.vo;

import java.util.Map;

/**
 * @Author: guochen
 * @Description:
 * @Date:Created in 3:18 PM 7/3/2020
 */
public class BpmTaskDraftContexts {
    private String taskId;
    //task variables
    private Map<String, String> variables;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "BpmTaskDraftContexts{" +
                "taskId='" + taskId + '\'' +
                ", variables=" + variables +
                '}';
    }
}
