package com.intellifusion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiskInfos {
    private String groupName;
    private Long totalMB;
    private Long freeMB;
    private float totalGB;
    private float freeGB;

}
