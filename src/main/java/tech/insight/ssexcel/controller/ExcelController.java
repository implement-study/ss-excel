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

    //  导出数据 下载excel

    @GetMapping("/download")
    @SSExcel(excelName = "用户数据$date")
    public List<User> download() {
        return data();
    }

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
