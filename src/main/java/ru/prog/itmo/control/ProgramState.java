package ru.prog.itmo.control;

public class ProgramState {
    private boolean workStatus;
    public ProgramState(){
        workStatus = true;
    }
    public ProgramState(boolean workStatus){
        this.workStatus = workStatus;
    }
    public void setWorkStatus(boolean workStatus) {
        this.workStatus = workStatus;
    }

    public boolean isWorkStatus() {
        return workStatus;
    }
}
