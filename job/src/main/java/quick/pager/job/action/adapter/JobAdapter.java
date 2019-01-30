package quick.pager.job.action.adapter;

import quick.pager.job.common.params.JobParams;

/**
 * 所有的job任务必须实现此接口
 */
public interface JobAdapter {

    /**
     * 执行job任务的方法
     * @param params 执行job任务的参数
     */
    void execute(JobParams params) throws Exception;
}
