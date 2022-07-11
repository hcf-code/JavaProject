package com.intellifusion.service;

import com.intellifusion.config.MinioProperties;
import com.intellifusion.entity.DiskInfos;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author SanheDashen
 * @date 2022/07/01 14:50
 */

@Service
public class MinIoServiceImpl implements MinIoService {


    @Autowired
    MinioClient minioClient;

    @Autowired
    MinioProperties minioProperties;

    @Override
    public void upload() throws Exception {
        String bucketName = minioProperties.getBucketName();
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .filename("C:\\Users\\admin\\Pictures\\12331313.jpg")
                        .object("hcf.jpg")
                        .build()
        );
        System.out.println("上传文件成功!");
    }


    @Override
    public void download() throws Exception{
        minioClient.downloadObject(
                DownloadObjectArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .filename("C:\\Users\\admin\\Pictures\\hcf.png")
                        .object("11111111111111.png")
                        .build()
        );
        System.out.println("文件下载完成");
    }

    @Override
    public void test() {
        System.out.println("调用了MinioServiceImpl");
    }

}
