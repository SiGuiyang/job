package quick.job.common.exception;

public class AppExcetion extends RuntimeException {
    private static final long serialVersionUID = -4271032594960811490L;

    private int code;
    private String message;

    public AppExcetion(int code,String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
