/***********************************************************************
 * Module:  TempData.java
 * Author:  Administrator
 * Purpose: Defines the Class TempData
 ***********************************************************************/

package com.qfy.base.dto;

import java.io.Serializable;

public class BaseDto implements Serializable, Cloneable {
    /**
     * 
     */
    private static final long serialVersionUID = -7911278621742574401L;
    /**
     * 用户登陆令牌
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "BaseDataDto [token=" + token + "]";
    }
}