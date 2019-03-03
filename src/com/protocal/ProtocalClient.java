package com.protocal;

import org.apache.mina.common.*;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class ProtocalClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 7080;
    static long counter = 0;
    final static int fil = 100;
    static long start = 0;

    public static void main(String[] args) {
        start = System.currentTimeMillis();
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("coderc", new ProtocolCodecFilter(new ProtocalFactory(Charset.forName("UTF-8"))));
        connector.getSessionConfig().setReadBufferSize(1024);
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,10);
        connector.setHandler(new MyClientHandler());
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(HOST, PORT));
        connectFuture.addListener(new IoFutureListener<ConnectFuture>() {
            @Override
            public void operationComplete(ConnectFuture future) {
                if (future.isConnected()){
                    IoSession session = future.getSession();
                    senddata(session);
                }
            }
        });
    }

    public static void senddata(IoSession session){
        for (int i = 0; i < fil; i++) {
            String content = "gaoxiong:" + i;
            ProtocalPack pack = new ProtocalPack((byte) i, content);
            session.write(pack);
            System.out.println("客户端发送数据:" + pack);
        }
    }
}
