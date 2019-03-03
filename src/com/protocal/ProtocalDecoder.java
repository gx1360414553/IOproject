package com.protocal;

import org.apache.mina.common.AttributeKey;
import org.apache.mina.common.IoBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ProtocalDecoder implements ProtocolDecoder {
    private final AttributeKey CONTEXT = new AttributeKey(this.getClass(), "context");
    private final Charset charset;
    private int maxPackLength = 100;

    public ProtocalDecoder() {
        this(Charset.defaultCharset());
    }

    public ProtocalDecoder(Charset charset) {
        this.charset = charset;
    }



    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        final int packHeadLength = 5;
        Context ctx = this.getContext(ioSession);
        ctx.append(ioBuffer);
        IoBuffer buf = ctx.getBuf();
        buf.flip();
        while (buf.remaining() >= packHeadLength){
            buf.mark();
            int length = buf.getInt();
            byte flag = buf.get();
            if (length < 0 || length > maxPackLength){
                buf.reset();
                break;
            }else if(length >= packHeadLength && length - packHeadLength <= buf.remaining()){
                int oldLimit = buf.limit();
                buf.limit(buf.position() + length - packHeadLength);
                String context = buf.getString(ctx.getDecoder());
                buf.limit(oldLimit);
                ProtocalPack pack = new ProtocalPack(flag, context);
                protocolDecoderOutput.write(pack);
            }else {//半包
                buf.clear();
                break;
            }
        }
        if(buf.hasRemaining()){
            IoBuffer temp = IoBuffer.allocate(maxPackLength).setAutoExpand(true);
            temp.put(buf);
            temp.flip();
            buf.reset();
            buf.put(temp);
        }else {
            buf.reset();
        }
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {
        Context ctx = (Context) ioSession.getAttribute(CONTEXT);
        if (ctx != null){
            ioSession.removeAttribute(CONTEXT);
        }
    }

    public Context getContext(IoSession session){
       Context ctx = (Context) session.getAttribute(CONTEXT);
       if (ctx == null){
           ctx = new Context();
           session.setAttribute(CONTEXT, ctx);
       }
       return ctx;
    }

    private class Context{
        private final CharsetDecoder decoder;
        private IoBuffer buf;

        public void append(IoBuffer in){
            this.getBuf().put(in);
        }

        public void rest(){
            decoder.reset();
        }

        public CharsetDecoder getDecoder() {
            return decoder;
        }

        public IoBuffer getBuf() {
            return buf;
        }

        public void setBuf(IoBuffer buf) {
            this.buf = buf;
        }

        public Context() {
            decoder = charset.newDecoder();
            buf = IoBuffer.allocate(80).setAutoExpand(true);
        }

    }

    public AttributeKey getCONTEXT() {
        return CONTEXT;
    }

    public Charset getCharset() {
        return charset;
    }

    public int getMaxPackLength() {
        return maxPackLength;
    }

    public void setMaxPackLength(int maxPackLength) {
        if (maxPackLength < 0){
            throw new IllegalArgumentException("maxPackLength参数:" + maxPackLength);
        }
        this.maxPackLength = maxPackLength;
    }
}
