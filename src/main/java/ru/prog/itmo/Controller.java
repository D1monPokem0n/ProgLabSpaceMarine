package ru.prog.itmo;



public class Controller {
    private final ProgramState programState;
    private Storage storage;
    private ConsoleArgument argument;
    private final CommandMap commandMap;
    public Controller(Storage storage){
        programState = new ProgramState(true);
        argument = new ConsoleArgument();
        commandMap = new CommandMap(storage, programState, argument);
    }
    public void executeCommand(String commandName){
        commandMap.getCommandHashMap().get(commandName).execute();
    }

    public CommandMap getCommandMap() {
        return commandMap;
    }

    public ConsoleArgument getArgument() {
        return argument;
    }

    public void setArgument(ConsoleArgument argument) {
        this.argument = argument;
    }

    public ProgramState getProgramState() {
        return programState;
    }
}
