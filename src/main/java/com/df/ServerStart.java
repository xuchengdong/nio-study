package com.df;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author xuchengdong@qbao.com on 2017/9/18.
 */
public class ServerStart {

    public ServerStart(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(port));

        serverSocketChannel.configureBlocking(false); // Non-blocking Mode

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            if (socketChannel == null) {
                continue;
            }

            ByteBuffer buf = ByteBuffer.allocate(48);

            // 向客户端写信息
            String greeting = "Hello," + socketChannel.getRemoteAddress();
            buf.clear();
            buf.put(greeting.getBytes());

            buf.flip();

            while (buf.hasRemaining()) {
                socketChannel.write(buf);
            }

            socketChannel.shutdownOutput(); // 结束向客户端写操作,之后不能再写,否则抛异常,执行此语句后客户端read(buf)才能返回-1否则返回的是0

            // 从客户端读取信息
            buf.clear();
            int bytesRead = socketChannel.read(buf);

            System.out.println("server bytesRead:" + bytesRead);

            while (bytesRead != -1) {
                buf.flip();

                StringBuilder builder = new StringBuilder();

                while (buf.hasRemaining()) {
                    builder.append((char) buf.get());
//                    System.out.print((char) buf.get());
                }

                System.out.println("Receive client messages: " + builder.toString());

                buf.clear();
                bytesRead = socketChannel.read(buf);
            }

            socketChannel.close();
        }

    }


    public static void main(String[] args) {
        try {
            new ServerStart(9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
