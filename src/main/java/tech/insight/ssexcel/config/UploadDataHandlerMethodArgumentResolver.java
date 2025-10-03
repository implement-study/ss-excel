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
        // 检查条件：
        // 1. 参数有 @ExcelData 注解
        // 2. 方法有 @UploadExcel 注解
        return parameter.hasParameterAnnotation(ExcelData.class) && parameter.hasMethodAnnotation(UploadExcel.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 1 获取 HTTP 请求
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        // 2 检查是否为文件上传请求
        if (request instanceof MultipartRequest multipartRequest) {
            // 3 获取上传的文件（参数名为 "file"）
            MultipartFile file = multipartRequest.getFile("file");
            // 4 解析泛型类型（List<User> -> User.class）
            Class<?> excelType = findExcelType(parameter);
            // 5 使用 EasyExcel 解析文件
            return EasyExcel.read(file.getInputStream(), excelType, null)
                    .sheet()           // 读取第一个 sheet
                    .doReadSync();     // 同步读取，返回 List<User>
        }
        return null;
    }

    private Class<?> findExcelType(MethodParameter parameter) {
        // 获取泛型参数类型：List<User>
        Type genericParameterType = parameter.getGenericParameterType();

        // 提取实际类型参数：User.class
        return (Class<?>) ((ParameterizedType) genericParameterType).getActualTypeArguments()[0];
    }

}
