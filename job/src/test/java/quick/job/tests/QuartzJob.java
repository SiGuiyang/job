package quick.job.tests;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import quick.job.common.factory.SyncJobFactory;


public class QuartzJob implements Job
{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		System.out.println("任务调度>>>>" + context.getJobDetail().getDescription() + ",type= "
				+ context.getJobDetail().getJobDataMap().get("type"));
		System.out.println(new Date().toString());
	}

	public static void main(String[] args) throws SchedulerException
	{
		SchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler = factory.getScheduler();

		JobDetail jobDetaiail = JobBuilder.newJob(SyncJobFactory.class).withIdentity("myjob").build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("myTrigger").build();

		scheduler.scheduleJob(jobDetaiail, trigger);
		scheduler.start();
	}

}
