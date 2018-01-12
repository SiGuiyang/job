package quick.job.maintaince.service;

import quick.job.maintaince.request.BaseEvent;
import quick.job.maintaince.resp.Response;

public interface IService {
    Response doService(BaseEvent evt);
}
