package com.Jongyeol.MultiplyServer;

import java.net.Socket;

public interface ServerTemplete {
    void run(Socket socket);
    int getId();
}
