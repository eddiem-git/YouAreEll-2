package models;

/* 
 * POJO for an Message object
 */
public class Message implements Comparable<Message> {

    private String message;
    private String fromId;
    private String toId;

    public Message (String message, String fromId, String toId) {

        this.message = message;
        this.fromId = fromId;
        this.toId = toId;
    }

    public int compareTo(Message o) {
        return 0;
    }
}