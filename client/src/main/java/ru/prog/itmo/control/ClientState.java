package ru.prog.itmo.control;

public class ClientState {
    private boolean workStatus;
    public ClientState(){
        workStatus = true;
    }
    public ClientState(boolean workStatus){
        this.workStatus = workStatus;
    }
    public void setWorkStatus(boolean workStatus) {
        this.workStatus = workStatus;
    }

    public boolean isWorkStatus() {
        return workStatus;
    }
}
