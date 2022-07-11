package com.intellifusion.controller;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.intellifusion.entity.TestEntity;
import com.intellifusion.service.PersonLikeSearch;
import com.intellifusion.service.TestService;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscAtomicArrayQueue;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping("/hcf01/{id}")
    public TestEntity test(@PathVariable("id") Long id) {
        return testService.TestHcf(id);
    }

    @RequestMapping(path = "/bulkInsert")
    public void bulkInsert() throws CloneNotSupportedException {
        for (int i = 0; i < 900; i++) {
            testService.bulkInsert();
        }
    }

    @RequestMapping(path = "/bulkSaveES")
    public void bulkSaveES() {
        testService.bulkSaveES();
    }

    @RequestMapping(path = "/reIndex")
    public void reIndex() {
        testService.reIndex();
    }

    @RequestMapping(path = "/SearchES/{name}/{sale}")
    public List<TestEntity> SearchES(@PathVariable String name,@PathVariable Double sale) {
        return testService.SearchES(name,sale);
    }


    @RequestMapping(path = "/aggSelect/{group}/{name}")
    public List<HashMap<String, Long>> aggSelect(@PathVariable Integer group,@PathVariable String name) {
        return testService.aggSelect(group,name);
    }

    @RequestMapping(path = "ip")
    public boolean IpAnalysis(HttpServletRequest request) throws IOException {
        if(null == request.getHeader("Referer") || !request.getHeader("Referer").contains("www.baidu.com"))
        {
            System.out.println("neiwang");
        }
//        System.out.println(request.getServerName()+":"+request.getServerPort());
//        System.out.println(request.getRemoteAddr());
//        System.out.println("远程地址："+request.getRemoteHost()+":"+request.getRemotePort());
//        System.out.println(request.getRequestURI());
//        System.out.println(request.getPathInfo());
        Runtime runtime = Runtime.getRuntime();
        System.out.println(runtime.totalMemory()/1024/1024+"/"+runtime.maxMemory()/1024/1024+"/"+runtime.freeMemory()/1024/1024);
        InputStream inputStream = runtime.exec("ping " + request.getRemoteAddr()).getInputStream();
        List list = IOUtils.readLines(inputStream, "GBK");
        System.out.println(list);

        System.out.println();


        String pattern = "((127\\.0|192\\.168|172\\.([1][6-9]|[2]\\d|3[01]))"+"(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}|"+"^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3})";
        Pattern reg = Pattern.compile(pattern);
        Matcher match = reg.matcher(request.getRemoteAddr());
        return match.find();
    }


    @RequestMapping(path = "/testDf")
    public String[] testDf() throws IOException {
//        Process process = Runtime.getRuntime().exec("df -hl");
//        List<String> lines = IoUtil.readLines(process.getInputStream(), "GBK",new ArrayList<>());
//        for (int i = 0; i < lines.size(); i++) {
//            String[] split = lines.get(i).split("\\s+");
//            if (split[split.length - 1].equalsIgnoreCase("/"))
//                return lines.get(i).split("\\s+");
//        }
//        //返回null代表未找到挂载的硬盘
        return null;
    }


    public static PersonLikeSearch<Integer> likeSearch = new PersonLikeSearch<>();

    @RequestMapping(value = "init")
    public void initTestData(){
        likeSearch.put(1,"hcf");
        likeSearch.put(2,"cf");
        likeSearch.put(1,"ch");
    }


    @RequestMapping(value = "search/{word}")
    public Object search(@PathVariable String word){
        return likeSearch.search(word,2);
    }

}
