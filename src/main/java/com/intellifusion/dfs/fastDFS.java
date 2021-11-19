package com.intellifusion.dfs;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.File;
import java.io.IOException;

public class fastDFS {
    public static void main(String[] args) {
        fdfsUpload();
    }

    public static void fdfsUpload(){
        try {
            ClientGlobal.init("fdfs_client.conf");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            String path = System.getProperty("user.dir")+ File.separator+"hcf2.jpg";
            String[] strings = storageClient.upload_file(path, "jpg", null);
            for (int i = 0; i < strings.length; i++) {
                System.out.println(strings[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void fdfsDownload(){
        try {
            System.out.println(System.getProperty("user.dir"));
            ClientGlobal.init("fdfs_client.conf");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer, null);
            String path = System.getProperty("user.dir")+ File.separator+"hcf.jpg";
            storageClient.download_file("group1","M00/00/00/rBEwq2E4d9SAQqCZAARnXcnGyvE056.jpg",path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
