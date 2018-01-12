package quick.job.tests;

import com.alibaba.fastjson.JSON;
import quick.job.action.adapter.JobAdapter;
import quick.job.common.params.JobParams;

public class HelloJob2 implements JobAdapter
{

	@Override
	public void execute(JobParams params) throws Exception
	{
		System.out.println(JSON.toJSONString(params));
	}

}
