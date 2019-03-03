package syncio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class EchoClient {

    @Test
    public void testClient() throws IOException {
        SocketChannel channel = SocketChannel.open();  // 开启一个客户端SocketChannel
        channel.connect(new InetSocketAddress("127.0.0.1", 8083));  // 连接服务器ip和端口
        request(channel);  // 发送客户端请求
        handleResponse(channel);  // 处理服务端响应
    }

    private void request(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap("Hello, I'm client. ".getBytes());
        channel.write(buffer);  // 将客户端请求数据写入到Channel中
    }

    private void handleResponse(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);  // 读取服务端响应数据
        buffer.flip();  // 这里需要切换模式，因为上面的read()操作相对于ByteBuffer而言是写入
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.println("Client receives message: ");
        System.out.println(new String(bytes, StandardCharsets.UTF_8));  // 打印服务端返回数据
    }
}
