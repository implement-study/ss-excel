package tech.insight.ssexcel.config;

import com.alibaba.excel.EasyExcel;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author gongxuanzhangmelt@gmail.com
 **/
public class UploadDataHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ExcelData.class) && parameter.hasMethodAnnotation(UploadExcel.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request instanceof MultipartRequest multipartRequest) {
            MultipartFile file = multipartRequest.getFile("file");
            return EasyExcel.read(file.getInputStream(), findExcelType(parameter), null).sheet().doReadSync();
        }
        return null;
    }

    private Class<?> findExcelType(MethodParameter parameter) {
        Type genericParameterType = parameter.getGenericParameterType();
        return (Class<?>) ((ParameterizedType) genericParameterType).getActualTypeArguments()[0];
    }

}
