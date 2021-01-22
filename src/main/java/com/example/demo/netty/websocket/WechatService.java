package com.example.demo.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈一句话功能简述〉
 * 通过本地模拟用户登录和退出
 *
 * @author bf
 * @create 2018/3/19
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class WechatService implements WechatCallBack {
    private static final Logger LOG = Logger.getLogger(WechatService.class);
	
	public static final Map<String, WechatCallBack> bananaWatchMap = new ConcurrentHashMap<String, WechatCallBack>(); // <requestId, callBack>
	
	private ChannelHandlerContext ctx;
	private String name;
	
	public WechatService(ChannelHandlerContext ctx, String name) {
		this.ctx = ctx;
		this.name = name;
	}

	public static boolean register(String requestId, WechatCallBack callBack) {
		if (StringUtils.isBlank(requestId) || bananaWatchMap.containsKey(requestId)) {
			return false;
		}
		bananaWatchMap.put(requestId, callBack);
		return true;
	}
	
	public static boolean logout(String requestId) {
		if (StringUtils.isBlank(requestId) || !bananaWatchMap.containsKey(requestId)) {
			return false;
		}
		bananaWatchMap.remove(requestId);
		return true;
	}
	
	@Override
	public void send(Request request) throws Exception {
		if (this.ctx == null || this.ctx.isRemoved()) {
			throw new Exception("尚未握手成功，无法向客户端发送WebSocket消息");
		}
		this.ctx.channel().write(new TextWebSocketFrame(request.toJson()));
		this.ctx.flush();
	}
	
	
	/**
	 * 通知所有机器有机器下线
	 * @param requestId
	 */
	public static void notifyDownline(String requestId) {
		WechatService.bananaWatchMap.forEach((reqId, callBack) -> { // 通知有人下线
			Request serviceRequest = new Request();
			serviceRequest.setServiceId(CODE.downline.code);
			serviceRequest.setRequestId(requestId);
			try {
				callBack.send(serviceRequest);
			} catch (Exception e) {
				LOG.warn("回调发送消息给客户端异常", e);
			}
		});
	}
	
	public String getName() {
		return name;
	}

}