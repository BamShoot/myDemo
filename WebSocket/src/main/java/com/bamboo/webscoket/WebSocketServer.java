package com.bamboo.webscoket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebsocketServer
 *
 * @author xu.xudong
 * @date 2020-11-27 14:21
 */
@Slf4j
@Component
@ServerEndpoint("/websocket")
public class WebSocketServer {

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;


    /**
     * 连接成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        log.info("新的连接sessionId：{}", session.getId());
        this.session = session;
        webSocketMap.put(session.getId(), this);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        String sessionId = session.getId();
        log.info("有客户端离线：{}", sessionId);
        webSocketMap.remove(sessionId);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到消息sessionId：{}的消息：{}", session.getId(), message);
        try {
            webSocketMap.get(session.getId()).sendMessage(message + "sdsad");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
