package quick.job.tests;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;

import org.junit.Test;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import quick.job.action.adapter.JobAdapter;
import quick.job.common.params.JobParams;

public class JobTest
{

	public static void main(String[] args)
	{
		try
		{

			JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger", "group1").startNow()
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(1).repeatForever())
					.build();

			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			scheduler.start();

			scheduler.scheduleJob(jobDetail, trigger);

			// scheduler.shutdown();

		}
		catch (SchedulerException e)
		{
			e.printStackTrace();
		}

	}
	
	@Test
	public void tesJob()
	{
		JobAdapter job = new HelloJob2();
		Class<?> clazz = job.getClass();
		Method method = null;
		JobParams params = new JobParams();
		params.setExecuteTime(new Date());
		params.setJobId(1L);
		params.setParams(new HashMap<String,Object>());
		try
		{
			method = clazz.getDeclaredMethod("execute", new Class[] { JobParams.class });
			method.invoke(job, params);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
