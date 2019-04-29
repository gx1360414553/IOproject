package com.netty.client;

import org.jboss.netty.channel.*;

import java.net.InetSocketAddress;

public class HiHandler extends SimpleChannelHandler {
    /**
     * 接收消息
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("messageReceived");
        /*ChannelBuffer message = (ChannelBuffer) e.getMessage();
        String s = new String(message.array());*/
        String message = (String) e.getMessage();
        System.out.println(message);
        //回写数据
        //ChannelBuffer channelBuffer = ChannelBuffers.copiedBuffer("hi".getBytes());
        //ctx.getChannel().write("hi");

        super.messageReceived(ctx, e);
    }

    /**
     * 捕获异常
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("exceptionCaught");
        super.exceptionCaught(ctx, e);
    }

    /**
     * 新连接
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.getChannel().getRemoteAddress();
        String clientIP = insocket.getAddress().getHostAddress();
        System.out.println(clientIP);

        System.out.println("channelConnected");
        super.channelConnected(ctx, e);
    }

    /**
     * 必须是连接已经建立,关闭通道的时候才会触发
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelDisconnected");
        super.channelDisconnected(ctx, e);
    }

    /**
     * channel关闭的时候触发
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed");
        super.channelClosed(ctx, e);
    }
}
