package quick.pager.job.maintaince.request;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BaseEvent implements Serializable {
    private static final long serialVersionUID = -6472961780936632120L;
    /**
     * 数据库主键
     */
    private Long id;
    /**
     * 页数
     */
    private int page = 1;
    /**
     * 条数
     */
    private int limit = 20;
    /**
     * mysql 分页起始值
     */
    private int offset;
    /**
     * 操作方式
     */
    private String operation;

    BaseEvent() {
        this.offset = (this.page - 1) * this.limit;
    }

}
