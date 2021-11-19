package com.intellifusion.service;

import com.intellifusion.entity.DiskInfo;

import java.io.IOException;

public interface SystemInfoService {

    public DiskInfo getFastDFSDiskInfo(String mountPoint);

}
