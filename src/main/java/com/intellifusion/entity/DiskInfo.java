package com.intellifusion.entity;

import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class DiskInfo {
    //文件系统
    String fileSystem;
    //空间总大小
    String totalSize;
    //已经使用了多少
    String used;
    //可用空间
    String avail;
    //已经使用的空间百分比
    String usedPercent;
    //挂载点
    String mountedOn;

    public static class DiskInfoBuilder{
        //文件系统
        String fileSystem;
        //空间总大小
        String totalSize;
        //已经使用了多少
        String used;
        //可用空间
        String avail;
        //已经使用的空间百分比
        String usedPercent;
        //挂载点
        String mountedOn;

        public DiskInfoBuilder fileSystem(String fileSystem) {
            this.fileSystem = fileSystem;
            return this;
        }

        public DiskInfoBuilder totalSize(String totalSize) {
            this.totalSize = totalSize;
            return this;
        }

        public DiskInfoBuilder used(String used) {
            this.used = used;
            return this;
        }

        public DiskInfoBuilder avail(String avail) {
            this.avail = avail;
            return this;
        }

        public DiskInfoBuilder usedPercent(String usedPercent) {
            this.usedPercent = usedPercent;
            return this;
        }

        public DiskInfoBuilder mountedOn(String mountedOn) {
            this.mountedOn = mountedOn;
            return this;
        }

        public DiskInfo build(){
            return new DiskInfo(fileSystem,totalSize,used,avail,usedPercent,mountedOn);
        }
    }
}
