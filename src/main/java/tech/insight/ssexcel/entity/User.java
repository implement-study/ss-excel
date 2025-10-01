package tech.insight.ssexcel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author gongxuanzhangmelt@gmail.com
 **/
@Data
public class User {

    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("年龄")
    private Integer age;
}
