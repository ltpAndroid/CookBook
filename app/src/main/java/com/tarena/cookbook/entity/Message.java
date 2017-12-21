package com.tarena.cookbook.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class Message extends BmobObject {
    private String detail;
    private List<String> imagePaths;
    private MyUser user;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Message{" +
                "detail='" + detail + '\'' +
                ", imagePaths=" + imagePaths +
                ", user=" + user +
                '}';
    }
}
