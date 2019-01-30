package quick.pager.job.action;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import quick.pager.job.action.adapter.JobAdapter;
import quick.pager.job.common.params.JobParams;

@Component("helloJob")
public class HelloAction implements JobAdapter {

    @Override
    public void execute(JobParams params) throws Exception {
        System.out.println("执行JOB任务的参数 = " + JSON.toJSONString(params));
    }
}
