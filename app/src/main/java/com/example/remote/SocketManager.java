package com.example.remote;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;

public class SocketManager {
    private String IP;
    private int Port;

    private SocketChannel socketChannel;
    private Selector selector;

    private readDataThread readData;
    private sendDataThread sendData;

    private Handler handler;

    public SocketManager(String ip, int port, Handler h) {
        this.IP = ip;
        this.Port = port;
        this.handler = h;

        readData = new readDataThread();
        readData.start();
    }

    private void setSocket(String ip, int port) throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(ip, port));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    public void sendData(String data) {
        sendData = new sendDataThread(socketChannel, data);
        sendData.start();
    }

    private void read(SelectionKey key) throws Exception {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = 0;

        read = sc.read(buffer); buffer.flip();
        String data = new String(); CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        data = decoder.decode(buffer).toString();
        Message msg = handler.obtainMessage();
        msg.what = 1;
        msg.obj = data; handler.sendMessage(msg);
        clearBuffer(buffer);
    }

    private void clearBuffer(ByteBuffer buffer) {
        if (buffer != null) {
            buffer.clear();
            buffer = null;
        }
    }

    /*********** inner thread classes **************/
    public class sendDataThread extends Thread {
        private SocketChannel socketChannel;
        private String data;

        public sendDataThread(SocketChannel sc, String d) {
            socketChannel = sc;
            data = d;
        }

        public void run() {
            try {
                socketChannel.write(ByteBuffer.wrap(data.getBytes()));
            } catch (Exception e1) { }
        }
    }

    public class readDataThread extends Thread {
        public readDataThread() { }
        public void run() {
            try {
                setSocket(IP, Port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.obtainMessage();
            handler.sendEmptyMessage(0);

            try {
                while(true) {
                    selector.select();
                    Iterator it = selector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey key = (SelectionKey) it.next();
                        if (key.isReadable()) {
                            try {
                                read(key);
                            } catch (Exception e) { }
                        }
                        it.remove();
                    }
                }
            } catch (Exception e) { }
        }
    }
}
