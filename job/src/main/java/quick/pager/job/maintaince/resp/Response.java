package quick.pager.job.maintaince.resp;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Response<T> implements Serializable {
    private static final long serialVersionUID = -2418939711153539401L;

    private long total;

    private int code = 200;

    private String message = "操作成功";

    private T data;
}
