package main;

/**
 * Message class with properties.
 */
public class Message {

    public String name;
    public String time;
    public String text;

    /**
     * Constructor - creation of new
     * Message object with certain values.
     * @param name name
     * @param time time
     * @param text text
     */
    public Message(String name, String time, String text){
        this.name = name;
        this.time = time;
        this.text = text;

    }
    /**
     * Getter
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * Getter
     * @return time
     */
    public String getTime(){
        return time;
    }

    /**
     * Getter
     * @return url
     */
    public String getText(){
        return text;
    }
}
