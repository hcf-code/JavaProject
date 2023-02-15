package com.intellifusion.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SanheDashen
 * @date 2022/07/01 14:50
 */
public interface MinIoService {
    void upload() throws Exception;

    void download() throws Exception;

    void test();

    void insert(HttpServletRequest request) throws Exception;

}
