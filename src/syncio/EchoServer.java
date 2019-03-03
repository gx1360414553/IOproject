package syncio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class EchoServer {

    @Test
    public void testServer() throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();  // 开启一个ServerSocketChannel
        server.bind(new InetSocketAddress(8083));  // 将服务器绑定到8080端口
        while (true) {  // 在无限循环中不断接收客户端的请求，并且进行处理
            SocketChannel channel = server.accept();  // 接收客户端请求，如果没有请求到来，则阻塞在这里
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);  // 读取客户端请求Channel中的数据
            handle(buffer);  // 处理客户端请求channel的数据
            response(channel);  // 往客户端channel中写入数据，以返回响应
        }
    }

    private void handle(ByteBuffer buffer) {
        buffer.flip();  // 读取数据之后需要切换ByteBuffer的模式为读取模式
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.println("Server receives message: ");
        System.out.println(new String(bytes, StandardCharsets.UTF_8));  // 打印客户端发送的数据
    }

    private void response(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer
                .wrap(("Hello, I'm server. It's: " + new Date()).getBytes());
        channel.write(buffer);  // 往客户端中写入当前数据
        channel.close();
    }
}
