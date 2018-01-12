package quick.job.maintaince.resp;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = -2418939711153539401L;

    private int code = 200;

    private String message = "操作成功";

    private Object obj;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
