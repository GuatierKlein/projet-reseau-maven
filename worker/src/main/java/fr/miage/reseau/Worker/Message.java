package fr.miage.reseau.Worker;

/**
 * La classe Message représente un message avec une commande et jusqu'à deux arguments.
 */
public class Message {
    private String _command;
    private String _arg1;
    private String _arg2;

    /**
     * Constructeur de la classe Message.
     * Parse une chaîne de caractères pour extraire la commande et les arguments.
     *
     * @param message la chaîne de caractères représentant le message
     * @throws IllegalArgumentException si le message est invalide (moins de 1 ou plus de 3 parties)
     */
    public Message(String message) {
        String[] splitMessage = message.split(" ");

        if (splitMessage.length < 1 || splitMessage.length > 3)
            throw new IllegalArgumentException("Message invalide");

        _command = splitMessage[0];
        if (splitMessage.length >= 2)
            _arg1 = splitMessage[1];

        if (splitMessage.length == 3)
            _arg2 = splitMessage[2];
    }

    /**
     * Retourne la commande du message.
     *
     * @return la commande du message
     */
    public String get_command() {
        return _command;
    }

    /**
     * Retourne le premier argument du message.
     *
     * @return le premier argument du message
     */
    public String get_arg1() {
        return _arg1;
    }

    /**
     * Retourne le deuxième argument du message.
     *
     * @return le deuxième argument du message
     */
    public String get_arg2() {
        return _arg2;
    }

    /**
     * Retourne une représentation en chaîne de caractères de l'objet Message.
     *
     * @return une chaîne de caractères représentant le message
     */
    @Override
    public String toString() {
        return "Message{" +
                "command='" + _command + '\'' +
                ", arg1='" + _arg1 + '\'' +
                ", arg2='" + _arg2 + '\'' +
                '}';
    }
}
