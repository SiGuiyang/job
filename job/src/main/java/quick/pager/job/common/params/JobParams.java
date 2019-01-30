package quick.pager.job.common.params;

import java.util.Date;
import java.util.Map;
import lombok.Data;
import lombok.ToString;

/**
 * job执行参数
 */
@Data
@ToString
public class JobParams {
    /**
     * 执行方式
     */
    private String operation;
    /**
     * 参数
     */
    private Map<String,Object> params;
    /**
     * 执行job名称
     */
    private String jobName;
    /**
     * 执行job组
     */
    private String jobGroup;
    /**
     * 当前任务执行时间
     */
    private Date executeTime;
    /**
     * 下次执行的时间
     */
    private Date nextExecuteTime;
    /**
     * 执行的任务Id
     */
    private Long jobId;
    /**
     * spring bean name
     */
    private String springId;
    /**
     * 执行Job任务的方法
     */
    private String method;

}
