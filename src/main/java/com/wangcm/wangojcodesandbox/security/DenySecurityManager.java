package com.wangcm.wangojcodesandbox.security;

import java.security.Permission;

public class DenySecurityManager extends SecurityManager {

    /**
     * 禁止所有的权限
     * @param perm
     */
    @Override
    public void checkPermission(Permission perm) {
//        throw new SecurityException("权限不足");
//        super.checkPermission(perm);

    }



}
