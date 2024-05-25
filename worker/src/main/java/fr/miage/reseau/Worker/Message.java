package fr.miage.reseau.Worker;

public class Message {
    private String _command;
    private String _arg1;
    private String _arg2;

    public Message(String message) {
        String[] splitMessage = message.split(" ");

        if(splitMessage.length < 1 || splitMessage.length > 3)
            throw new IllegalArgumentException("Message invalide");

        _command = splitMessage[0];
        if(splitMessage.length >= 2)
            _arg1 = splitMessage[1];

        if(splitMessage.length == 3)
            _arg2 = splitMessage[2];
    }

    public String get_command() {
        return _command;
    }

    public String get_arg1() {
        return _arg1;
    }

    public String get_arg2() {
        return _arg2;
    }

    @Override
    public String toString() {
        return "Message{" +
                "command='" + _command + '\'' +
                ", arg1='" + _arg1 + '\'' +
                ", arg2='" + _arg2 + '\'' +
                '}';
    }
}
