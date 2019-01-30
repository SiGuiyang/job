package quick.pager.job.maintaince.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import quick.pager.job.maintaince.constants.Constants;
import quick.pager.job.maintaince.request.JobEvent;
import quick.pager.job.maintaince.resp.Response;
import quick.pager.job.maintaince.service.JobService;

@RestController
@RequestMapping("/job")
@CrossOrigin
public class JobController {
    @Autowired
    private JobService jobService;

    @PostMapping("/query")
    public Response queryAllJobs(JobEvent event) {
        event.setOperation(Constants.Operation.all);
        return jobService.doService(event);
    }

    @PostMapping("/update")
    public Response update(JobEvent event) {
        event.setOperation(Constants.Operation.update);
        return jobService.doService(event);
    }

    @PostMapping("/save")
    public Response saveJob(JobEvent event) {
        event.setOperation(Constants.Operation.save);
        return jobService.doService(event);
    }

    @PostMapping("/delete/{id}")
    public Response deleteJob(@PathVariable("id") Long id) {
        JobEvent event = new JobEvent();
        event.setId(id);
        event.setOperation(Constants.Operation.delete);
        return jobService.doService(event);
    }

    @PostMapping("/execute/{id}")
    public Response doJob(@PathVariable("id") Long id) {
        JobEvent event = new JobEvent();
        event.setId(id);
        event.setOperation(Constants.Operation.execute);
        return jobService.doService(event);
    }

    @PostMapping("/resume/{id}")
    public Response resume(@PathVariable("id") Long id) {
        JobEvent event = new JobEvent();
        event.setId(id);
        event.setOperation(Constants.Operation.resume);
        return jobService.doService(event);
    }

    @PostMapping("/pause/{id}")
    public Response pause(@PathVariable("id") Long id) {
        JobEvent event = new JobEvent();
        event.setId(id);
        event.setOperation(Constants.Operation.pause);
        return jobService.doService(event);
    }

    @PostMapping("/query/{id}")
    public Response queryJob(@PathVariable("id") Long id) {
        JobEvent event = new JobEvent();
        event.setId(id);
        event.setOperation(Constants.Operation.info);
        return jobService.doService(event);
    }

}
