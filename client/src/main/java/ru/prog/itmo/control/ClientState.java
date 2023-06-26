package ru.prog.itmo.control;

public class ClientState {
    private boolean workStatus;
    private boolean isLogged;
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

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public boolean isLogged() {
        return isLogged;
    }
}
