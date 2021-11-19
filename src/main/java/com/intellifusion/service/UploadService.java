package com.intellifusion.service;

import com.intellifusion.entity.DiskInfos;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {

    public String uploadImage(MultipartFile file) throws IOException;

    public String uploadSmallImage(MultipartFile file) throws IOException;

    void downLoad(String path) throws IOException;

    public DiskInfos getDiskInfo();
}
