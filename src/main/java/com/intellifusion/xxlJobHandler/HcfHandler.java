package com.intellifusion.xxlJobHandler;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

@Component
public class HcfHandler {

    private static transient Logger logger = LoggerFactory.getLogger(HcfHandler.class);

    @XxlJob("HcfJobHandler")
    public void hcfJobHandler() {
        logger.info("hello word!");
        System.out.println(131313131);
    }

    public static void main(String[] args) {
        String str = null;
        try {
            if (str.equals("hcf"))
                System.out.println("hcf");
        } catch (Exception e) {
            System.out.println(e.toString());   //java.lang.NullPointerException
            e.printStackTrace();
            test(e);
            System.out.println(e.getCause().getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static PrintStream test(Exception e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        e.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 控制台打印整个错误栈  ret
        System.out.println(ret);
        return pout;
    }

}
