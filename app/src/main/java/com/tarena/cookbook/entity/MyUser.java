package com.tarena.cookbook.entity;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/11/29 0029.
 */

public class MyUser extends BmobUser {
    private String nickname;

    private String headPath;

    private String signature;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPath() {
        return headPath;
    }

    public void setHeadPath(String headPath) {
        this.headPath = headPath;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
