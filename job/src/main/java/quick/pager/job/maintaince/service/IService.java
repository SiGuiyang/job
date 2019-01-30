package quick.pager.job.maintaince.service;

import quick.pager.job.maintaince.request.BaseEvent;
import quick.pager.job.maintaince.resp.Response;

/**
 * service层顶级接口
 *
 * @param <R> 返回数据模型
 * @author siguiyang
 */
public interface IService<R> {
    /**
     * 执行service层方法
     *
     * @param evt 入参
     */
    Response<R> doService(BaseEvent evt);
}
