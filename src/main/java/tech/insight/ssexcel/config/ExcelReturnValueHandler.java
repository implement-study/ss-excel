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

    /**
     * 判断是否支持指定的返回值类型
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        // 检查条件：
        // 1. 方法有 @SSExcel 注解
        // 2. 返回值类型是 Collection 的子类（如 List）
        return returnType.hasMethodAnnotation(SSExcel.class) && Collection.class.isAssignableFrom(returnType.getParameterType());
    }


    /**
     * 处理返回值并将其转换为Excel文件进行下载
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        // 1 获取注解信息
        SSExcel ssExcel = returnType.getMethodAnnotation(SSExcel.class);

        // 2 设置 HTTP 响应头
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        // 3 处理文件名
        String template = ssExcel.excelName();  // "用户数据$date"
        String excelName = analysisExcelName(template);  // "用户数据1703123456789"
        String fileName = URLEncoder.encode(excelName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 4 解析数据类型
        Class<?> excelType = findExcelType(returnType);  // User.class

        // 5 使用 EasyExcel 生成文件
        EasyExcel.write(response.getOutputStream(), excelType)
                .sheet("模板")  // 设置 sheet 名称
                .doWrite((Collection) returnValue);  // 写入数据
    }


    /**
     * 解析Excel文件名模板，将模板中的$date占位符替换为当前时间戳
     *
     * @param template Excel文件名模板字符串，其中可能包含$date占位符
     * @return 替换后的Excel文件名，其中$date被替换为当前时间戳
     */
    private String analysisExcelName(String template) {
        // 将模板中的$date占位符替换为当前系统时间戳
        return template.replace("$date", System.currentTimeMillis() + "");
    }


    /**
     * 查找Excel数据的泛型类型
     *
     * @param returnType 方法参数对象，用于获取泛型类型信息
     * @return 返回Excel数据的泛型类型Class对象
     */
    private Class<?> findExcelType(MethodParameter returnType) {
        // 获取泛型参数类型：List<User>
        Type genericParameterType = returnType.getGenericParameterType();

        // 提取实际类型参数：User.class
        return (Class<?>) ((ParameterizedType) genericParameterType).getActualTypeArguments()[0];
    }

}
