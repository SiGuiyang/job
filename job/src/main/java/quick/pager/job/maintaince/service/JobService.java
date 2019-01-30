package quick.pager.job.maintaince.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quick.pager.job.common.params.JobParams;
import quick.pager.job.common.service.RedisService;
import quick.pager.job.maintaince.constants.Constants;
import quick.pager.job.maintaince.model.JobModel;
import quick.pager.job.maintaince.request.BaseEvent;
import quick.pager.job.maintaince.request.JobEvent;
import quick.pager.job.maintaince.resp.Response;

import java.util.Date;
import java.util.List;
import quick.pager.job.mapper.JobMapper;

@Service
public class JobService implements IService {
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public Response doService(BaseEvent evt) {
        JobEvent jobEvent = (JobEvent) evt;
        String operation = jobEvent.getOperation();
        Response resp = new Response();

        JobModel model = jobMapper.selectByPrimaryKey(jobEvent.getId());

        switch (operation) {
            case Constants.Operation.info:
                // 查询单个job任务
                resp = new Response<>();
                resp.setData(model);
                break;
            case Constants.Operation.all: // 查询所有job任务
                resp = queryAll(jobEvent);
                break;
            case Constants.Operation.save:
            case Constants.Operation.update:
                resp = modify(jobEvent);
                break;
            default:
                publish(model, operation);
        }

        return resp;
    }

    /**
     * 查询定时任务
     *
     * @param jobEvent 参数
     */
    private Response<List<JobModel>> queryAll(JobEvent jobEvent) {
        Response<List<JobModel>> response = new Response<>();
        PageHelper.startPage(jobEvent.getPage(), jobEvent.getLimit());
        JobModel jobModel = new JobModel();
        jobModel.setJobGroup(jobEvent.getJobGroup());
        jobModel.setJobName(jobEvent.getJobName());
        jobModel.setJobStatus(jobEvent.getJobStatus());
        jobModel.setId(jobEvent.getId());
        List<JobModel> jobModelList = jobMapper.selectAll(jobModel);
        PageInfo<JobModel> pageInfo = new PageInfo<>(jobModelList);

        response.setData(jobModelList);
        response.setTotal(pageInfo.getTotal());
        return response;
    }

    private Response modify(JobEvent jobEvent) {

        JobModel jobModel = new JobModel();
        jobModel.setCreateTime(new Date());
        jobModel.setJobStatus(Constants.JobStatus.running.status);
        jobModel.setJobName(jobEvent.getJobName());
        jobModel.setJobGroup(jobEvent.getJobGroup());
        jobModel.setClassName(jobEvent.getClassName());
        jobModel.setDescription(jobEvent.getDescription());
        jobModel.setCron(jobEvent.getCron());
        jobModel.setMethodName(jobEvent.getMethodName());
        jobModel.setSpringId(jobEvent.getSpringId());

        if (null != jobEvent.getId()) {
            jobModel.setId(jobEvent.getId());
            jobMapper.updateByPrimaryKeySelective(jobModel);

        } else {
            jobMapper.insertSelective(jobModel);

        }

        publish(jobModel, jobEvent.getOperation());

        return new Response();


    }

    // 立即执行，更新，删除，恢复，暂停使用redis队列实现
    private void publish(JobModel model, String operation) {
        JobParams params = new JobParams();
        params.setMethod(model.getMethodName());
        params.setJobId(model.getId());
        params.setSpringId(model.getSpringId());
        params.setExecuteTime(new Date());
        params.setOperation(operation);
        params.setJobName(model.getJobName());
        params.setJobGroup(model.getJobGroup());
        redisService.publish(JSON.toJSONString(params));
    }
}
