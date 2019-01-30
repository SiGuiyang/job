package quick.pager.job.tests;

import com.alibaba.fastjson.JSON;
import quick.pager.job.action.adapter.JobAdapter;
import quick.pager.job.common.params.JobParams;

public class HelloJob2 implements JobAdapter
{

	@Override
	public void execute(JobParams params) throws Exception
	{
		System.out.println(JSON.toJSONString(params));
	}

}
