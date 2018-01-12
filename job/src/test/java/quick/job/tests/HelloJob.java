package quick.job.tests;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job
{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		Date date = new Date();
		System.out.println("调用时间：" + new SimpleDateFormat("hh:mm:ss").format(date));

		System.out.println("Hello World");
	}

}
