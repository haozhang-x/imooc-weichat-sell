package cn.cloudx.weichatsell.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;


@ServerEndpoint("/websocket")
@Slf4j
@Component
public class WebSocket {
    private static CopyOnWriteArrayList<WebSocket> webSockets = new CopyOnWriteArrayList<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSockets.add(this);
        log.info("[websocket消息] 有新的连接,总数：{}", webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        log.info("[websocket消息] 连接断开,总数：{}", webSockets.size());

    }


    @OnMessage
    public void onMessage(String message) {
        log.info("[websocket消息] 收到客户端发来的消息{}", message);
    }


    public void sendMessage(String message) {
        for (WebSocket webSocket : webSockets) {
            log.info("[websocket消息] 广播消息,message={}", message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.info("[websocket消息] 广播消息发送异常,e={1}", e);
            }
        }
    }


}
