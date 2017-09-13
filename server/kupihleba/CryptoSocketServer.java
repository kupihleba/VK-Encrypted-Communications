package kupihleba;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;

/**
 * Alternative class for socket communications
 */
public class CryptoSocketServer extends Thread {
    CryptoSocketServer() {
        setDaemon(true);
    }

    CryptoSocketServer(int port) {
        this();
        this.port = port;
    }

    static boolean DEBUG = true;

    private static void log(String data) {
        if (DEBUG)
            System.out.println(data);
    }

    //final static String HOST = "127.0.0.1";
    private int port = 5005;
    private static ArrayDeque<Thread> threads = new ArrayDeque<>();
    private static Thread serverThread;

    @Override
    public void interrupt() {
        threads.forEach(Thread::interrupt);
        super.interrupt();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (!Thread.interrupted()) {
                Socket client = serverSocket.accept();
                log("got a client!");
                threads.push(new Thread(new SocketClient(client)));
                threads.peek().setDaemon(true);
                threads.peek().start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SocketClient implements Runnable {
    SocketClient(Socket client) {
        this.client = client;
        try {
            in = client.getInputStream();
            out = client.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private Socket client;
    private InputStream in;
    private OutputStream out;

    @Override
    public void run() {
        int cmd;
        int query;
        try {
            while (!Thread.interrupted() && in.available() > 0) {
                query = in.read();
                System.out.print((char) query);
                out.write((char) query);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
