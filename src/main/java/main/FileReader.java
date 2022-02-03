package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static main.Main.showExceptionDialog;


/**
 * FileReader Class for file parsing and creation of Message object.
 */
public class FileReader {

    /**
     * The method reads the lines, divides them into substrings and
     * creates a Message class object from the obtained data.
     *
     * @param file selected file
     * @return list of Message objects
     * @throws IOException
     */
    public static ArrayList<Message> getMessages(File file) throws IOException {

        String line, time, name = null, text = null;
        BufferedReader input;

        ArrayList<Message> messages = new ArrayList<>();

        input = new BufferedReader(new java.io.FileReader(file));
        line = input.readLine();

        long lines = Files.lines(Path.of(file.getPath())).count();
        int count = 0;
        while (line != null) {
            if (line.contains("Time:")) {
                time = resize(line, 5);
                line = input.readLine();
                if (line.contains("Name:")) {
                    name = resize(line, 5);
                    line = input.readLine();
                    if (line.contains("Message:")) {
                        text = resize(line, 8);
                    } else showExceptionDialog(1);
                } else showExceptionDialog(2);

                Message message = new Message(name, time, text);
                messages.add(message);
            }
            line = input.readLine();
            count ++;
        }

        if (count % 2 == 1 ) {
            return messages;
        }
        else {
            showExceptionDialog(4); // return dialog message with detected error
            return null;
        }
    }

    /**
     * This creates a substring of line.
     *
     * @param line line
     * @param number number
     * @return substring
     */
    public static String resize(String line, int number){
        return line.substring(number);
    }
}
