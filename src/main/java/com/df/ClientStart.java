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
        socketChannel.configureBlocking(false); // Non-blocking Mode
        socketChannel.connect(new InetSocketAddress(host, port));

        while (!socketChannel.finishConnect()) {
            //wait, or do something else...
        }

        ByteBuffer buf = ByteBuffer.allocate(48);

        // 从服务端读取信息
        int bytesRead = socketChannel.read(buf);
        while (bytesRead != -1) {
            buf.flip();

            StringBuilder builder = new StringBuilder();

            while (buf.hasRemaining()) {
                builder.append((char) buf.get());
//                System.out.print((char) buf.get());
            }

            if (builder.length() > 0) {
                System.out.println("Receive server messages: " + builder.toString());
            }

            buf.clear();
            bytesRead = socketChannel.read(buf);
            System.out.println("client bytesRead:" + bytesRead);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 向服务端写信息
        String greeting = "Hello Server, I'am " + socketChannel.getLocalAddress();
        buf.clear();
        buf.put(greeting.getBytes());

        buf.flip();

        while (buf.hasRemaining()) {
            socketChannel.write(buf);
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
