package yuanh.himes.avtivi.vo;

import java.util.List;

/**
 * @Author: guochen
 * @Description:
 * @Date:Created in 4:18 PM 7/3/2020
 */
public class UserTasksWithInterruptType {
    private String interruptType;

    private List<BpmUserTask> userTasks;

    public UserTasksWithInterruptType() {
    }

    public UserTasksWithInterruptType(String interruptType, List<BpmUserTask> userTasks) {
        this.interruptType = interruptType;
        this.userTasks = userTasks;
    }

    public String getInterruptType() {
        return interruptType;
    }

    public void setInterruptType(String interruptType) {
        this.interruptType = interruptType;
    }

    public List<BpmUserTask> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(List<BpmUserTask> userTasks) {
        this.userTasks = userTasks;
    }

    @Override
    public String toString() {
        return "UserTasksWithInterruptType{" +
                "interruptType='" + interruptType + '\'' +
                ", userTasks=" + userTasks +
                '}';
    }
}
