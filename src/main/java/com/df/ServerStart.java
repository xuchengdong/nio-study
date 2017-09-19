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

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            String greeting = "Hello," + socketChannel.getRemoteAddress();
            ByteBuffer buf = ByteBuffer.allocate(48);
            buf.clear();
            buf.put(greeting.getBytes());

            buf.flip();

            while(buf.hasRemaining()) {
                socketChannel.write(buf);
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
