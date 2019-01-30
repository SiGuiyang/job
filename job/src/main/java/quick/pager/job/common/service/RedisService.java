package quick.pager.job.common.service;

public interface RedisService {
    String get(String key);

    void del(String key);

    void set(String key,String value);

    void setExpire(String key,String value,long seconds);

    void publish(String channel,String message);

    void publish(String message);


}
