package quick.job.common.listener;

import com.alibaba.fastjson.JSON;
import org.quartz.Scheduler;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.util.ObjectUtils;
import quick.job.common.params.JobParams;
import quick.job.common.utils.CommonContext;
import quick.job.common.utils.JobCenter;
import quick.job.maintaince.constants.Constants;
import quick.job.maintaince.model.JobModel;
import quick.job.mapper.JobMapper;

/**
 * 执行job任务的监听
 */
public class RedisMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        JobParams jobParams = JSON.parseObject(message.toString(), JobParams.class);

        if (ObjectUtils.isEmpty(jobParams)) {
            return;
        }
        JobMapper jobMapper = CommonContext.getBean("jobMapper", JobMapper.class);
        Scheduler scheduler = CommonContext.getBean("scheduler", Scheduler.class);
        String operation = jobParams.getOperation();
        Long jobId = jobParams.getJobId();
        JobModel jobModel = jobMapper.selectPrimaryKey(jobId);
        // 立即执行
        if (Constants.Operation.execute.name().equals(operation)) {
            jobModel.setJobStatus(Constants.JobStatus.running.status);
            JobCenter.invoke(jobParams);
            jobMapper.update(jobModel);
        } else if (Constants.Operation.pause.name().equals(operation)) { // 暂停
            jobModel.setJobStatus(Constants.JobStatus.pause.status);
            JobCenter.pauseJob(scheduler, jobParams.getJobName(), jobParams.getJobGroup());
            jobMapper.update(jobModel);
        } else if (Constants.Operation.resume.name().equals(operation)) { // 恢复
            jobModel.setJobStatus(Constants.JobStatus.running.status);
            JobCenter.resumeJob(scheduler, jobParams.getJobName(), jobParams.getJobGroup());
            jobMapper.update(jobModel);
        } else if (Constants.Operation.delete.name().equals(operation)) { // 删除
            JobCenter.deleteJob(scheduler, jobParams.getJobName(), jobParams.getJobGroup());
            jobModel.setJobStatus(Constants.JobStatus.delete.status);
            jobMapper.update(jobModel);
        } else if (Constants.Operation.save.name().equals(operation)) { // 创建新的job任务
            JobCenter.createJob(scheduler, jobModel);
        } else if (Constants.Operation.update.name().equals(operation)) { // 更新job任务
            JobCenter.updateJob(scheduler, jobModel);
        }

    }
}
