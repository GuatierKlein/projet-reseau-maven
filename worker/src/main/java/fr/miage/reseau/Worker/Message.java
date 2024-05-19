package fr.miage.reseau.Worker;

public class Message {
    private String command;
    private String arg1;
    private String arg2;

    public Message(String message) {
        String[] splitMessage = message.split(" ");

        if(splitMessage.length < 1 || splitMessage.length > 3)
            throw new IllegalArgumentException("Message invalide");

        command = splitMessage[0];
        if(splitMessage.length >= 2)
            arg1 = splitMessage[1];

        if(splitMessage.length == 3)
            arg2 = splitMessage[2];
    }

    public String getCommand() {
        return command;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    @Override
    public String toString() {
        return "Message{" +
                "command='" + command + '\'' +
                ", arg1='" + arg1 + '\'' +
                ", arg2='" + arg2 + '\'' +
                '}';
    }
}
