package quick.job.maintaince.request;

import java.io.Serializable;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
