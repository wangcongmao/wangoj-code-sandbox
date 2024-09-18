package com.wangcm.wangojcodesandbox.security;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestSecurityManager {

    public static void main(String[] args) {
        System.setSecurityManager(new MySecurityManager());

        List<String> list = FileUtil.readLines(new File("G:\\fxiaoke\\mianShi\\OJ\\standard\\stage4\\wangoj-code-sandbox\\src\\main\\resources\\testCode\\simpleCompute\\Main.java"), StandardCharsets.UTF_8);

        System.out.println(list);
    }
}
