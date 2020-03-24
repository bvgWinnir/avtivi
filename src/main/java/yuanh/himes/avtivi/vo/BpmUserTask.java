package yuanh.himes.avtivi.vo;

import lombok.Data;

import java.util.Map;

/**
 * @Author: guochen
 * @Description:
 * @Date:Created in 3:18 PM 7/3/2020
 */
@Data
public class BpmUserTask {
    private String id;
    private String name;
    //private String description;
    //private Integer priority;
    private String assignee;
    private String tenantId;
    private String formKey;
    private Boolean isSuspended;
    private String processDefinitionId;
    private Long createTime;
    private Map<String, Object> context;
    private Map<String, Object> processContext;

}
