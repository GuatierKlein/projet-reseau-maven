package fr.miage.reseau.Worker;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private List<MessageLine> lines;
    
    public Message(String message) {
        String[] splitMessage = message.split("\n");
        lines = new ArrayList<>();
        for (String line : splitMessage) {
            lines.add(new MessageLine(line));
        }
    }

    public List<MessageLine> getLines() {
        return lines;
    }
    
}