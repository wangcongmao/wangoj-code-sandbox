package com.wangcm.wangojcodesandbox.security;

import java.security.Permission;

public class MySecurityManager extends SecurityManager {

    /**
     * 禁止所有的权限
     * @param perm
     */
    @Override
    public void checkPermission(Permission perm) {
//        throw new SecurityException("权限不足");
        super.checkPermission(perm);

    }

    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec权限不足"+cmd);
    }

    @Override
    public void checkRead(String file) {

        throw new SecurityException("checkRead权限不足"+file);
    }

    @Override
    public void checkWrite(String file) {
        throw new SecurityException("checkWrite权限不足"+file);
    }

    @Override
    public void checkDelete(String file) {
        throw new SecurityException("checkDelete权限不足"+file);
    }

    @Override
    public void checkConnect(String host, int port) {
        throw new SecurityException("checkConnect权限不足"+host + ":"+port);
    }
}
