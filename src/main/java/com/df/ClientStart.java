package com.df;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author xuchengdong@qbao.com on 2017/9/18.
 */
public class ClientStart {

    public ClientStart(String host, int port) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(host, port));

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = socketChannel.read(buf);
        while (bytesRead != -1) {
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }

            buf.clear();
            bytesRead = socketChannel.read(buf);
        }

        socketChannel.close();
    }

    public static void main(String[] args) {
        try {
            new ClientStart("localhost", 9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
