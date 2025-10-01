package tech.insight.ssexcel.config;

import com.alibaba.excel.EasyExcel;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Collection;

/**
 * @author gongxuanzhangmelt@gmail.com
 **/
public class ExcelReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.hasMethodAnnotation(SSExcel.class) && Collection.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        SSExcel ssExcel = returnType.getMethodAnnotation(SSExcel.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String template = ssExcel.excelName();
        String excelName = analysisExcelName(template);
        String fileName = URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        Class<?> excelType = findExcelType(returnType);
        EasyExcel.write(response.getOutputStream(), excelType).sheet("模板").doWrite((Collection) returnValue);
    }

    private String analysisExcelName(String template) {
        return template.replace("$date", System.currentTimeMillis() + "");
    }


    private Class<?> findExcelType(MethodParameter returnType) {
        Type genericParameterType = returnType.getGenericParameterType();
        return (Class<?>) ((ParameterizedType) genericParameterType).getActualTypeArguments()[0];
    }

}
