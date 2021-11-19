package com.intellifusion.dfs;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
public class FileConfig {

    /**
     *   * 解决上传文件过大导致异常的问题。
     *   * the request was rejected because its size (170982031) exceeds the
     * configured
     *   * @return
     *   
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxRequestSize(DataSize.ofBytes(200 * 1048576L));
        factory.setMaxFileSize(DataSize.ofBytes(200 * 1048576L));
        return factory.createMultipartConfig();
    }

}
