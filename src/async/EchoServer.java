package async;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    // 声明一个线程池，用于异步处理客户端请求
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Test
    public void testServer() throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8080));
        while (true) {
            // 服务器通过accept()方法接收客户端请求，如果没有客户端请求，当前线程就会阻塞在这里
            SocketChannel channel = server.accept();
            // 在接收到客户端请求之后，将该请求委托到线程池中进行处理
            executor.execute(() -> {
                try {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.read(buffer);
                    handle(buffer);
                    response(channel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void response(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer
                .wrap(("Hello, I'm server. It's: " + new Date()).getBytes());
        channel.write(buffer);
        channel.close();
    }

    private void handle(ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.println("Server receives message: ");
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }
}
