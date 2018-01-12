package quick.job.common.utils;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import quick.job.action.adapter.JobAdapter;
import quick.job.common.factory.SyncJobFactory;
import quick.job.maintaince.model.JobModel;
import quick.job.common.params.JobParams;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * 任务调度工具类
 *
 * @author SiGuiyang
 */
public class JobCenter {
    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(JobCenter.class);

    /**
     * 根据job名称和job组名称获取触发key
     *
     * @param jobName  jobName
     * @param jobGroup jobGroup
     */
    private static TriggerKey getTriggerKey(String jobName, String jobGroup) {
        return TriggerKey.triggerKey(jobName, jobGroup);
    }

    /**
     * 获取表达式触发器
     *
     * @param scheduler 调度器
     * @param jobName   job名称
     * @param jobGroup  job组
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, String jobName, String jobGroup) {
        TriggerKey key = getTriggerKey(jobName, jobGroup);
        CronTrigger trigger = null;
        try {
            trigger = (CronTrigger) scheduler.getTrigger(key);
        } catch (SchedulerException e) {
            logger.error("获取定时任务CronTrigger出现异常");
        }
        return trigger;
    }

    /**
     * 创建任务
     *
     * @param scheduler scheduler
     * @param job       job
     * @param params    params
     */
    public static void createJob(Scheduler scheduler, JobModel job, JobParams params) {
        Class<? extends Job> jobClass = SyncJobFactory.class;

        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(job.getJobName(), job.getJobGroup()).build();
        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());
        // 按新的cron表达式构建一个新的trigger
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup())
                .withSchedule(scheduleBuilder).build();
        // 获取job触发器的名称
        jobDetail.getJobDataMap().put("JobAdapter", params);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error("创建定时任务失败，错误信息：{}", e);
        }

    }

    /**
     * 创建定时任务
     *
     * @param scheduler scheduler
     * @param job       job
     */
    public static void createJob(Scheduler scheduler, JobModel job) {

        Class<? extends Job> jobClass = SyncJobFactory.class;

        TriggerKey key = getTriggerKey(job.getJobName(), job.getJobGroup());

        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(key);
            if (null == trigger) {
                // 构建job信息
                JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(job.getJobName(), job.getJobGroup())
                        .build();
                // 表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());
                // 按新的cron表达式构建一个新的trigger
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup())
                        .withSchedule(scheduleBuilder).build();

                // 获取job触发器的名称
                jobDetail.getJobDataMap().put("JobAdapter", job);
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());
                trigger = TriggerBuilder.newTrigger().withIdentity(key).withSchedule(scheduleBuilder).build();
                scheduler.rescheduleJob(key, trigger);
            }
        } catch (SchedulerException e) {
            logger.error("创建定时任务失败，错误信息：{}", e);
        }
    }

    /**
     * 获取所有job任务
     */
    public static List<JobModel> getJobs(Scheduler scheduler) {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();

        List<JobModel> jobs = Lists.newArrayList();
        try {
            Set<JobKey> jobKyes = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKyes) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {

                    JobModel event = new JobModel();
                    event.setJobName(jobKey.getName());
                    event.setJobGroup(jobKey.getGroup());
                    event.setDescription("触发器：" + trigger.getKey());
                    Trigger.TriggerState state = scheduler.getTriggerState(trigger.getKey());
                    event.setJobStatus(state.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        event.setCron(cronTrigger.getCronExpression());
                    }
                    jobs.add(event);
                }
            }
            logger.info("获取所有计划中的任务列表——————————{}", JSON.toJSONString(jobs));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    /**
     * 获取正在运行的job任务
     *
     * @param scheduler scheduler
     */
    public static List<JobModel> getRunningJobs(Scheduler scheduler) {

        try {
            List<JobExecutionContext> contexts = scheduler.getCurrentlyExecutingJobs();
            List<JobModel> jobs = Lists.newArrayListWithCapacity(contexts.size());

            for (JobExecutionContext context : contexts) {
                JobModel event = new JobModel();
                JobDetail jobDetail = context.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = context.getTrigger();
                event.setJobName(jobKey.getName());
                event.setJobGroup(jobKey.getGroup());
                event.setDescription("触发器————————————{}" + trigger.getKey());
                Trigger.TriggerState state = scheduler.getTriggerState(trigger.getKey());
                event.setJobStatus(state.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    event.setCron(cronTrigger.getCronExpression());
                }
                jobs.add(event);
            }

            logger.info("所有正在运行的job任务————————————{}", JSON.toJSONString(jobs));
            return jobs;
        } catch (SchedulerException e) {
            logger.error("未找到运行中的job任务：{}", e);
        }
        return null;
    }

    /**
     * 运行一次任务
     *
     * @param scheduler scheduler
     * @param jobName   jobName
     * @param jobGroup  jobGroup
     */
    public static void runOnce(Scheduler scheduler, String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("运行一次定时任务失败————————————————", e);
        }
    }

    /**
     * 暂停任务
     *
     * @param scheduler scheduler
     * @param jobName   jobName
     * @param jobGroup  jobGroup
     */
    public static void pauseJob(Scheduler scheduler, String jobName, String jobGroup) {

        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("暂停定时任务失败————————————————", e);
        }
    }

    /**
     * 恢复任务
     *
     * @param scheduler scheduler
     * @param jobName   jobName
     * @param jobGroup  jobGroup
     */
    public static void resumeJob(Scheduler scheduler, String jobName, String jobGroup) {

        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("暂停定时任务失败——————————————————", e);
        }
    }

    /**
     * 获取jobKey
     *
     * @param jobName  the job name
     * @param jobGroup the job group
     * @return the job key
     */
    private static JobKey getJobKey(String jobName, String jobGroup) {

        return JobKey.jobKey(jobName, jobGroup);
    }

    /**
     * 更新定时任务
     *
     * @param scheduler the scheduler
     */
    public static void updateJob(Scheduler scheduler, JobModel job) {
        updateJob(scheduler, job.getJobName(), job.getJobGroup(), job.getCron(), job);
    }

    /**
     * 更新定时任务
     *
     * @param scheduler      the scheduler
     * @param jobName        the job name
     * @param jobGroup       the job group
     * @param cronExpression the cron expression
     * @param param          the param
     */
    private static void updateJob(Scheduler scheduler, String jobName, String jobGroup, String cronExpression, Object param) {

        // 同步或异步
        Class<? extends Job> jobClass = SyncJobFactory.class;

        try {
            JobDetail jobDetail = scheduler.getJobDetail(getJobKey(jobName, jobGroup));

            jobDetail = jobDetail.getJobBuilder().ofType(jobClass).build();

            // 更新参数 实际测试中发现无法更新
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            jobDataMap.put("JobAdapter", param);
            jobDetail.getJobBuilder().usingJobData(jobDataMap);

            TriggerKey triggerKey = getTriggerKey(jobName, jobGroup);

            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            // 忽略状态为PAUSED的任务，解决集群环境中在其他机器设置定时任务为PAUSED状态后，集群环境启动另一台主机时定时任务全被唤醒的bug
            if (!triggerState.name().equalsIgnoreCase("PAUSED")) {
                // 按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            logger.error("更新定时任务失败——————————————————", e);
        }
    }

    /**
     * 删除定时任务
     *
     * @param scheduler scheduler
     * @param jobName   jobName
     * @param jobGroup  jobGroup
     */
    public static void deleteJob(Scheduler scheduler, String jobName, String jobGroup) {
        try {
            scheduler.deleteJob(getJobKey(jobName, jobGroup));
        } catch (SchedulerException e) {
            logger.error("删除定时任务失败————————————————", e);
        }
    }

    /**
     * 通过反射执行定时任务的方法
     *
     * @param params job执行的参数
     */
    public static void invoke(JobParams params) {
        if (ObjectUtils.isEmpty(params)) {
            return;
        }
        String springId = params.getSpringId();
        String jobName = params.getJobName();
        String jobGroup = params.getJobGroup();
        String methodName = params.getMethod();
        logger.info("反射执行定时任务[——————————jobName：{}———————jobGroup: {}————————————]", jobName, jobGroup);
        JobAdapter job;
        if (StringUtils.isEmpty(springId)) {
            logger.warn("执行job任务的springId为空");
            return;
        }

        job = CommonContext.getBean(springId, JobAdapter.class);
        if (null == job) {

            logger.warn("任务名称 = {} ————————————未启动成功，请检查是否配置正确！！！", jobName);
            return;
        }
        Class<?> clazz = job.getClass();
        Method method = null;

        try {
            // 通过反射执行job的方法
            method = clazz.getDeclaredMethod(methodName, JobParams.class);
        } catch (NoSuchMethodException | SecurityException e) {
            logger.error("任务名称 = {}————————————未启动成功，方法名设置错误！！！", jobName);
        }

        if (null != method) {
            try {
                method.invoke(job, params);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.error("任务名称 = {} ————————————未启动成功，反射调用错误！！！", jobName);
            }
        }
    }
}
