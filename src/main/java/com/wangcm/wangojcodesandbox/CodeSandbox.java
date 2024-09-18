package com.wangcm.wangojcodesandbox;

import com.wangcm.wangojcodesandbox.model.ExecuteCodeRequest;
import com.wangcm.wangojcodesandbox.model.ExecuteCodeResponse;


/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
