package web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;

    private volatile boolean running = false;

    public Server(int port) throws Exception{
        serverSocket = new ServerSocket(port);
    }

    public void start() throws Exception{

        running = true;

        while(running) {
            Socket socket = serverSocket.accept();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestHandler handler = new RequestHandler(socket);
                    handler.handle();
                }
            }).start();
        }
    }

    public void stop() throws Exception{
        running = false;
        serverSocket.close();
    }
}
