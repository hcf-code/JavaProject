package com.intellifusion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiskInfos {
    private String groupName;
    private Long totalMB;
    private Long freeMB;
    private float totalGB;
    private float freeGB;
}
