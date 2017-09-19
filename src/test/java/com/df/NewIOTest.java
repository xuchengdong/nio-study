package com.df;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xuchengdong@qbao.com on 2017/9/18.
 */
public class NewIOTest {

    @Test
    public void testFileChannel() throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {

            System.out.println("Read " + bytesRead);
            buf.flip();

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }

            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }


    @Test
    public void testScatterAndGather() throws IOException {
        RandomAccessFile inFile = new RandomAccessFile("data/nio-data.txt", "rw");
        FileChannel inChannel = inFile.getChannel();

        ByteBuffer header = ByteBuffer.allocate(12);
        ByteBuffer body = ByteBuffer.allocate(50);

        ByteBuffer[] bufferArray = {header, body};

        long bytesRead = inChannel.read(bufferArray);
        System.out.println("Read " + bytesRead);

        header.flip();
        body.flip();
        header.mark();
        body.mark();

        while (header.hasRemaining()) {
            System.out.print((char) header.get());
        }
        System.out.println("======================");

        while (body.hasRemaining()) {
            System.out.print((char) body.get());
        }
        System.out.println("======================");

        header.reset();
        body.reset();

        RandomAccessFile outFile = new RandomAccessFile("data/nio-data2.txt", "rw");
        FileChannel outChannel = outFile.getChannel();

        // (1) 成功
        outChannel.write(bufferArray);

        // (2) 失败
//        long position = 0;
//        long count = inChannel.size();
//        System.out.println(position+" "+count);
//        outChannel.transferFrom(inChannel, position, count);

        inFile.close();
        outFile.close();
    }

    @Test
    public void testTransferFrom() throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("data/fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("data/toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        toChannel.transferFrom(fromChannel, position, count);
    }

    @Test
    public void testTransferTo() throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("data/fromFile.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("data/toFile.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        fromChannel.transferTo(position, count, toChannel);
    }

    @Test
    public void testSelector() throws IOException, InterruptedException {
    }
}
