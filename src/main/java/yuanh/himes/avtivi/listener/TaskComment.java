package yuanh.himes.avtivi.listener;

import java.io.Serializable;

/**
 * @Author: guochen
 * @Description:
 * @Date:Created in 1:18 PM 7/3/2020
 */
public class TaskComment implements Serializable {
    private String assignee;
    private String result;
    private String comment;

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "TaskComment{" +
                "assignee='" + assignee + '\'' +
                ", result='" + result + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
