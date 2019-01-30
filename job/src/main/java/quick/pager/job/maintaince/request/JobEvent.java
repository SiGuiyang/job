package quick.pager.job.maintaince.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class JobEvent extends BaseEvent {
    private static final long serialVersionUID = -7215766248406654134L;
    /**
     * job名称
     */
    private String jobName;
    /**
     * job组
     */
    private String jobGroup;
    /**
     * job状态
     */
    private String jobStatus;

    private String cron;

    private String description;

    private String springId;

    private String className;

    private String methodName;

}
