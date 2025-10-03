package tech.insight.ssexcel.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author gongxuanzhangmelt@gmail.com
 **/
@Configuration
public class MvcConfiguration implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter adapter;


    @Override
    public void afterPropertiesSet() throws Exception {
        // 注册自定义返回值处理器
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        if (returnValueHandlers != null) {
            List<HandlerMethodReturnValueHandler> newList = new ArrayList<>(returnValueHandlers);
            newList.add(0, new ExcelReturnValueHandler());
            adapter.setReturnValueHandlers(newList);
        }
    }
}
