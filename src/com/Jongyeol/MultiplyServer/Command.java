package com.Jongyeol.MultiplyServer;

public enum Command {
    stop(cmd -> System.exit(0)),
    reload(cmd -> ServerManager.serverManager.loadServers())
    ;
    private final Run run;
    Command(Run run) {
        this.run = run;
    }
    public void run(String[] cmd) {
        run.run(cmd);
    }
    public interface Run{
        void run(String[] cmd);
    }
}
