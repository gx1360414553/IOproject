package com.protocal;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

public class MyServerHandler extends IoHandlerAdapter {
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("server->exceptionCaught");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        ProtocalPack pack = (ProtocalPack)message;
        System.out.println("服务端接受:" + pack);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("server->sessionIdle");
    }
}
