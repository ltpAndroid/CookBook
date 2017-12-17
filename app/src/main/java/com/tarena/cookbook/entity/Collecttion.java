package com.tarena.cookbook.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class Collecttion extends BmobObject {
    private MyUser myUser;
    private String cookName;
    private boolean isLike;

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public String getCookName() {
        return cookName;
    }

    public void setCookName(String cookName) {
        this.cookName = cookName;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
