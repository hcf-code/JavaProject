package com.intellifusion.controller;

import com.intellifusion.entity.DiskInfo;
import com.intellifusion.service.MinIoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author SanheDashen
 * @date 2022/07/01 15:00
 */

@RestController
@RequestMapping("/minio")
public class MinIoController {

    @Autowired
    MinIoService minIoService;

    @RequestMapping("/upload")
    public void upload() throws Exception {
        minIoService.upload();
    }


    @RequestMapping("/download")
    public void download() throws Exception {
        minIoService.download();
    }

    @Autowired
    ApplicationContext applicationContext;


    @RequestMapping("/test")
    public void test(HttpServletRequest request) throws Exception {

        System.out.println(applicationContext.getBean("&test"));

    }

}
