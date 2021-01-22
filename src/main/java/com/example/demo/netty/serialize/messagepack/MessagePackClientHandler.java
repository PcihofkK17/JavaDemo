package com.example.demo.netty.serialize.messagepack;

import com.example.demo.netty.entity.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;

/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author bf
 * @create 2018/3/15
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MessagePackClientHandler extends ChannelInboundHandlerAdapter {

    private int count;

    public MessagePackClientHandler(int count) {
        this.count = count;
    }

    /**
     * 客户端像服务的推送 对象（在 netty 客户端配置了解码和编码规则）
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo[] userInfos = userInfo();
        for (UserInfo userInfo : userInfos){
            ctx.write(userInfo);
        }
        ctx.flush();
        System.out.println("----------- send over ------------");
    }

    private UserInfo[] userInfo() {
        UserInfo[] userInfos = new UserInfo[count];
        UserInfo userInfo = null;

        for (int i = 0; i < count; i++) {
            userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("ABC USERNAME --------> " + i);
            userInfo.setUserId(UUID.randomUUID().toString());
            userInfos[i] = userInfo;
        }
        return userInfos;
    }

    /**
     * 接收服务端发送过来的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        System.out.println(msg);
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
