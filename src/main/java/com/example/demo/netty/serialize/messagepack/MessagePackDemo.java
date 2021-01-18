package com.example.demo.netty.serialize.messagepack;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉
 * MessagePack 序列化，反序列化方案
 *
 * @author bf
 * @create 2018/3/15
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MessagePackDemo {

    public static void main(String[] args) throws IOException {
        List<String> data = new ArrayList<>();
        data.add("aaaaaaaaaa");
        data.add("bbbbbbbbb");
        data.add("ccccccccc");

        // 创建序列化对象
        MessagePack messagePack = new MessagePack();

        // 序列化
        byte[] raw = messagePack.write(data);

        System.out.println("序列化 length: " + raw.length);

        // 反序列化
        List<String> read = messagePack.read(raw, Templates.tList(Templates.TString));
        for (String value: read){
            System.out.println("反序列化数据： " + value);
        }



    }
}
