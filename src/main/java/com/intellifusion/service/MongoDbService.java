package com.intellifusion.service;

public interface MongoDbService {
    void selectOne();

    void bulkInsertData() throws Exception;
}
