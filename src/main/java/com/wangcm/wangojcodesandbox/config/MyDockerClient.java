package com.wangcm.wangojcodesandbox.config;

import cn.hutool.json.JSONUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;


@Data
public class MyDockerClient {


    private DockerClient dockerClient;

    public MyDockerClient() {

        DockerClientBuilder dockerClientBuilder = DockerClientBuilder.getInstance();
        dockerClient = dockerClientBuilder.build();

//        Info info = dockerClient.infoCmd().exec();
//        String infoStr = JSONObject.toJSONString(info);
//        JSONObject jsonObject = new JSONObject();
//        String infoStr = JSONUtil.toJsonStr(info);
//        System.out.println("docker环境信息");
//        System.out.println(infoStr);
    }

    /**
     * 启动容器
     * @param containerId
     */
    public void startContainer(String containerId) {
        // 启动容器
        dockerClient.startContainerCmd(containerId).exec();
    }

    /**
     * 查看日志
     * @param containerId
     */
    public void showLog(String containerId) {
        // 查看日志
        LogContainerResultCallback logContainerResultCallback = new LogContainerResultCallback() {
            @Override
            public void onNext(Frame item) {
                System.out.println("日志：" + new String(item.getPayload()));
                super.onNext(item);
            }
        };
        try {
            dockerClient.logContainerCmd(containerId)
                    .withStdErr(true)
                    .withStdOut(true)
                    .exec(logContainerResultCallback)
                    .awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除容器
     * @param containerId
     */
    public void removeContainer(String containerId) {
        // 删除容器
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();
    }

    /**
     * 删除镜像
     * @param image
     */
    public void removeImage(String image) {
        // 删除镜像
        dockerClient.removeImageCmd(image).exec();
    }


    /**
     * 查看容器状态
     */
    public void showContainerState() {
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
    public String createContainer(String image, String path, String volumePath) {
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        // 容器配置
        HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(100*1000*1000L);
        hostConfig.withMemorySwap(0L);
        hostConfig.withCpuCount(1L);
        // 创建容器时指定 volumn 映射，把本地文件同步到容器中，可以让容器访问，也可以叫容器挂载目录
        // 将Windows路径转换为Docker兼容的路径
//        String dockerPath = path.replace("\\", "/");
        hostConfig.setBinds(new Bind(path, new Volume("/app")));
        CreateContainerResponse createContainerResponse = containerCmd
                .withNetworkDisabled(true)
                .withReadonlyRootfs(true)
                .withHostConfig(hostConfig)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withTty(true)
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
    public void pullImage(String image) throws InterruptedException {
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
    /*public static DockerClient getNewConnection() {
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
    }*/
}
