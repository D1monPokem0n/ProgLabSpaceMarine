package ru.prog.itmo.control;

public class ServerState {
    private boolean workStatus;

    public ServerState() {
        workStatus = true;
    }

    public ServerState(boolean workStatus) {
        this.workStatus = workStatus;
    }

    public void setWorkStatus(boolean workStatus) {
        this.workStatus = workStatus;
        if (!workStatus) {
            System.exit(0);
        }
    }

    public boolean isWorkStatus() {
        return workStatus;
    }
}
