package com.intellifusion.controller;

import com.intellifusion.entity.DiskInfos;
import com.intellifusion.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("fdfs")
@RestController
public class FastDfsController {

    @Autowired
    UploadService uploadService;


    @RequestMapping("upload")
    @ResponseBody
    public Map<String, Object> upload(MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String path = uploadService.uploadImage(file);
        map.put("path",path);
        return map;
    }

    @RequestMapping("uploadSmall")
    @ResponseBody
    public Map<String, Object> uploadSmall(MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String path = uploadService.uploadSmallImage(file);
        map.put("path",path);
        return map;
    }


    @RequestMapping(path = "/download")
    public void download(String fileName) throws IOException {
        uploadService.downLoad(fileName);
    }

    @RequestMapping(path = "/getDiskInfo")
    public DiskInfos getDiskInfo(){
        return uploadService.getDiskInfo();
    }
}
