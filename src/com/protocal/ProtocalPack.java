package com.protocal;

public class ProtocalPack {
    private int length;
    private byte flag;
    private String content;

    public ProtocalPack(byte flag, String content) {
        this.flag = flag;
        this.content = content;
        int len1 = content == null ? 0 : content.getBytes().length;
        this.length = 5 + len1;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ProtocalPack{" +
                "length=" + length +
                ", flag=" + flag +
                ", content='" + content + '\'' +
                '}';
    }
}
