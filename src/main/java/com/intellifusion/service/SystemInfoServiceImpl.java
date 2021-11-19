package com.intellifusion.service;

import com.intellifusion.entity.DiskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
@Service
@EnableScheduling
public class SystemInfoServiceImpl implements SystemInfoService {

    @Autowired
    SystemInfoService systemInfoService;

    //获取FastDfs挂在的硬盘信息 参数：挂载点目录
    @Override
    public DiskInfo getFastDFSDiskInfo(String mountPoint){
        //Process process = Runtime.getRuntime().exec("df -hl");
        //List<String> lines = IoUtil.readLines(process.getInputStream(), "GBK",new ArrayList<>());
        String[] strings = {"Filesystem      Size  Used Avail Use% Mounted on"," /dev/vda1        40G  8.5G   29G  23% /"," devtmpfs        909M     0  909M   0% /dev"," tmpfs           919M     0  919M   0% /dev/shm"," tmpfs           919M  496K  919M   1% /run"," tmpfs           919M     0  919M   0% /sys/fs/cgroup"," tmpfs           184M     0  184M   0% /run/user/0"};
        List<String> lines = Arrays.stream(strings).collect(Collectors.toList());
        Random random = new Random();
        for (int i = 0; i < lines.size(); i++) {
            String[] infoArr = lines.get(i).trim().split("\\s+");
            if (infoArr[infoArr.length - 1].equalsIgnoreCase(mountPoint)) {
                return new DiskInfo.DiskInfoBuilder()
                        .fileSystem(infoArr[0])
                        .totalSize(infoArr[1])
                        .used(infoArr[2])
                        .avail(infoArr[3])
                        .usedPercent(random.nextInt(100)+"%")
                        .mountedOn(infoArr[5]).build();
            }
        }
        //返回null代表未找到挂载的硬盘
        return null;
    }

    //@Scheduled(cron = "*/5 * * * * ?")
    public void DiskMonitor(){
        String[] points = {"/", "/dev", "/dev/shm", "/run", "/sys/fs/cgroup", "/run/user/0", "fastDfs"};
        DiskInfo diskInfo = systemInfoService.getFastDFSDiskInfo(points[(int) (Math.random()*(6-0)+1)]);
        if (Objects.isNull(diskInfo))
            throw new RuntimeException("挂载点不存在！");
        double usePercent = Double.parseDouble(diskInfo.getUsedPercent().replace("%", ""));
        if (usePercent>70.0){
            System.out.println(diskInfo);
            System.out.println("存储空间不足,请及时清理文件或者选择扩容,存储空间占用率已达: "+diskInfo.getUsedPercent());
        }else {
            System.out.println(diskInfo);
            System.out.println("当前存储空间占用率为："+diskInfo.getUsedPercent());
        }
    }

}
