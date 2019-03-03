package com.protocal;

import org.apache.mina.common.IoBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

public class ProtocalEncoder extends ProtocolEncoderAdapter {
    private final Charset charset;

    public ProtocalEncoder(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {
        ProtocalPack value = (ProtocalPack) message;
        IoBuffer buffer = IoBuffer.allocate(value.getLength());
        buffer.setAutoExpand(true);
        buffer.putInt(value.getLength());
        buffer.put(value.getFlag());
        if (value.getContent() != null){
            buffer.put(value.getContent().getBytes());
        }
        buffer.flip();
        out.write(buffer);
    }
}
