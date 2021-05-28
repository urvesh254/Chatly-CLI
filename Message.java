import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    // Type of the messages.
    public static final int EXIT = 0;
    public static final int USER_INFO = 1;
    public static final int MESSAGE = 2;

    private int messageType;
    private String message;

    public Message(int messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public int getMessageType() {
        return this.messageType;
    }

    public String getMessage() {
        return this.message;
    }
}