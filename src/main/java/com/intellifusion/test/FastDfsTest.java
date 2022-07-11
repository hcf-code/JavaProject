package com.intellifusion.test;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class FastDfsTest {

    @Autowired
    FastFileStorageClient fastFileStorageClient;

    @Autowired
    TrackerClient trackerClient;

    @Test
    public void getStorage() {
        StorageNode storeStorage = trackerClient.getStoreStorage();
        System.out.println(storeStorage);
        List<StorageState> stateList = trackerClient.listStorages("group1");
        List<GroupState> groupStates = trackerClient.listGroups();
        for (GroupState state : groupStates) {
            System.out.println(state.getGroupName());
        }
        System.out.println("111");
    }



    public static <T, R, U extends Comparable<U>> List<T> polymerizationSortTree(List<T> list, Function<T, R> loadKey, Function<T, R> loadParentKey,
                                                                                 BiConsumer<T, List<T>> write, Function<? super T, ? extends U> keyExtractor) {

        List<T> root = list.stream().filter(o -> loadParentKey.apply(o) == null || String.valueOf(loadParentKey.apply(o)).equals(0)).collect(Collectors.toList());
        Stack<T> stack = new Stack<>();
        root.forEach(stack::push);
        while (!stack.isEmpty()) {
            T o = stack.pop();
            R key = loadKey.apply(o);
            List<T> childrenList = list.stream()
                    .filter(k -> key.equals(loadParentKey.apply(k))).sorted(Comparator.comparing(keyExtractor).reversed()).collect(Collectors.toList());
            write.accept(o, CollectionUtils.isEmpty(childrenList) ? null : childrenList);
            if (childrenList.size() > 0) {
                stack.addAll(childrenList);
                childrenList.forEach(stack::push);
            }
        }
        return root;
    }
}
