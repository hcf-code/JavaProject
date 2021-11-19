package com.intellifusion.service;

import com.github.tobato.fastdfs.domain.fdfs.GroupState;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import com.intellifusion.entity.DiskInfo;
import com.intellifusion.entity.DiskInfos;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.xml.ws.soap.Addressing;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Autowired
    FastFileStorageClient fastFileStorageClient;

    @Autowired
    TrackerClient trackerClient;

    //上传普通图
    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null || image.getWidth() == 0 || image.getHeight() == 0)
            throw new RuntimeException("上传的文件不是图片！");
        //获取扩展名
        String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        //上传到服务器
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
        return "106.15.251.229" + storePath.getFullPath();
    }

    //上传缩略图
    @Override
    public String uploadSmallImage(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null || image.getWidth() == 0 || image.getHeight() == 0)
            throw new RuntimeException("上传的文件不是图片！");
        //获取扩展名
        String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        //上传到服务器
        StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extension, null);
        return "106.15.251.229/" + storePath.getFullPath();
    }

    @Override
    public void downLoad(String path) throws IOException {
        byte[] bytes = fastFileStorageClient.downloadFile("group1", path, new DownloadByteArray());
        FileOutputStream stream = new FileOutputStream("150x150.jpg");
        stream.write(bytes);
    }



    //随着规模增大，未来可能会有搭建Storage cluster，其中某一个group占用量过大都会导致程序崩溃，所以不能看总剩余量
    //大多数情况各组间轮询存储，各组容量剩余相差不会很大

    @Override
    public DiskInfos getDiskInfo() {
        List<GroupState> groupStates = trackerClient.listGroups();

        //test
        GroupState groupState = new GroupState();
        groupState.setFreeMB(30000);
        groupState.setGroupName("test");
        groupState.setTotalMB(40000);
        groupStates.add(groupState);

        DecimalFormat df = new DecimalFormat("#.00");

        if (groupStates.isEmpty())
            return null;
        List<DiskInfos> list = new ArrayList<>();
        for (GroupState state : groupStates)
            list.add(new DiskInfos(state.getGroupName(), state.getTotalMB(), state.getFreeMB(), Float.parseFloat(df.format(state.getTotalMB() / 1000f)), Float.parseFloat(df.format(state.getFreeMB() / 1000f))));
        if (list.size() == 1)
            return list.get(0);
        return merge(list);
    }

    public DiskInfos merge(List<DiskInfos> diskInfos) {
        List<DiskInfos> infoList = new ArrayList<>(diskInfos.stream()
                .collect(Collectors.toMap(DiskInfos::getClass,a -> a, (o1, o2) -> {
                    o1.setFreeMB(o1.getFreeMB() + o2.getFreeMB());
                    o1.setTotalMB(o1.getTotalMB() + o2.getTotalMB());
                    o1.setFreeGB(o1.getFreeGB() + o2.getFreeGB());
                    o1.setTotalGB(o1.getTotalGB() + o2.getTotalGB());
                    return o1;
                })).values());
        DiskInfos diskInfo = infoList.get(0);
        diskInfo.setGroupName("totalGroup");
        return diskInfo;
    }

}
