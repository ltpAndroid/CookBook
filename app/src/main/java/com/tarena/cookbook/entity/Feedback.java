package com.tarena.cookbook.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/12/25
 */

public class Feedback extends BmobObject {
    private String describe;
    private String contact;
    private MyUser user;
    private List<String> imgs;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "describe='" + describe + '\'' +
                ", contact='" + contact + '\'' +
                ", user=" + user +
                ", imgs=" + imgs +
                '}';
    }
}
