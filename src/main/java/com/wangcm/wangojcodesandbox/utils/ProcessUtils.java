package com.wangcm.wangojcodesandbox.utils;

import cn.hutool.core.util.StrUtil;
import com.wangcm.wangojcodesandbox.model.ExecuteMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 进程工具类
 */
public class ProcessUtils {

    /**
     * 执行进程并获取信息
     * @param runProcess
     * @return
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            // 编译成功
            if (exitValue == 0) {
                System.out.println(opName + "成功");
                // 分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 逐行读取
                String compileOutputLine ;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(compileOutputLine);
                }
                String res = StringUtils.join(outputStrList, "\n");
                executeMessage.setMessage(res.toString());
            } else {
                // 异常退出
                System.out.println(opName + "失败，错误码" + exitValue);

                // 分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 逐行读取
                String compileOutputLine ;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(compileOutputLine);
                }
                String res = StringUtils.join(outputStrList, "\n");
                executeMessage.setMessage(res.toString());
                // 分批获取进程的错误输出
                BufferedReader errorBufferReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                List<String> errorOutputStrList = new ArrayList<>();
                // 逐行读取
                String errorCompileOutputLine ;
                while ((errorCompileOutputLine = errorBufferReader.readLine()) != null) {
                    errorOutputStrList.add(errorCompileOutputLine);
                }
                String errRes = StringUtils.join(errorOutputStrList, "\n");
                executeMessage.setErrorMessage(errRes);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return executeMessage;
    }

    /**
     * 执行交互式进程并获取信息
     * @param runProcess
     * @return
     */
    public static ExecuteMessage runInterProcessAndGetMessage(Process runProcess, String opName, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            StopWatch stopWatch = new StopWatch();

            stopWatch.start();

            InputStream inputStream = runProcess.getInputStream();
            // 向控制台输入程序
            OutputStream outputStream = runProcess.getOutputStream();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

            String[] s = args.split(" ");

            outputStreamWriter.write(StrUtil.join("\n", s)+"\n");
            // 相当于按了回车
            outputStreamWriter.flush();

            // 分批获取进程的正常输出
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            // 逐行读取
            String compileOutputLine ;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            executeMessage.setMessage(compileOutputStringBuilder.toString());

            outputStream.close();
            outputStreamWriter.close();
            inputStream.close();

            runProcess.destroy();

            stopWatch.stop();

            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());

        }  catch (IOException e) {
            e.printStackTrace();
        }

        return executeMessage;
    }
}
