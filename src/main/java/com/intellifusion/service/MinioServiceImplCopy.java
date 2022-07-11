package com.intellifusion.service;

import org.springframework.stereotype.Service;

/**
 * @author SanheDashen
 * @date 2022/07/08 15:18
 */
@Service
public class MinioServiceImplCopy implements MinIoService {
    @Override
    public void upload() throws Exception {

    }

    @Override
    public void download() throws Exception {

    }

    @Override
    public void test() {
        System.out.println("调用了copy");
    }
}
