package com.Jongyeol.MultiplyServer;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ServerManager extends Thread {
    public static void main(String[] args) {
        new ServerManager();
    }
    public static ServerManager serverManager;
    private ServerSocket socket;
    private int id = 0;
    private final ArrayList<Connection> connections = new ArrayList<>();
    public final Map<Integer, ServerTemplete> servers = new HashMap<>();
    public ServerManager() {
        super("ServerManager");
        serverManager = this;
        loadServers();
        new Thread("Scanner Listener") {
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while(true) {
                    String line = scanner.nextLine();
                    String[] cmds = line.split(" ");
                    try {
                        Command.valueOf(cmds[0].toLowerCase()).run(cmds);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Unknown Command");
                    }
                }
            }
        }.start();
        start();
    }
    @Override
    public void run() {
        try {
            socket = new ServerSocket(1209);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                connections.add(new Connection(socket.accept(), id));
                id++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void loadServers() {
        File serverFolder = new File("Servers");
        if(!serverFolder.exists() || !serverFolder.isDirectory()) serverFolder.mkdir();
        File dumpFolder = new File("dumpFolder");
        if(dumpFolder.exists() && dumpFolder.isDirectory()) dumpFolder.delete();
        dumpFolder.mkdir();
        try {
            Files.copy(serverFolder.toPath(), dumpFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
            for(File file : dumpFolder.listFiles()) {
                URLClassLoader url = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
                try {
                    Class<ServerTemplete> cl = (Class<ServerTemplete>) url.loadClass("com.Jongyeol." + file.getName() + ".Server");
                    ServerTemplete server = cl.newInstance();
                    servers.put(server.getId(), server);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
