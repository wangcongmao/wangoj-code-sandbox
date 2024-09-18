package com.wangcm.wangojcodesandbox.security;

import java.security.Permission;

public class DefaultSecurityManager extends SecurityManager {

    /**
     * 检查所有的权限
     * @param perm
     */
    @Override
    public void checkPermission(Permission perm) {
        System.out.println("默认不做任何限制");
        System.out.println(perm);
        super.checkPermission(perm);
        throw new SecurityException("权限不足");
    }



}
