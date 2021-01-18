package com.example.demo.netty.entity;

import org.msgpack.annotation.Message;

/**
 * 〈一句话功能简述〉
 * ******* 被 MessagePack 一定要加 @Message 注解
 *
 * @author bf
 * @create 2018/3/16
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Message
public class UserInfo {

    private int age;

    private String name;

    private String userId;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserInfo() {
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
