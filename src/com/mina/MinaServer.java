package com.mina;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import static org.apache.mina.common.IdleStatus.*;

public class MinaServer {
    static int PORT=7080;
    static IoAcceptor accept = null;

    public static void main(String[] args) {
        try {
            accept = new NioSocketAcceptor();
            //设置编码过滤器
            accept.getFilterChain().addLast("codec",new ProtocolCodecFilter(
                    new TextLineCodecFactory(
                            Charset.forName("UTF-8"),
                            LineDelimiter.WINDOWS.getValue(),
                            LineDelimiter.WINDOWS.getValue()
                    )
            ));
            accept.getFilterChain().addFirst("filter", new MyServerFilter());
            accept.getSessionConfig().setReadBufferSize(1024);
            accept.getSessionConfig().setIdleTime(BOTH_IDLE,10);
            accept.setHandler(new MyServerHandler());
            accept.bind(new InetSocketAddress(PORT));
            System.out.println("Server ->" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
