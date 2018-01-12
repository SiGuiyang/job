package quick.job.maintaince.resp;

public class SimpleResp<T> extends Response {
    private static final long serialVersionUID = -8665177432012419699L;

    private long total;

    private T record;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public T getRecord() {
        return record;
    }

    public void setRecord(T record) {
        this.record = record;
    }
}
