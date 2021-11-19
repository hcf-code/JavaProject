package com.intellifusion.controller;

import com.intellifusion.service.MongoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mongo")
public class MongoDbController {

    @Autowired
    MongoDbService mongoDbService;


    @RequestMapping("selectOne")
    public void selectOne(){
        mongoDbService.selectOne();
    }

    @RequestMapping("bulkInsertData")
    public void bulkInsertData() throws Exception {
        mongoDbService.bulkInsertData();
    }
}
