package quick.pager.job.common.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.util.ObjectUtils;
import quick.pager.job.common.params.JobParams;
import quick.pager.job.common.utils.CommonContext;
import quick.pager.job.common.utils.JobCenter;
import quick.pager.job.maintaince.constants.Constants;
import quick.pager.job.maintaince.model.JobModel;
import quick.pager.job.mapper.JobMapper;

/**
 * 执行job任务的监听
 */
@Slf4j
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
        JobModel jobModel = jobMapper.selectByPrimaryKey(jobId);
        // 立即执行
        if (Constants.Operation.execute.equals(operation)) {
            log.info("redis 触发立即执行");
            jobModel.setJobStatus(Constants.JobStatus.running.status);
            JobCenter.invoke(jobParams);
            jobMapper.updateByPrimaryKeySelective(jobModel);
        } else if (Constants.Operation.pause.equals(operation)) { // 暂停
            log.info("redis 触发暂停");
            jobModel.setJobStatus(Constants.JobStatus.pause.status);
            JobCenter.pauseJob(scheduler, jobParams.getJobName(), jobParams.getJobGroup());
            jobMapper.updateByPrimaryKeySelective(jobModel);
        } else if (Constants.Operation.resume.equals(operation)) { // 恢复
            log.info("redis 触发恢复");
            jobModel.setJobStatus(Constants.JobStatus.running.status);
            JobCenter.resumeJob(scheduler, jobParams.getJobName(), jobParams.getJobGroup());
            jobMapper.updateByPrimaryKeySelective(jobModel);
        } else if (Constants.Operation.delete.equals(operation)) { // 删除
            log.info("redis 触发删除");
            JobCenter.deleteJob(scheduler, jobParams.getJobName(), jobParams.getJobGroup());
            jobModel.setJobStatus(Constants.JobStatus.delete.status);
            jobMapper.updateByPrimaryKeySelective(jobModel);
        } else if (Constants.Operation.save.equals(operation)) { // 创建新的job任务
            log.info("redis 触发创建新的job任务");
            JobCenter.createJob(scheduler, jobModel);
        } else if (Constants.Operation.update.equals(operation)) { // 更新job任务
            log.info("redis 触发更新job任务");

            JobCenter.updateJob(scheduler, jobModel);
        }

    }
}
