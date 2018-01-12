package quick.job.action;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import quick.job.action.adapter.JobAdapter;
import quick.job.common.params.JobParams;

@Component("helloJob")
public class HelloAction implements JobAdapter {

    @Override
    public void execute(JobParams params) throws Exception {
        System.out.println("执行JOB任务的参数 = " + JSON.toJSONString(params));
    }
}
