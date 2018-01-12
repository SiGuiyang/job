package quick.job.common.factory;

import com.alibaba.fastjson.JSON;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import quick.job.common.utils.JobCenter;
import quick.job.maintaince.model.JobModel;
import quick.job.common.params.JobParams;

import java.util.Date;
import java.util.Map;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SyncJobFactory extends QuartzJobBean {

    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(SyncJobFactory.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("开始执行同步任务工厂————————————");
        JobDataMap mergedJobDateMap = jobExecutionContext.getMergedJobDataMap();
        JobModel job = (JobModel) mergedJobDateMap.get("JobAdapter");
        String jobName = job.getJobName();
        String jobGroup = job.getJobGroup();
        logger.info("开始执行job任务：[——————————jobName: {},————jobGroup：{}————]", jobName, jobGroup);
        String triggerKey = jobExecutionContext.getTrigger().getKey().toString();
        logger.info("触发器执行key：[——————{}————]", triggerKey);
        Map<String, Object> wrappedMap = mergedJobDateMap.getWrappedMap();
        Date nextExecuteTime = jobExecutionContext.getTrigger().getNextFireTime();
        Date startTime = jobExecutionContext.getTrigger().getStartTime();

        JobParams params = new JobParams();
        params.setJobName(job.getJobName());
        params.setJobGroup(job.getJobGroup());
        params.setExecuteTime(startTime);
        params.setNextExecuteTime(nextExecuteTime);
        params.setParams(wrappedMap);
        params.setJobId(job.getId());
        params.setSpringId(job.getSpringId());
        params.setMethod(job.getMethodName());
        JobCenter.invoke(params);

        System.out.println("==================================" + JSON.toJSONString(params));
    }
}
