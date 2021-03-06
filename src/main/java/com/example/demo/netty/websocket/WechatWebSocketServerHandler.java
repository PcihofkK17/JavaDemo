package com.example.demo.netty.websocket;

import com.google.gson.JsonSyntaxException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * 〈一句话功能简述〉
 * WebSocket server handler
 *
 * WebScoket原理: https://www.zhihu.com/question/20215561
 *
 * @author bf
 * @create 2018/3/19
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class WechatWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {


	private static final Logger LOG = Logger.getLogger(WechatWebSocketServerHandler.class.getName());
	
	private WebSocketServerHandshaker handshaker;
	private ChannelHandlerContext ctx;
	private String sessionId;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) { // 传统的HTTP接入
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) { // WebSocket接入
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}




	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOG.error("WebSocket异常", cause);
		ctx.close();
		LOG.info(sessionId + " 	注销");
		WechatService.logout(sessionId); // 注销
		WechatService.notifyDownline(sessionId); // 通知有人下线
	}

	/**
	 * 处理Http请求，完成WebSocket握手<br/>
	 * 注意：WebSocket连接第一次请求使用的是Http，Http形式进入，后面就是WebSocket方式的连接了
	 *
	 *
	 * @param ctx
	 * @param request
	 * @throws Exception
	 */
	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		/**
		 * 如果HTTP解码失败，返回HHTP异常
		 * DecoderResult: Http 请求解析结果
		 * Upgrade: 再判断是否是 websocket 请求，如果是websocket,就返回
		 */
		if (request.decoderResult().isFailure() || (!"websocket".equals(request.headers().get("Upgrade")))) {
			sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}

		/**
		 * 正常WebSocket的Http连接请求，构造握手响应返回
		 * WebSocketServerHandshakerFactory: 创建一个 WebSocket 工厂类(网络套接字)
		 * 	webSocketURL: 网络套接字通信URL,后续的网络套接字将全部发送到这个 URL 上面
		 * 	subprotocols: 支持的协议的CSV。 如果不支持子协议，则为空。
		 *  allowExtensions: 允许在web套接字的保留位中使用扩展名？？？？？
		 */
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				"ws://localhost:9090/websocket", null, false);
		// 创建全局 WebSocket
		handshaker = wsFactory.newHandshaker(request);
		if (handshaker == null) { // 无法处理的websocket版本
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			// 向客户端发送websocket握手,完成握手
			handshaker.handshake(ctx.channel(), request);
			// 记录管道处理上下文，便于服务器推送数据到客户端
			this.ctx = ctx;
		}
	}

	/**
	 * 处理Socket请求
	 * @param ctx
	 * @param frame
	 * @throws Exception 
	 */
	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
		// 判断是否是关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		// 判断是否是Ping消息
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 当前只支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException("当前只支持文本消息，不支持二进制消息");
		}
		
		// 处理来自客户端的WebSocket请求
		try {
			Request request = Request.create(((TextWebSocketFrame)frame).text());
			Response response = new Response();
			response.setServiceId(request.getServiceId());
			if (CODE.online.code.intValue() == request.getServiceId()) { // 客户端注册
				String requestId = request.getRequestId();
				if (StringUtils.isBlank(requestId)) {
					response.setIsSucc(false).setMessage("requestId不能为空");
					return;
				} else if (StringUtils.isBlank(request.getName())) {
					response.setIsSucc(false).setMessage("name不能为空");
					return;
				} else if (WechatService.bananaWatchMap.containsKey(requestId)) {
					response.setIsSucc(false).setMessage("您已经注册了，不能重复注册");
					return;
				}
				if (!WechatService.register(requestId, new WechatService(ctx, request.getName()))) {
					response.setIsSucc(false).setMessage("注册失败");
				} else {
					response.setIsSucc(true).setMessage("注册成功");
					
					WechatService.bananaWatchMap.forEach((reqId, callBack) -> {
						response.getHadOnline().put(reqId, ((WechatService)callBack).getName()); // 将已经上线的人员返回
						
						if (!reqId.equals(requestId)) {
							Request serviceRequest = new Request();
							serviceRequest.setServiceId(CODE.online.code);
							serviceRequest.setRequestId(requestId);
							serviceRequest.setName(request.getName());
							try {
								callBack.send(serviceRequest); // 通知有人上线
							} catch (Exception e) {
								LOG.warn("回调发送消息给客户端异常", e);
							}
						}
					});
				}
				sendWebSocket(response.toJson());
				this.sessionId = requestId; // 记录会话id，当页面刷新或浏览器关闭时，注销掉此链路
			} else if (CODE.send_message.code.intValue() == request.getServiceId()) { // 客户端发送消息到聊天群
				String requestId = request.getRequestId();
				if (StringUtils.isBlank(requestId)) {
					response.setIsSucc(false).setMessage("requestId不能为空");
				} else if (StringUtils.isBlank(request.getName())) {
					response.setIsSucc(false).setMessage("name不能为空");
				} else if (StringUtils.isBlank(request.getMessage())) {
					response.setIsSucc(false).setMessage("message不能为空");
				} else {
					response.setIsSucc(true).setMessage("发送消息成功");
					
					WechatService.bananaWatchMap.forEach((reqId, callBack) -> { // 将消息发送到所有机器
						Request serviceRequest = new Request();
						serviceRequest.setServiceId(CODE.receive_message.code);
						serviceRequest.setRequestId(requestId);
						serviceRequest.setName(request.getName());
						serviceRequest.setMessage(request.getMessage());
						try {
							callBack.send(serviceRequest);
						} catch (Exception e) {
							LOG.warn("回调发送消息给客户端异常", e);
						}
					});
				}
				sendWebSocket(response.toJson());
			} else if (CODE.downline.code.intValue() == request.getServiceId()) { // 客户端下线
				String requestId = request.getRequestId();
				if (StringUtils.isBlank(requestId)) {
					sendWebSocket(response.setIsSucc(false).setMessage("requestId不能为空").toJson());
				} else {
					WechatService.logout(requestId);
					response.setIsSucc(true).setMessage("下线成功");
					
					WechatService.notifyDownline(requestId); // 通知有人下线
					
					sendWebSocket(response.toJson());
				}
				
			} else {
				sendWebSocket(response.setIsSucc(false).setMessage("未知请求").toJson());
			}
		} catch (JsonSyntaxException e1) {
			LOG.warn("Json解析异常", e1);
		} catch (Exception e2) {
			LOG.error("处理Socket请求异常", e2);
		}
	}

	/**
	 * Http返回
	 * @param ctx
	 * @param request
	 * @param response
	 */
	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {
		// 返回应答给客户端
		if (response.status().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
			response.content().writeBytes(buf);
			buf.release();
			HttpUtil.setContentLength(response, response.content().readableBytes());
		}

		// 如果是非Keep-Alive，关闭连接
		/**
		 * 非 Keep-Alive 或者，不是200 状态码的话，关闭连接
		 * Keep-Alive： Http1.1，新增的东西，可以在请求中发送多个 request ,返回多个 response, 只有这样，才能使用 WebSocket
		 * 非200状态码，同样也关闭
		 */
		ChannelFuture f = ctx.channel().writeAndFlush(response);
		if (!HttpUtil.isKeepAlive(request) || response.status().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	/**
	 * WebSocket返回
	 */
	public void sendWebSocket(String msg) throws Exception {
		if (this.handshaker == null || this.ctx == null || this.ctx.isRemoved()) {
			throw new Exception("尚未握手成功，无法向客户端发送WebSocket消息");
		}
		this.ctx.channel().write(new TextWebSocketFrame(msg));
		this.ctx.flush();
	}

}