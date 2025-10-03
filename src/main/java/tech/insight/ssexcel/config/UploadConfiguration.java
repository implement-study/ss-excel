package tech.insight.ssexcel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author gongxuanzhangmelt@gmail.com
 **/
@Configuration
public class UploadConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 注册自定义参数解析器
        resolvers.add(new UploadDataHandlerMethodArgumentResolver());
    }
}
