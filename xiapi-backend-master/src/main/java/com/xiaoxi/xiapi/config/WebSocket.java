package com.xiaoxi.xiapi.config;

import cn.hutool.json.JSONUtil;
import com.xiaoxi.common.model.entity.User;
import com.xiaoxi.xiapi.model.dto.ws.Message;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.xiaoxi.common.constant.UserConstant.USER_LOGIN_STATE;

@Component
@ServerEndpoint(value = "/websocket", configurator = HttpSessionConfig.class)
public class WebSocket {
    public static Map<String, Session> sessions = new ConcurrentHashMap<>();

    //
    public static List<User> userList =new ArrayList<>();

    private HttpSession httpSession;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        User user = (User) this.httpSession.getAttribute(USER_LOGIN_STATE);
        //如果不存在再添加
        if(!userList.contains(user)){
            userList.add(user);
        }
        sessions.put(user.getUserAccount(), session);
        System.out.println("WebSocket opened for user: " + user.getUserAccount());
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        Message messageBean = JSONUtil.toBean(message, Message.class);
        String targetUser = messageBean.getTargetUser();
        String sendMessage = messageBean.getMessage();
        Session session  = sessions.get(targetUser);
        if (session != null) {
            session.getBasicRemote().sendText(sendMessage);
        }
    }

    @OnClose
    public void onClose(Session session) {
        User user = (User) this.httpSession.getAttribute(USER_LOGIN_STATE);
        sessions.remove(user.getUserAccount());
        userList.remove(user);
        System.out.println("WebSocket closed for user: " + user.getUserAccount());
    }
}
