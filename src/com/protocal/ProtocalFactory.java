package com.protocal;

import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

public class ProtocalFactory implements ProtocolCodecFactory {
    private final ProtocalDecoder decoder;
    private final ProtocalEncoder encoder;

    public ProtocalFactory(Charset charset) {
        encoder = new ProtocalEncoder(charset);
        decoder = new ProtocalDecoder(charset);
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return decoder;
    }
}
