package quick.job.maintaince.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import quick.job.maintaince.constants.Constants;
import quick.job.maintaince.request.JobEvent;
import quick.job.maintaince.resp.Response;
import quick.job.maintaince.service.JobService;

@RestController
@RequestMapping("/job")
@CrossOrigin
public class JobController {
    @Autowired
    private JobService jobService;

    @PostMapping("/query")
    public Response queryAllJobs(JobEvent event) {
        event.setOperation(Constants.Operation.all.name());
        return jobService.doService(event);
    }

    @PostMapping("/update")
    public Response update(JobEvent event) {
        event.setOperation(Constants.Operation.update.name());
        return jobService.doService(event);
    }

    @PostMapping("/save")
    public Response saveJob(JobEvent event) {
        event.setOperation(Constants.Operation.save.name());
        return jobService.doService(event);
    }

    @PostMapping("/delete/{id}")
    public Response deleteJob(@PathVariable("id") Long id) {
        JobEvent event = new JobEvent();
        event.setId(id);
        event.setOperation(Constants.Operation.delete.name());
        return jobService.doService(event);
    }

    @PostMapping("/execute/{id}")
    public Response doJob(@PathVariable("id") Long id) {
        JobEvent event = new JobEvent();
        event.setId(id);
        event.setOperation(Constants.Operation.execute.name());
        return jobService.doService(event);
    }

    @PostMapping("/resume/{id}")
    public Response resume(@PathVariable("id") Long id) {
        JobEvent event = new JobEvent();
        event.setId(id);
        event.setOperation(Constants.Operation.resume.name());
        return jobService.doService(event);
    }

    @PostMapping("/pause/{id}")
    public Response pause(@PathVariable("id") Long id) {
        JobEvent event = new JobEvent();
        event.setId(id);
        event.setOperation(Constants.Operation.pause.name());
        return jobService.doService(event);
    }

    @PostMapping("/query/{id}")
    public Response queryJob(@PathVariable("id") Long id) {
        JobEvent event = new JobEvent();
        event.setId(id);
        event.setOperation(Constants.Operation.info.name());
        return jobService.doService(event);
    }

}
