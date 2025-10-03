package tech.insight.ssexcel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.insight.ssexcel.config.ExcelData;
import tech.insight.ssexcel.config.SSExcel;
import tech.insight.ssexcel.config.UploadExcel;
import tech.insight.ssexcel.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author gongxuanzhangmelt@gmail.com
 **/
@RestController
@RequestMapping("/excel")
public class ExcelController {

    /**
     * 提供用户数据Excel文件下载接口。
     * 该接口通过{@link SSExcel}注解将返回的用户列表自动转换为Excel文件，
     * 文件名格式为"用户数据"后接当前日期（如"用户数据20231015"）。
     */
    @GetMapping("/download")
    @SSExcel(excelName = "用户数据$date")
    public List<User> download() {
        return data();
    }

    /**
     * 提供用户数据Excel文件上传接口
     * UploadExcel注解标记该方法处理Excel上传
     * ExcelData注解标记参数化接受解析后的数据
     */
    @PostMapping("upload")
    @UploadExcel
    public String upload(@ExcelData List<User> objects) {
        System.out.println(objects);
        return "success";
    }

    private List<User> data() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setAge(i);
            user.setName(UUID.randomUUID().toString());
            users.add(user);
        }
        return users;
    }

    //  上传一个excel

}
