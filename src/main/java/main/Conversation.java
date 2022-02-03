package main;

import java.util.*;

/**
 * Conversation class with properties.
 */
public class Conversation {
    public List<Message> messages;
    public Set<String> participants;
    public String path;
    public String date;
    public String name;
    public int size;
    public boolean favorite;

    /**
     * Constructor - creation of new object
     * with certain values.
     *
     * @param messages messages
     * @param participants participants
     * @param path path of the file
     * @param name name of the file
     * @param date date of adding
     * @param size size of conversation
     * @param favorite boolean value
     */

    public Conversation (List<Message> messages, Set<String> participants, String path, String name, String date, int size, boolean favorite){
        this.messages = messages;
        this.participants = participants;
        this.path = path;
        this.date = date;
        this.size = size;
        this.name = name;
        this.favorite = favorite;
    }

    /**
     * Getter
     * @return list of messages
     */
    public List<Message> getMessages(){
        return messages;
    }

    /**
     * Getter
     * @return Set of participants
     */
    public Set<String> getParticipants(){
        return participants;
    }

    /**
     * Getter
     * @return file path
     */
    public String getPath(){
        return path;
    }

    /**
     * Getter
     * @return date of adding
     */
    public String getDate(){
        return date;
    }

    /**
     * Getter
     * @return name of file
     */
    public String getName(){
        return name;
    }

    /**
     * Getter
     * @return size of conversation
     */
    public Integer getSize(){
        return size;
    }

    /**
     * Getter
     * @return boolean value
     */
    public Boolean getFavorite(){
        return favorite;
    }

    /**
     * Setter
     * @param favorite set the boolean value
     */
    public void setFavorite(Boolean favorite){
        this.favorite = favorite;
    }
}
