package com.example.ex4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpClient {
    private Socket socket;
    private static TcpClient s_instance = null;
    PrintWriter out;

    public static TcpClient Instance() {
        if (s_instance == null){
            s_instance =  new TcpClient();
        }
        return s_instance;
    }

    public void connectToTheServer(final String ip, final int port) {
        final Thread outThread = new Thread() {
            @Override
            public void run() {
            try {
                socket = new Socket(ip, port);
                //System.out.println("connected");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            }
        };
        outThread.start();
    }

    public void disConnectFromServer() {
        if (out != null) {
            out.close();
        }
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(final String message) {
        final Thread outThread = new Thread() {
            //String message2 = "set /controls/flight/rudder 0";
            @Override
            public void run() {
                //System.out.println("Started...");
                //System.out.println(message + "\r\n");
                out = null;
                try {
                    out = new PrintWriter(socket.getOutputStream());
                    out.println(message + "\r\n");
                    out.flush();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
//                    if (out != null) {
//                        out.close();
//                    }
                }
            }
        };
        outThread.start();
    }
}
