package quick.job.init;

import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import quick.job.common.utils.JobCenter;
import quick.job.mapper.JobMapper;
import quick.job.maintaince.model.JobModel;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class JobInit {

    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void init() {
        List<JobModel> jobModelList = jobMapper.selectAll(new JobModel());

        try {
            for (JobModel model : jobModelList) {
                CronTrigger cronTrigger = JobCenter.getCronTrigger(scheduler, model.getJobName(), model.getJobGroup());
                if (ObjectUtils.isEmpty(cronTrigger)) {
                    JobCenter.createJob(scheduler, model);
                } else {
                    JobCenter.updateJob(scheduler, model);
                }
            }
        } catch (Exception e) {

        }
    }

}
