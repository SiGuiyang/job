package quick.job.maintaince.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quick.job.common.params.JobParams;
import quick.job.common.service.RedisService;
import quick.job.maintaince.constants.Constants;
import quick.job.maintaince.model.JobModel;
import quick.job.maintaince.request.BaseEvent;
import quick.job.maintaince.request.JobEvent;
import quick.job.maintaince.resp.Response;
import quick.job.maintaince.resp.SimpleResp;
import quick.job.mapper.JobMapper;

import java.util.Date;
import java.util.List;

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
        Response resp;
        // 查询单个job任务
        JobModel model = jobMapper.selectPrimaryKey(jobEvent.getId());
        if(Constants.Operation.info.name().equals(operation)){
            resp = new Response();
            resp.setObj(model);
            return resp;
        } else if (Constants.Operation.all.name().equals(operation)) { // 查询所有job任务
            SimpleResp<List<JobModel>> simpleResp = new SimpleResp<>();
            PageHelper.startPage(jobEvent.getPage(),jobEvent.getLimit());
            JobModel jobModel = new JobModel();
            jobModel.setJobGroup(jobEvent.getJobGroup());
            jobModel.setJobName(jobEvent.getJobName());
            jobModel.setJobStatus(jobEvent.getJobStatus());
            jobModel.setId(jobEvent.getId());
            List<JobModel> jobModelList = jobMapper.selectAll(jobModel);
            PageInfo<JobModel> pageInfo = new PageInfo<>(jobModelList);

            simpleResp.setRecord(jobModelList);
            simpleResp.setTotal(pageInfo.getTotal());
            return simpleResp;
        } else {
            resp = new Response();
            // 保存，由于保存时没有主键，所以后续使用在redis队列中执行会发生异常
            if(Constants.Operation.save.name().equals(operation)) {
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
                jobMapper.save(jobModel);
                // 需要单独写JobParams
                JobParams params = new JobParams();
                params.setMethod(jobModel.getMethodName());
                params.setJobId(jobModel.getId());
                params.setSpringId(jobModel.getSpringId());
                params.setExecuteTime(new Date());
                params.setOperation(jobEvent.getOperation());
                params.setJobName(jobModel.getJobName());
                params.setJobGroup(jobModel.getJobGroup());
                redisService.publish(JSON.toJSONString(params));
                return resp;
            } else if(Constants.Operation.update.name().equals(operation)) { // 更新
                JobModel jobModel = new JobModel();
                jobModel.setId(jobEvent.getId());
                jobModel.setJobStatus(Constants.JobStatus.running.status);
                jobModel.setJobName(jobEvent.getJobName());
                jobModel.setJobGroup(jobEvent.getJobGroup());
                jobModel.setClassName(jobEvent.getClassName());
                jobModel.setDescription(jobEvent.getDescription());
                jobModel.setCron(jobEvent.getCron());
                jobModel.setMethodName(jobEvent.getMethodName());
                jobModel.setSpringId(jobEvent.getSpringId());
                jobMapper.update(jobModel);
            }
            // 立即执行，更新，删除，恢复，暂停使用redis队列实现
            JobParams params = new JobParams();
            params.setMethod(model.getMethodName());
            params.setJobId(model.getId());
            params.setSpringId(model.getSpringId());
            params.setExecuteTime(new Date());
            params.setOperation(jobEvent.getOperation());
            params.setJobName(model.getJobName());
            params.setJobGroup(model.getJobGroup());
            redisService.publish(JSON.toJSONString(params));
        }
        return resp;
    }
}
