package com.wangcm.wangojcodesandbox.docker;

import cn.hutool.json.JSONUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.wangcm.wangojcodesandbox.config.MyDockerClient;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

public class DockerDemo {

    @Resource
    MyDockerClient myDockerClient;

    static DockerClient dockerClient = connect();



    public static void main(String[] args) throws InterruptedException {
        // 获取默认的 Docker Client
//        DockerClient dockerClient = DockerClientBuilder.getInstance().build();


//        DockerClientImpl dockerClient = DockerClientImpl.getInstance(c);


        String image = "nginx:latest";
//        pullImage(image);

        // 创建容器
        String containerId = createContainer(image);


        // 查看容器状态
//        showContainerState();

        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();

        // 查看日志
        LogContainerResultCallback logContainerResultCallback = new LogContainerResultCallback() {
            @Override
            public void onNext(Frame item) {
                System.out.println("日志：" + new String(item.getPayload()));
                super.onNext(item);
            }
        };
        dockerClient.logContainerCmd(containerId)
                .withStdErr(true)
                .withStdOut(true)
                .exec(logContainerResultCallback)
                .awaitCompletion();


        // 删除容器
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();

        // 删除镜像
        dockerClient.removeImageCmd(image).exec();


    }

    public static void showContainerState() {
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        List<Container> containerList = listContainersCmd.withShowAll(true).exec();
        for (Container container : containerList) {
            System.out.println(container);
        }
    }

    /**
     * 创建容器
     * @param image
     */
    public static String createContainer(String image) {
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        CreateContainerResponse createContainerResponse = containerCmd
                .withCmd("echo", "Hello Docker")
                .exec();
        System.out.println(createContainerResponse);
        return createContainerResponse.getId();
    }


    /**
     * 拉取镜像
     * @param image
     * @throws InterruptedException
     */
    public static void pullImage(String image) throws InterruptedException {
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
        PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
            @Override
            public void onNext(PullResponseItem item) {
                System.out.println("下载镜像"+item.getStatus());
                super.onNext(item);
            }
        };
        pullImageCmd
                .exec(pullImageResultCallback)
                .awaitCompletion();
        System.out.println("下载完成");
    }

    /**
     * 连接docker服务器
     *
     * @return
     */
    public static DockerClient connect() {
        // 配置docker CLI的一些选项
        DefaultDockerClientConfig config = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerTlsVerify(false)
                .withDockerHost("tcp://124.70.97.179:2375")
                // 与docker版本对应，参考https://docs.docker.com/engine/api/#api-version-matrix
                // 或者通过docker version指令查看api version
                .withApiVersion("1.41")
//                .withRegistryUrl(REGISTRY_URL)
                .build();

        // 创建DockerHttpClient
        DockerHttpClient httpClient = new ApacheDockerHttpClient
                .Builder()
                .dockerHost(config.getDockerHost())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        Info info = dockerClient.infoCmd().exec();
//        String infoStr = JSONObject.toJSONString(info);
//        JSONObject jsonObject = new JSONObject();
        String infoStr = JSONUtil.toJsonStr(info);
        System.out.println("docker环境信息");
        System.out.println(infoStr);
        return dockerClient;
    }
}
