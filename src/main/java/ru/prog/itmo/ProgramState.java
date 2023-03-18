package ru.prog.itmo;

public class ProgramState {
    private boolean workStatus;
    public ProgramState(){
        workStatus = true;
    }
    public void setWorkStatus(boolean workStatus) {
        this.workStatus = workStatus;
    }

    public boolean isWorkStatus() {
        return workStatus;
    }
}
