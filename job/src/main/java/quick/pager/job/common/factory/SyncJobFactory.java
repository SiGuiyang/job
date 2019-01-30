package quick.pager.job.common.factory;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;
import quick.pager.job.common.utils.JobCenter;
import quick.pager.job.maintaince.model.JobModel;
import quick.pager.job.common.params.JobParams;

import java.util.Date;
import java.util.Map;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
public class SyncJobFactory extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("开始执行同步任务工厂————————————");
        JobDataMap mergedJobDateMap = jobExecutionContext.getMergedJobDataMap();
        JobModel job = (JobModel) mergedJobDateMap.get("JobAdapter");
        String jobName = job.getJobName();
        String jobGroup = job.getJobGroup();
        log.info("开始执行job任务：[——————————jobName: {},————jobGroup：{}————]", jobName, jobGroup);
        String triggerKey = jobExecutionContext.getTrigger().getKey().toString();
        log.info("触发器执行key：[——————{}————]", triggerKey);
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

        log.info("调用完成 ==================================");
    }
}
