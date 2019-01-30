package quick.pager.job;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import quick.pager.job.common.utils.CommonContext;

import java.util.List;
import quick.pager.job.common.utils.JobCenter;
import quick.pager.job.maintaince.model.JobModel;
import quick.pager.job.mapper.JobMapper;

@SpringBootApplication
@MapperScan("quick.pager.job.mapper")
@Slf4j
public class JobApplication extends WebMvcConfigurerAdapter implements CommandLineRunner {

    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private Scheduler scheduler;


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty);
        //日期格式化
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        converter.setFastJsonConfig(fastJsonConfig);

        List<MediaType> supportedMediaTypes = new ArrayList<>();

        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        converters.add(converter);
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info("初始化job任务开始 ==================");
        List<JobModel> jobModelList = jobMapper.selectAll(new JobModel());

        for (JobModel model : jobModelList) {
            CronTrigger cronTrigger = JobCenter.getCronTrigger(scheduler, model.getJobName(), model.getJobGroup());
            if (ObjectUtils.isEmpty(cronTrigger)) {
                JobCenter.createJob(scheduler, model);
            } else {
                JobCenter.updateJob(scheduler, model);
            }
        }
        log.info("job任务初始化完毕 =================");
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(JobApplication.class, args);
        CommonContext.setApplicationContext(context);
    }
}
