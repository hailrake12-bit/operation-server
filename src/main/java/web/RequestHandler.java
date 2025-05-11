package web;

import java.io.*;
import java.net.Socket;

public class RequestHandler {
    private final Socket clientSocket;

    RequestHandler(Socket socket){
        clientSocket = socket;
    }

    public void handle(){
        try(InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream()){

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

            RequestDispatcher requestDispatcher = new RequestDispatcher(writer, reader);

            requestDispatcher.dispatch();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
