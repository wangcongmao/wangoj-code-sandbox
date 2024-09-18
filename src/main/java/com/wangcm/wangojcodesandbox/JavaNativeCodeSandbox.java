package com.wangcm.wangojcodesandbox;
import java.util.ArrayList;

import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.wangcm.wangojcodesandbox.model.JudgeInfo;

import java.util.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.wangcm.wangojcodesandbox.model.ExecuteCodeRequest;
import com.wangcm.wangojcodesandbox.model.ExecuteCodeResponse;
import com.wangcm.wangojcodesandbox.model.ExecuteMessage;
import com.wangcm.wangojcodesandbox.security.DefaultSecurityManager;
import com.wangcm.wangojcodesandbox.security.DenySecurityManager;
import com.wangcm.wangojcodesandbox.utils.ProcessUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * java原生实现，直接复用模板方法
 */
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }


}
