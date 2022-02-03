package main;

/**
 * Emotion class with properties.
 */

public class Emotion {

    public String url;
    public String type;
    public String pattern;

    /**
     * Constructor - creation of new
     * Emotion object with certain values.
     * @param url url
     * @param type type
     * @param pattern pattern
     */
    public Emotion(String url, String type, String pattern){
        this.url = url;
        this.type = type;
        this.pattern = pattern;
    }

    /**
     * Getter
     * @return url
     */
    public String getUrl(){
        return url;
    }

    /**
     * Getter
     * @return type
     */
    public String getType(){
        return type;
    }

    /**
     * Getter
     * @return pattern
     */
    public String getPattern(){
        return pattern;
    }

}
