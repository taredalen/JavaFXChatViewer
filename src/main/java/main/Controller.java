package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

import static data.JSONConverter.*;
import static main.Main.showExceptionDialog;


/**
 * Class is based on main.fxml file containing elements of the graphical interface. Controller create relationships
 * between data from other classes and the interface scene and consists of methods related to the user action taking
 * place in the window (scene).
 * */

public class Controller {

    @FXML  // declaration of field from .fxml file
    public AnchorPane chatAnchorPane, leftAnchorPane;

    @FXML
    public Tab favoritesTab, recentTab, allTab;

    @FXML
    private Button favoriteButton, modeButton;

    @FXML
    public ScrollPane chatScrollPane;

    @FXML
    public BorderPane rightPane;

    @FXML
    public TextField textField;

    @FXML
    public TabPane tabPane;

    @FXML
    public Text pathText;

    @FXML
    public VBox vbox;

    public TextFlow messageTextFlow = new TextFlow(), nameTextFlow ; // fields not related to the fxml file
    public ArrayList<Conversation> recentConversations = new ArrayList<>();
    public ArrayList<String> nameList = new ArrayList<>();
    public Conversation currentConversation;
    public static boolean darkMode;

    /**
     * This method :
     * - always call methode updateTab in order to update the favorite and
     *   recent tabs of the TabPane.
     * - modifies ScrollPane properties to focus content inside its size.
     * - initialize darkMode to true.
     */

    public void initialize() {
        updateTab(favoritesTab);
        updateTab(allTab);

        chatScrollPane.setFitToHeight(true);
        chatScrollPane.setFitToWidth(true);

        darkMode = true;
    }

    /**
     * This method call the method getMessages(File file) of FileReader class to get a
     * list of messages, as also create a new Conversation object that will be saved
     * in the currentConversation list and shown on the recentTab and allTab.
     *
     * @param actionEvent on openButton clicked
     */

    public void openChat(ActionEvent actionEvent) throws IOException {

        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        File selectedFile = new FileChooser().showOpenDialog(stage);

        if (!(selectedFile == null)) {
            ArrayList<Message> messages = FileReader.getMessages(selectedFile);
            try {
                vbox.getChildren().clear(); // to show another chat
                nameList.clear();

                messages.forEach(message -> {
                    nameList.add(message.getName());
                    messageGenerator(message.getText(), message.getName(), message.getTime());
                });

                String path = selectedFile.getPath();
                String name = selectedFile.getName();

                Set<String> participants = new LinkedHashSet<>(nameList);
                String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

                currentConversation = new Conversation(messages, participants ,path, name, date, messages.size(),false );
                recentConversations.add(currentConversation); // adding this conversation in the list
                pathText.setText("Path: " + currentConversation.getPath());
                updateTab(recentTab);  // that will be updated fo the tabs
                updateTab(allTab);
            } catch (Exception e){
                //showExceptionDialog(4); // return dialog message with detected error
            }
        }
    }

    /**
     * This method used for generating the messages. The resulting message text will be
     * split into subtexts due to the pattern obtained from the iteration of the list
     * of emotion objects. If the previous name of the sender of the message is equal
     * to the current one, then the textFlow with the name and the profile Pane will
     * not be displayed.
     *
     * @param message message from message object
     * @param name name from message object
     * @param time time  from message object
     */

    public void messageGenerator(String message, String name, String time) {

        nameTextFlow = new TextFlow();
        messageTextFlow = new TextFlow();

        Text timeText = new Text(time);
        TextFlow timeTextFlow = new TextFlow();

        Label firstLetterLabel = new Label(Character.toString(name.charAt(0)));
        List<Node> nodeList = new ArrayList<>(); // list with text and images
        Pane profilePane = new Pane(firstLetterLabel); // name's first letter

        if (containsGif(message)){ // if message contains smile
            emotions.forEach( em -> { // list of objects containing patters to check
                String pattern = em.getPattern();
                Matcher matcher = Pattern.compile(pattern).matcher(message);
                int lastEnd = 0;
                while(matcher.find()) {
                    // cut part without gif and put in the Text
                    Text text = new Text(message.substring(lastEnd, matcher.start()));
                    text.getStyleClass().add("message-text");
                    if (darkMode) text.getStyleClass().add("message-text");
                    else text.getStyleClass().add("message-text-light");

                    URL url =  getClass().getResource(em.getUrl());
                    ImageView imageView = new ImageView(String.valueOf(url));

                    if (em.getType().equals(message)){
                        imageView.setFitHeight(120); // to have a big nice gif :)
                        imageView.setFitWidth(120);
                    }
                    else {
                        imageView.setFitHeight(24);
                        imageView.setFitWidth(24);
                    }
                    nodeList.add(text); // adding separately in list of nodes
                    nodeList.add(imageView); // text and imageview

                    lastEnd = matcher.end();
                }
            });
        } else { // if message is without gif
            Text text = new Text(message);
            if (darkMode) text.getStyleClass().add("message-text");
            else text.getStyleClass().add("message-text-light");
            nodeList.add(text);
            nameTextFlow.setMinHeight(20);
            timeTextFlow.setMinHeight(20);
         }
        if (!sameName(name)) { // profile pane not showing for the same person
            Text nameText = new Text(name);
            nameText.getStyleClass().add("name-text");
            nameTextFlow.getChildren().add(nameText);
            profilePane.setVisible(true);
        } else profilePane.setVisible(false);

        timeTextFlow.getChildren().add(timeText);
        messageTextFlow.getChildren().addAll(nodeList);

        VBox centralVbox = new VBox();
        BorderPane borderPane = new BorderPane();

        VBox.setVgrow(centralVbox, Priority.ALWAYS);
        VBox.setVgrow(messageTextFlow, Priority.ALWAYS);

        messageTextFlow.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        centralVbox.getChildren().addAll(nameTextFlow, messageTextFlow);

        borderPane.setRight(timeTextFlow);
        borderPane.setLeft(profilePane);
        borderPane.setCenter(centralVbox);

        BorderPane.setMargin(profilePane, new Insets(0,10,0,0));
        BorderPane.setMargin(timeTextFlow, new Insets(0,10,0,10));

        timeText.getStyleClass().add("time-text"); // styling items
        borderPane.getStyleClass().add("border-pane");
        profilePane.getStyleClass().add("profile-pane");
        timeTextFlow.getStyleClass().add("time-text-flow");
        nameTextFlow.getStyleClass().add("name-text-flow");
        messageTextFlow.getStyleClass().add("time-text-flow");

        vbox.getChildren().add(borderPane);
     }

    /**
     * This method used to check if message contains any
     * smile by searching in the list of Message objects.
     *
     * @param message message object
     * @return boolean value of search
     */

    public boolean containsGif(String message) {
        for (Emotion search : emotions) {
            if (message.contains(search.getType())) { // message contains smile
                return true;
            }
        } return false;
    }

    /**
     * This method is used to detect if the previous name match the
     * current one in order to not show the profile name and profile
     * Pane if the message was sent by the same person.
     *
     * @param currentName name of the current message object
     * @return boolean result of search
     */

    public boolean sameName(String currentName) {
        if (nameList.size()-1 <= 1) {
            return false;
        }
        if (nameList.get(nameList.size()-2).equals(currentName)) {
            return true;
        } return false;
    }

    /**
     * Method generates a viewList  of conversation in left side of the window
     * using the setCellFactory and class CustomListCell(), as well as search
     * of the conversation in selected tab.
     * @param selectedTab tab of the TabPane
     */

    public void updateTab(Tab selectedTab) {

        ObservableList<Conversation> observableList = FXCollections.observableArrayList();

        if (selectedTab.equals(recentTab)) recentConversations.forEach(observableList::addAll);
        if (selectedTab.equals(favoritesTab)) favoriteConversations.forEach(observableList::addAll);
        else if (selectedTab.equals(allTab)) {
            favoriteConversations.forEach(observableList::addAll);
            recentConversations.forEach(observableList::addAll);
        }

        FilteredList<Conversation> filteredData = new FilteredList<>(observableList,p -> true);
        // set the filter Predicate whenever the filter changes.
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(conversation ->{
                if(newValue == null || newValue.isEmpty()){
                    return true; // if filter text is empty, display all conversations
                }
                // compare file name of every conversation with filter text
                String lowerCaseFilter = newValue.toLowerCase();

                if(conversation.getName().toLowerCase().contains(lowerCaseFilter)) { //filter matches file name
                    return true;
                } else return false; // does not match
            });
        });

        SortedList<Conversation> sortedData = new SortedList<>(filteredData);
        ListView<Conversation> listView = new ListView<>(sortedData);

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectLast();

        listView.setCellFactory(list -> new CustomListCell()); // creating grinPane for each item

        listView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            currentConversation = listView.getSelectionModel().getSelectedItem();
            if(!(currentConversation == null)) {
                pathText.setText("Path: " + currentConversation.getPath());
                // if selected conversation is favorite then the color will change
                if (currentConversation.getFavorite()) changeButtonColor(1);
                else changeButtonColor(2);
                nameList.clear();
                vbox.getChildren().clear();
                currentConversation.getMessages().forEach(m -> {
                    nameList.add(m.getName());
                    messageGenerator(m.getText(), m.getName(), m.getTime());
                });
            }
        });

        listView.getStyleClass().add("listview");
        VBox.setVgrow(listView, Priority.ALWAYS);
        selectedTab.setContent(listView);
    }

    /**
     * This method ads or remove current (selected)
     * conversation from list of conversations from
     * JSON file, change the color of button and
     * update related tabs.
     *
     * @param event favoriteButton is clicked
     */

    public void addToFavorites(ActionEvent event) {

        if (!(currentConversation == null)){
            if (currentConversation.getFavorite()) {
                updateData(currentConversation, "remove");
                changeButtonColor(2);
                updateTab(favoritesTab);
            } else {
                currentConversation.setFavorite(true);
                updateData(currentConversation, "add");
                changeButtonColor(1);
                updateTab(favoritesTab);
            }
        } else showExceptionDialog(6);
    }

    /**
     * This method changes the color of the buttons
     * depending on the actions taking place.
     *
     * @param option int of selected option
     */
    public void changeButtonColor(Integer option ){
        switch (option) {
            case 1:
                ImageView imageView = new ImageView(String.valueOf(getClass().getResource("/images/star2.png")));
                imageView.setFitWidth(22);
                imageView.setFitHeight(22);
                favoriteButton.setGraphic(imageView);
                break;
            case 2:
                ImageView imageView2 = new ImageView(String.valueOf(getClass().getResource("/images/star.png")));
                imageView2.setFitWidth(22);
                imageView2.setFitHeight(22);
                favoriteButton.setGraphic(imageView2);
                break;
            case 3:
                modeButton.setStyle("-fx-background-color: #616BEE");
                break;
        }
    }

    /**
     * This method changes the background of components
     * depending on the current mode.
     * @param event button set clicked
     */
    public void setCurrentMode(ActionEvent event){
        System.out.println(darkMode);
        if(darkMode){
            darkMode = false;
            chatAnchorPane.setStyle("-fx-background-color: #e7e7eb");
            chatScrollPane.setStyle("-fx-background-color: #e7e7eb");
            vbox.setStyle("-fx-background-color: #e7e7eb");
            textField.setStyle("-fx-background-color: #b3c4d3");
            rightPane.setStyle("-fx-background-color: #e7e7eb");
            leftAnchorPane.setStyle("-fx-background-color: #e7e7eb");
            tabPane.setStyle("-fx-background-color: #e7e7eb");
            chatScrollPane.setStyle("-fx-background-color: #e7e7eb");
        } else {
            darkMode = true;
            chatAnchorPane.setStyle("-fx-background-color: #1C1C2A");
            chatScrollPane.setStyle("-fx-background-color: #1C1C2A");
            vbox.setStyle("-fx-background-color: #1C1C2A");
            textField.setStyle("-fx-background-color: #1C1C2A");
            chatScrollPane.setStyle("-fx-background-color: #1C1C2A");
            rightPane.setStyle("-fx-background-color: #27273B");
            leftAnchorPane.setStyle("-fx-background-color: #27273B");
            tabPane.setStyle("-fx-background-color: #27273B");
        }
        updateTab(allTab); // update tabs after changes
        updateTab(recentTab);
        updateTab(favoritesTab);

        //currentConversation.getMessages().forEach(m -> { // update current conversation
        //    nameList.add(m.getName());
        //    messageGenerator(m.getText(), m.getName(), m.getTime());
        //});

        changeButtonColor(3); // change button background color
    }

    /**
     * This method allows to create a gridPane with the
     * components of the current Conversation object.
     * gridPane contains names, date a name of the
     * file of the conversation, as well as circle
     * pane with total number of messages.
     */

    private static class CustomListCell extends ListCell<Conversation> {

        private Text path, date;
        private Label numberLabel, partLabel;
        private GridPane gridPane;
        private Pane profilePane;

        /**
         * Creation of gridPane
         */
        public CustomListCell() {

            gridPane = new GridPane();
            numberLabel = new Label();
            date = new Text();
            path = new Text();
            partLabel = new Label();
            profilePane = new Pane(numberLabel);

            gridPane.getStyleClass().add("grid-pane");
            date.getStyleClass().add("date-text");
            path.getStyleClass().add("path-text");

            if (darkMode) {
                profilePane.getStyleClass().add("circle-pane");
                partLabel.getStyleClass().add("part-label");
            }
            else {
                profilePane.getStyleClass().add("circle-pane-light");
                partLabel.getStyleClass().add("part-label-light");
            }

            gridPane.getColumnConstraints().addAll(new ColumnConstraints(40),
                    new ColumnConstraints(80), new ColumnConstraints(50));

            gridPane.setPadding(new Insets(10, 10, 10, 10));

            gridPane.add(profilePane, 0, 1,2,2);
            gridPane.add(partLabel, 1, 1,1,1);
            gridPane.add(date, 2, 1,1,1);
            gridPane.add(path, 1, 2,1,1);

            GridPane.setValignment(partLabel, VPos.TOP);
            GridPane.setHalignment(partLabel, HPos.LEFT);
            GridPane.setHalignment(date, HPos.RIGHT);
            GridPane.setValignment(date, VPos.TOP);
            GridPane.setValignment(path, VPos.BOTTOM);
            GridPane.setHalignment(path, HPos.LEFT);

            gridPane.setHgap(10);
        }

        /**
         * Method for setting information related to the
         * current conversation in gridPane.
         * @param conversation current conversation
         * @param empty boolean value
         */

        protected void updateItem(Conversation conversation, boolean empty) {
            super.updateItem(conversation, empty);
            if (conversation != null && !empty) { //  test for null item and empty parameter

                date.setText(conversation.getDate()); // getting elements from conversation
                path.setText(conversation.getName());
                numberLabel.setText(String.valueOf(conversation.getSize()));

                String participants = Arrays.toString(new Set[]{conversation.getParticipants()}).replace("[", "").replace("]", "");

                partLabel.setText(participants);
                setGraphic(gridPane);
            } else {
                setGraphic(null);
            }
        }
    }
}
