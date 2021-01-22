package com.example.demo.netty.websocket;

/**
 * 〈一句话功能简述〉
 * 消息发送回调接口
 *
 * @author bf
 * @create 2018/3/19
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface WechatCallBack {
	
	// 服务端发送消息给客户端
	void send(Request request) throws Exception;
	
}