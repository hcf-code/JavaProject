package com.intellifusion.controller;

import com.intellifusion.mapper.HcfMapper;
import com.intellifusion.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * excel
 *
 * @author SanheDashen
 * @date 2022/04/02 10:11
 */
@RestController
@RequestMapping("hcf")
public class ExcelController {

    @Autowired
    HcfMapper hcfMapper;


    @RequestMapping("test")
    public void exportExcel() throws ExecutionException, InterruptedException {
        CompletableFuture<List<String>> async = CompletableFuture.supplyAsync(() -> {
            return hcfMapper.selectTest();
        });
        System.out.println(async.get());
    }
}
