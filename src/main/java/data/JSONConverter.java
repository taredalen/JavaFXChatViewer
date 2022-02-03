package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import main.Conversation;
import main.Emotion;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class includes methods related to the use and editing
 * of the original project data.
 */
public class JSONConverter {

    public static ArrayList<Emotion> emotions = new ArrayList<>();
    public static ArrayList<Conversation> favoriteConversations = new ArrayList<>();
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Method of start reading and creation of the JSON file.
     */
    public static void main() {
        emotionsInitialisation();
        readJsonFile();
        writeJsonFile();
    }

    /**
     * Method of writing the JSON file.
     */
    public static void writeJsonFile(){
        try (FileWriter file = new FileWriter("data.json")) {
            gson.toJson(favoriteConversations, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method of reading the JSON file.
     */
    
    public static void readJsonFile(){
        try {
            JsonReader reader = new JsonReader(new FileReader("data.json"));
            Type founderListType = new TypeToken<ArrayList<Conversation>>(){}.getType();
            favoriteConversations = gson.fromJson(reader, founderListType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method of updating the JSON file.
     * @param conversation concerned conversation
     * @param option action to realise
     */
    public static void updateData(Conversation conversation, String option) {

        if (option.equals("remove")) favoriteConversations.remove(conversation);
        else favoriteConversations.add(conversation);

        try (FileWriter file = new FileWriter("data.json")) {
            gson.toJson(favoriteConversations, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method of creation of emotions list
     * based on constructor of Emotion class
     */
    public static void emotionsInitialisation() {

        Emotion emotion1 = new Emotion("/images/sad.gif",":(", ":\\(");
        Emotion emotion2 = new Emotion("/images/happy.gif",":)",":\\)");

        emotions.add(emotion1);
        emotions.add(emotion2);
    }
}

