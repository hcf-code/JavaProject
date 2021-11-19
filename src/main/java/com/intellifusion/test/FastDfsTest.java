package com.intellifusion.test;

import com.github.tobato.fastdfs.domain.fdfs.GroupState;
import com.github.tobato.fastdfs.domain.fdfs.StorageNode;
import com.github.tobato.fastdfs.domain.fdfs.StorageState;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class FastDfsTest {

    @Autowired
    FastFileStorageClient fastFileStorageClient;

    @Autowired
    TrackerClient trackerClient;

    @Test
    public void getStorage(){
        StorageNode storeStorage = trackerClient.getStoreStorage();
        System.out.println(storeStorage);
        List<StorageState> stateList = trackerClient.listStorages("group1");
        List<GroupState> groupStates = trackerClient.listGroups();
        for (GroupState state:groupStates) {
            System.out.println(state.getGroupName());
        }
        System.out.println("111");
    }
}
