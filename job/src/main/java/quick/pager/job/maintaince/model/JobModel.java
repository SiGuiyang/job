package quick.pager.job.maintaince.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JobModel implements Serializable{

    private static final long serialVersionUID = 8984218738753141165L;
    /**
     * 主键
     */
    private Long id;

    private Date createTime;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务分组
     */
    private String jobGroup;
    /**
     * 任务状态<br />
     * 0：暂停<br />
     * 1：删除<br />
     * 2：正常<br />
     */
    private String jobStatus;
    /**
     * cron表达式
     */
    private String cron;
    /**
     * 描述
     */
    private String description;
    /**
     * 任务执行时调用哪个类的方法 包名+类名
     */
    private String className;
    /**
     * spring bean
     */
    private String springId;
    /**
     * 任务调用的方法名
     */
    private String methodName;
}
