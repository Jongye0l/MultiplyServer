package com.Jongyeol.MultiplyServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import static com.Jongyeol.MultiplyServer.ServerManager.serverManager;

public class Connection {
    private final Socket socket;
    private final int id;
    public Connection(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
        new Thread("Connection-" + id) {
            @Override
            public void run() {
                while(true) {
                    try {
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        serverManager.servers.get(in.readInt()).run(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
