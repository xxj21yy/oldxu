package com.qfy.base.dto;

import com.qfy.common.tool.Base;

public class BaseEntry extends Base {

    /**
     * 有效
     */
    public static int YN = 0;
    /**
     * 无效
     */
    public static int NOT_YN = 1;
    /**
     * 创建人
     */
    private long createUser;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改人
     */
    private long updateUser;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 是否有效 默认有效
     */
    private int yn;

    public long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(long createUser) {
        this.createUser = createUser;
    }

    public long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(long updateUser) {
        this.updateUser = updateUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getYn() {
        return yn;
    }

    public void setYn(int yn) {
        this.yn = yn;
    }
}
