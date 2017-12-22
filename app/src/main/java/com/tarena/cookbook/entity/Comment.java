package com.tarena.cookbook.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by tarena on 2017/12/22
 */

public class Comment extends BmobObject {
    //评论内容
    private String content;
    //评论用户
    private MyUser user;
    //评论所属消息Id
    private String sourceMesId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public String getSourceMesId() {
        return sourceMesId;
    }

    public void setSourceMesId(String sourceMesId) {
        this.sourceMesId = sourceMesId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +
                ", user=" + user +
                ", sourceMesId='" + sourceMesId + '\'' +
                '}';
    }
}
