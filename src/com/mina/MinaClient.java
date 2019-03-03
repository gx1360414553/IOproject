package com.mina;


import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaClient {
    private static String host = "127.0.0.1";
    private static int port = 7080;

    public static void main(String[] args) {
        IoSession session = null;
        IoConnector connector = new NioSocketConnector();
        connector.setConnectTimeout(3000);
        //设置过滤器
        connector.getFilterChain().addLast("coderc", new ProtocolCodecFilter(
                new TextLineCodecFactory(
                        Charset.forName("UTF-8"),
                        LineDelimiter.WINDOWS.getValue(),
                        LineDelimiter.WINDOWS.getValue()
                )
        ));
        connector.getFilterChain().addFirst("filter",new MyClientFilter());
        connector.setHandler(new MyClientHandler());
        ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
        future.awaitUninterruptibly();//等待我们的连接
        session = future.getSession();
        session.write("你好!高雄");
        session.getCloseFuture().awaitUninterruptibly();//等待关闭连接
        connector.dispose();

    }
}
