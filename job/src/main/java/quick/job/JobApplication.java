package quick.job;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.base.Charsets;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import quick.job.common.utils.CommonContext;

import java.util.List;

@SpringBootApplication(scanBasePackages = "quick.job")
@MapperScan("quick.job.mapper")
public class JobApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(JobApplication.class, args);
        CommonContext.setApplicationContext(context);
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setDefaultCharset(Charsets.UTF_8);
        FastJsonConfig config = converter.getFastJsonConfig();
        config.setDateFormat("YYYY-mm-dd HH:mm:ss");
        converters.add(converter);
    }

}
