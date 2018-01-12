package quick.job.common.params;

import java.util.Date;
import java.util.Map;

/**
 * job执行参数
 */
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public Date getNextExecuteTime() {
        return nextExecuteTime;
    }

    public void setNextExecuteTime(Date nextExecuteTime) {
        this.nextExecuteTime = nextExecuteTime;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getSpringId() {
        return springId;
    }

    public void setSpringId(String springId) {
        this.springId = springId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
