import com.example.java2semester.Project2.Quiz;
import com.example.java2semester.Project2.QuizMaker;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableArray;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class QuizMaker extends Application {
    private String correctAnswer;
    private int totalCorrectAnswer = 0;
    private int nextCount = 0;
    private int width = 800;
    private int height = 600;
    private int second = 0;
    private int minute = 0;
    private String nextStringCount = String.valueOf(nextCount +1).concat(") ");
    final ToggleGroup group = new ToggleGroup();
    private ArrayList<String> arrayAnswers = new ArrayList<>();
    private ArrayList<RadioButton> isSelectedButton = new ArrayList<>();
    private ArrayList<String> fillinText = new ArrayList<>();
    private FileChooser fileChooser = new FileChooser();
    private Font font;
    private Label txtLabel  = new Label();

    //Buttons;
    RadioButton redButton = new RadioButton();
    RadioButton orangeButton = new RadioButton();
    RadioButton blueButton = new RadioButton();
    RadioButton greenButton = new RadioButton();
    Button nextButton  = new Button(">");
    Button backButton = new Button("<");
    Button buttonShowAnswer = new Button("Show Answers");
    Button buttonClose =new Button("Close Test");

    @Override
    public void start(Stage stage) throws Exception {
        //Our main Pane
        BorderPane mainPane = new BorderPane();

        //Button for Choosing file
        Button buttonChooseFile = new Button("Choose a file");
        buttonChooseFile.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
//        buttonChooseFile.setStyle("-fx-background-color: gray;");
        //image for First Scene
        ImageView imageFile = new ImageView(new Image(new FileInputStream("images/background.jpg")));
        imageFile.setFitHeight(height);//600
        imageFile.setFitWidth(width);//800
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageFile,buttonChooseFile);

        //Show the first scene
        Scene sceneFirst = new Scene(stackPane, width, height);
        stage.setScene(sceneFirst);

        //Font for Buttons
        Font fontR = Font.font("monospace",FontWeight.BOLD,15);

        //Music
        String musicFile = "kahoot.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer musicPlayer = new MediaPlayer(sound);

        //Timer for All Questions
        Timer timer = new Timer();
        Text textTime = new Text();
        textTime.setStyle("-fx-font-size: 20");

        //Write our algorithm
        TimerTask task = new TimerTask(){
            public void run(){
                second++;
                if(second == 60){
                    minute++;
                    second = 0;
                }
                if(minute <= 9 && second <=9){
                    textTime.setText("0" + minute + ":" + "0"+ second);

                }else if(minute <= 9 && second > 9){
                    textTime.setText("0"+minute + ":"+second);
                }else{
                    textTime.setText(minute + ":" + second);
                }
            }
        };

        //After choosing File it starts
        buttonChooseFile.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {

                //Scene out Tests and Filling
                Scene sceneMain = new Scene(mainPane, width, height);
                stage.setScene(sceneMain);

                //Read File choose it from Computer
                File file = fileChooser.showOpenDialog(stage);
                String s = file.getName();
                Quiz quiz = new Quiz();

                redButton.setToggleGroup(group);
                redButton.setMinWidth(395);
                redButton.setMinHeight(90);
                redButton.setStyle("-fx-background-color: crimson");
                redButton.setTextFill(Color.WHITE);
                redButton.setFont(fontR);

                blueButton.setToggleGroup(group);
                blueButton.setMinWidth(395);
                blueButton.setMinHeight(90);
                blueButton.setStyle("-fx-background-color: royalblue");
                blueButton.setTextFill(Color.WHITE);
                blueButton.setFont(fontR);

                orangeButton.setToggleGroup(group);
                orangeButton.setMinWidth(395);
                orangeButton.setMinHeight(90);
                orangeButton.setStyle("-fx-background-color: orange");
                orangeButton.setTextFill(Color.WHITE);
                orangeButton.setFont(fontR);

                greenButton.setToggleGroup(group);
                greenButton.setMinWidth(395);
                greenButton.setMinHeight(90);
                greenButton.setStyle("-fx-background-color: yellowgreen");
                greenButton.setTextFill(Color.WHITE);
                greenButton.setFont(fontR);

                try {
                    //start method from Quiz
                    quiz.loadFromFile(s);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                //Start music
                musicPlayer.play();

                //start countdown timer
                timer.scheduleAtFixedRate(task, 1000,1000);

                //ArrayList with all Elements from Quiz (Questions and Answers);
                ArrayList <Question> questions = quiz.getAll();

//                      <-- SHUFFLING -->
//                Collections.shuffle(questions);

                //images icon
                ImageView fillImage = null;
                try {
                    fillImage = new ImageView(new Image(new FileInputStream("images/k.png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                //image for fillin
                ImageView fillImage2 = null;
                try {
                    fillImage2 = new ImageView(new Image(new FileInputStream("images/fillin.png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                fillImage.setFitWidth(50);
                fillImage.setFitHeight(30);
                fillImage2.setFitWidth(400);
                fillImage2.setFitHeight(280);

                //just fill arrayLists with null
                for (int i = 0; i < questions.size(); i++) {
                    RadioButton radioButton = new RadioButton();
                    isSelectedButton.add(radioButton);
                    arrayAnswers.add("");
                    fillinText.add("");
                }

                //KahootMain image ( MultipleChoose )
                ImageView imageView = null;
                try {
                    imageView = new ImageView(new Image(new FileInputStream("images/kahoot.png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                //Set testPhoto in Center
                imageView.setFitHeight(250);
                imageView.setFitWidth(400);
                VBox vBoxImage = new VBox();
                vBoxImage.getChildren().add(imageView);
                vBoxImage.setAlignment(Pos.CENTER);

                //question with icon K
                Label fillLabel = new Label(questions.get(nextCount).getDescription() , fillImage);
                fillLabel.setAlignment(Pos.CENTER);
                fillLabel.setMinHeight(100);
                fillLabel.setMinWidth(width);
                Font font = Font.font("monospace", FontWeight.BOLD, 18);
                fillLabel.setFont(font);

                //textField for Fillin questions
                TextField textFill = new TextField();
                textFill.setMaxSize(400,20);//size of TextField
                textFill.setMinSize(400,20);
                Label fillLabel2 = new Label("Type your answer here: ",textFill);
                fillLabel2.setContentDisplay(ContentDisplay.BOTTOM);

               /*
                             //next Picture
                    ImageView nextImage = null;
                    try {
                        nextImage = new ImageView(new Image(new FileInputStream("images/next.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    nextImage.setFitHeight(50);
                    nextImage.setFitWidth(40);
                */

                //size for button Next and Back
                nextButton.setMinWidth(40);
                nextButton.setMinHeight(50);
                backButton.setMinWidth(40);
                backButton.setMinHeight(50);
                nextButton.setFont(Font.font("Verdana", FontWeight.BOLD, 19));
                nextButton.setStyle("-fx-text-fill: ORANGERED;");
                backButton.setFont(Font.font("Verdana", FontWeight.BOLD, 19));
                backButton.setStyle("-fx-text-fill: ORANGERED;");

                //Next and Back buttons in stackPane
                StackPane nextPane = new StackPane();
                nextPane.getChildren().addAll(nextButton);
                nextPane.setAlignment(Pos.CENTER_RIGHT);//Next Button in Right side
                nextPane.setMinHeight(300);
                nextPane.setMaxHeight(300);
                //Back Button
                StackPane backPane = new StackPane();

                //Vboxes for our Fillin Questions
                VBox vBoxFill = new VBox();
                vBoxFill.getChildren().addAll(fillImage2,fillLabel2);
                vBoxFill.setAlignment(Pos.CENTER);
                //2
                VBox vBoxLabel = new VBox();
                vBoxLabel.getChildren().addAll(fillLabel, textTime);
                vBoxLabel.setAlignment(Pos.CENTER);

                //Buttons in vBox than in Hbox and than in BorderPane
                VBox vBox1 = new VBox(4);//Red and Orange
                VBox vBox2 = new VBox(4);//Blue and Green
                HBox hBox = new HBox(4);// All of them

                //Our first question if it Fillin
                if(questions.get(0).getDescription().contains("___")){
                    mainPane.setTop(vBoxLabel);
                    mainPane.setCenter(vBoxFill);
                    mainPane.setLeft(backPane);
                    mainPane.setRight(nextPane);
                    fillinText.set(nextCount,textFill.getText());
                    textFill.clear();
                }

                //Our first question if it Test
                else{
                    String [] answers = questions.get(0).option();
                    nextStringCount = String.valueOf(nextCount +1).concat(". ");
                    txtLabel = new Label(nextStringCount.concat(questions.get(0).getDescription()), textTime);//
                    redButton.setText(answers[0]);
                    blueButton.setText(answers[1]);
                    orangeButton.setText(answers[2]);
                    greenButton.setText(answers[3]);

                    //add buttons in Vbox than in Hbox
                    vBox1.getChildren().addAll(redButton,orangeButton);
                    vBox2.getChildren().addAll(blueButton,greenButton);
                    hBox.getChildren().addAll(vBox1, vBox2);
                    hBox.setAlignment(Pos.CENTER);

                    //Font for our Label
                    font = Font.font("monospace", FontWeight.BOLD, 18);
                    txtLabel.setFont(font);
                    txtLabel.setTextFill(Color.BLACK);
                    txtLabel.setMinHeight(100);
                    txtLabel.setMinWidth(width);
                    txtLabel.setAlignment(Pos.CENTER);
                    txtLabel.setContentDisplay(ContentDisplay.BOTTOM);

                    //Add all of them in BorderPane
                    mainPane.setTop(txtLabel);//Question
                    mainPane.setBottom(hBox); //Answers
                    mainPane.setCenter(vBoxImage); //Image and Timer
                    mainPane.setRight(nextPane); //RightButton
                    mainPane.setLeft(backPane); //LeftButton
                    BorderPane.setMargin(hBox, new Insets(3, 3, 3, 3));

                }
                //NEXT
                nextButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        backPane.getChildren().clear();
                        backPane.getChildren().add(backButton);
                        backPane.setAlignment(Pos.CENTER_LEFT);//Back Button in  Left side
                        backPane.setMinHeight(300);
                        backPane.setMaxHeight(300);

                        if (nextCount < questions.size() - 1) {
                            //we set our writerAnswer in ArrayList
                            fillinText.set(nextCount,textFill.getText());
                            nextCount++;
                            if(questions.get(nextCount).getDescription().contains("___")) {
                                textFill.clear();
                                textFill.setText(fillinText.get(nextCount));

                                mainPane.getChildren().clear();
                                nextStringCount = String.valueOf(nextCount +1).concat(". ");
                                fillLabel.setText(nextStringCount.concat(questions.get(nextCount).getDescription()));
                                vBoxLabel.getChildren().clear();
                                vBoxLabel.getChildren().addAll(fillLabel,textTime);
                                mainPane.setTop(vBoxLabel);
                                mainPane.setCenter(vBoxFill);
                                mainPane.setLeft(backPane);
                                mainPane.setRight(nextPane);
                            }
                            //If it Test
                            else{
                                String [] answer = questions.get(nextCount).option();

                                redButton.setText(answer[0]);
                                blueButton.setText(answer[1]);
                                orangeButton.setText(answer[2]);
                                greenButton.setText(answer[3]);

                                vBox1.getChildren().clear();
                                vBox2.getChildren().clear();
                                vBox1.getChildren().addAll(redButton,orangeButton);
                                vBox2.getChildren().addAll(blueButton,greenButton);
                                hBox.getChildren().clear();
                                hBox.getChildren().addAll(vBox1, vBox2);
                                hBox.setAlignment(Pos.CENTER);

                                nextStringCount = String.valueOf(nextCount +1).concat(". ");
                                txtLabel = new Label(nextStringCount.concat(questions.get(nextCount).getDescription()), textTime);//
//                                txtLabel.setText(nextStringCount.concat(questions.get(nextCount).getDescription()));
                                Font font = Font.font("monospace", FontWeight.BOLD, 18);
                                txtLabel.setFont(font);
                                txtLabel.setTextFill(Color.BLACK);
                                txtLabel.setMinHeight(100);
                                txtLabel.setMinWidth(width);
                                txtLabel.setAlignment(Pos.CENTER);
                                txtLabel.setContentDisplay(ContentDisplay.BOTTOM);

//                                redButton = setRadioButtonSelected();
//                                blueButton = setRadioButtonSelected();
//                                orangeButton = setRadioButtonSelected();
//                                greenButton = setRadioButtonSelected();

                                redButton.setSelected(false);
                                blueButton.setSelected(false);
                                orangeButton.setSelected(false);
                                greenButton.setSelected(false);
                                isSelectedButton.get(nextCount).setSelected(true);

                                mainPane.setTop(txtLabel);
                                mainPane.setCenter(vBoxImage);
                                mainPane.setBottom(hBox);
                                mainPane.setRight(nextPane);
                                mainPane.setLeft(backPane);
                            }
                            if(nextCount == questions.size()-1){
                                byte[] emojiByte = new byte[]{(byte)0xE2, (byte)0x9C, (byte)0x94};
                                String emoji = new String(emojiByte, Charset.forName("UTF-8"));
                                nextButton.setText(emoji);
                            }

                        }
                        else{
                            //The last Scene For Results
                            Label label = new Label("Your Result:");
                            label.setAlignment(Pos.CENTER);
                            label.setMinHeight(100);
                            label.setMinWidth(800);
                            Font font = Font.font("monospace", FontWeight.BOLD,20);
                            Font font1 = new Font("Time New Roman",14);
                            label.setFont(font);

                            //Check correct Test answers
                            for (int i = 0; i < arrayAnswers.size(); i++) {
                                if(arrayAnswers.get(i).equalsIgnoreCase(questions.get(i).getAnswer())){
                                    totalCorrectAnswer++;
                                }
                            }
                            //Check correct Filin answers
                            for (int i = 0; i < fillinText.size(); i++) {
                                if(fillinText.get(i).equalsIgnoreCase(questions.get(i).getAnswer())){
                                    totalCorrectAnswer++;
                                }
                            }

                            //IN percentage
                            Text text = new Text();
                            double number = (double)(totalCorrectAnswer * 100) / questions.size();
                            text.setText(String.format(Locale.US,"%.2f", number) + "%");
                            text.setX(100);
                            text.setY(800);
                            text.setStyle("-fx-font-family:Time New Roman; -fx-font-size: 16;");

                            //Correct Answers
                            Text text2 = new Text();
                            text2.setText("\nNumber of correct answers: " + totalCorrectAnswer + "/" + questions.size());
                            text2.setX(100);
                            text2.setY(800);
                            text2.setFont(font1);

                            //Save Time (Finished in ##.## );
                            String saveTime = textTime.getText();
                            Text text3 = new Text();
                            text3.setText("\nFinished in " + saveTime + "\n");
                            text3.setX(100);
                            text3.setY(800);
                            text3.setFont(font1);

                            buttonShowAnswer.setStyle("-fx-background-color: blue");
                            buttonClose.setStyle("-fx-background-color: red");
                            buttonShowAnswer.setTextFill(Color.WHITE);
                            buttonClose.setTextFill(Color.WHITE);
                            buttonShowAnswer.setFont(font);
                            buttonClose.setFont(font);
                            buttonShowAnswer.setMinWidth(400);
                            buttonShowAnswer.setMinHeight(50);
                            buttonClose.setMinWidth(400);
                            buttonClose.setMinHeight(50);

                            //Image for results
                            ImageView imageView = null;
                            try {
                                imageView = new ImageView(new Image(new FileInputStream("images/result.png")));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            imageView.setFitHeight(300);
                            imageView.setFitWidth(500);

                            //In one Vbox buttons and Image
                            VBox vBox = new VBox(7);
                            vBox.getChildren().addAll(buttonShowAnswer, buttonClose, imageView);
                            vBox.setAlignment(Pos.CENTER);

                            //Label and three texts
                            VBox vbox1 = new VBox(label, text,text2,text3);
                            vbox1.getChildren().addAll();
                            vbox1.setAlignment(Pos.CENTER);

                            //Add all of them in Border Pane ( Our Main Pane)
                            mainPane.getChildren().clear();
                            mainPane.setTop(vbox1);
                            mainPane.setCenter(vBox);
                        }
                    }
                });

                //Back Button
                backButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        nextButton.setText(">");
                        if (nextCount > 0){
                            if (nextCount == 1) {
                                backPane.getChildren().clear();
                            }
                            fillinText.set(nextCount,textFill.getText());
                            nextCount--;
                            if(questions.get(nextCount).getDescription().contains("___")) {
                                textFill.clear();
                                textFill.setText(fillinText.get(nextCount));

                                mainPane.getChildren().clear();
                                nextStringCount = String.valueOf(nextCount +1).concat(". ");
                                fillLabel.setText(nextStringCount.concat(questions.get(nextCount).getDescription()));
                                vBoxLabel.getChildren().clear();
                                vBoxLabel.getChildren().addAll(fillLabel,textTime);
                                mainPane.setTop(vBoxLabel);
                                mainPane.setCenter(vBoxFill);
                                mainPane.setLeft(backPane);
                                mainPane.setRight(nextPane);

                            }
                            else{
                                redButton.setSelected(false);
                                blueButton.setSelected(false);
                                orangeButton.setSelected(false);
                                greenButton.setSelected(false);
                                isSelectedButton.get(nextCount).setSelected(true);

                                nextStringCount = String.valueOf(nextCount +1).concat(". ");
                                //we rewrite this method for adding time in test when we go back
                                txtLabel = new Label(nextStringCount.concat(questions.get(nextCount).getDescription()), textTime);
                                Font font = Font.font("monospace", FontWeight.BOLD, 18);
                                txtLabel.setFont(font);
                                txtLabel.setTextFill(Color.BLACK);
                                txtLabel.setMinHeight(100);
                                txtLabel.setMinWidth(width);
                                txtLabel.setAlignment(Pos.CENTER);
                                txtLabel.setContentDisplay(ContentDisplay.BOTTOM);
//                                txtLabel.setText(nextStringCount.concat(questions.get(nextCount).getDescription()));
                                String[] answer = questions.get(nextCount).option();
                                redButton.setText(answer[0]);
                                blueButton.setText(answer[1]);
                                orangeButton.setText(answer[2]);
                                greenButton.setText(answer[3]);

                                mainPane.setTop(txtLabel);
                                mainPane.setCenter(vBoxImage);
                                mainPane.setBottom(hBox);
                                mainPane.setRight(nextPane);
                                mainPane.setLeft(backPane);

                            }
                        }
                    }
                });

                /*  OUR BUTTONS, WHEN WE FOCUSED - IT SAVED RESULTS IN ARRAYLIST */

                redButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(redButton.isSelected()){
                            arrayAnswers.set(nextCount,redButton.getText());
                            isSelectedButton.set(nextCount,redButton);
                        }

                    }
                });

                blueButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(blueButton.isSelected()){
                            arrayAnswers.set(nextCount,blueButton.getText());
                            isSelectedButton.set(nextCount,blueButton);
                        }
                    }
                });

                orangeButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(orangeButton.isSelected()){
                            arrayAnswers.set(nextCount,orangeButton.getText());
                            isSelectedButton.set(nextCount,orangeButton);
                        }
                    }
                });

                greenButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(greenButton.isSelected()){
                            arrayAnswers.set(nextCount,greenButton.getText());
                            isSelectedButton.set(nextCount,greenButton);
                        }
                    }
                });

                //Show Answers Button
                buttonShowAnswer.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        musicPlayer.stop();

                        //New Music for ShowAnswers
                        String musicFile = "showMusic.mp3";
                        Media sound = new Media(new File(musicFile).toURI().toString());
                        MediaPlayer music = new MediaPlayer(sound);
                        music.play();

                        //It will be a new Scene with new Border Pane
                        BorderPane showPane = new BorderPane();
                        ArrayList<String> showAll = new ArrayList<>();

                        for (int i = 0; i < questions.size(); i++) {
                            showAll.add((i+1)+") "+questions.get(i).getDescription());
                            for (int j = 0; j < 4; j++) {
                                if ((questions.get(i) instanceof Test)) {
                                    showAll.add((char)(65 + j) +") "+questions.get(i).option()[j]);
                                } else {
                                    showAll.add("I) "+questions.get(i).getAnswer());
                                    j = 4;
                                }
                            }
                            showAll.add("`");
                            showAll.add("");
                        }
                        //Font for all Labels
                        Font font = Font.font("monospace", FontWeight.BOLD, 15);
                        //For all Labels
                        VBox vbox1 = new VBox(5);

                        //Set our Questions and Answers in Bordre Pane
                        int n = 0;
                        for (int i = 0; i < showAll.size(); i++) {
                            Label placeH = new Label();
                            placeH.setText(showAll.get(i));
                            placeH.setFont(font);
                            //
                            if(n < questions.size()) {
                                //
                                if(placeH.getText().contains(arrayAnswers.get(n))){
                                    if(arrayAnswers.get(n).equals(questions.get(n).getAnswer())){
                                        placeH.setStyle("-fx-background-color: lime");
                                    }else {
                                        if(!arrayAnswers.get(n).equals(questions.get(n).getAnswer())){
                                            placeH.setStyle("-fx-background-color: red");
                                        }else
                                            placeH.setStyle("-fx-background-color: white");
                                    }
                                    if(placeH.getText().contains("I)")){
                                        placeH.setText("I) "+fillinText.get(n));
                                        if(!fillinText.get(n).equalsIgnoreCase(questions.get(n).getAnswer())){
                                            placeH.setStyle("-fx-background-color: red");
                                        }else
                                            placeH.setStyle("-fx-background-color: lime");
                                    }
                                }
                                if (placeH.getText().equals("`")) {
                                    placeH.setStyle("-fx-background-color: lime");
                                    placeH.setText("Correct Answer: " + questions.get(n).getAnswer()+ "\n");
                                    n++;
                                }
                            }

                            if(placeH.getText().contains("?") || placeH.getText().contains("___")){
                                placeH.setStyle("-fx-background-color: skyblue");
                            }
                            vbox1.getChildren().addAll(placeH);
                        }
                        //Gif for Show Answers
                        ImageView gif = new ImageView();
                        Image imageGif = new Image(new File("images/k.gif").toURI().toString());
                        gif.setImage(imageGif);
                        gif.setFitHeight(200);
                        gif.setFitWidth(400);

                        //Label and Button for Closing Test
                        Label labelClose  = new Label("Thanks for taking the quiz!");
                        labelClose.setStyle("-fx-font-family:monospace; -fx-font-size:20px; -fx-text-fill:black;");
                        Button buttonGoBack = new Button("Close");
                        buttonGoBack.setStyle("-fx-background-color: DARKSLATEBLUE; -fx-text-fill: white; -fx-font-family: monospace; -fx-font-size: 15px");
                        buttonGoBack.setMinWidth(200);
                        buttonGoBack.setMinHeight(30);
                        buttonGoBack.setAlignment(Pos.CENTER);

                        //Add in Vbox Text and Close Button
                        VBox vBoxCloseButton = new VBox();
                        vBoxCloseButton.getChildren().addAll(labelClose, buttonGoBack);
                        vBoxCloseButton.setAlignment(Pos.CENTER);

                        //Emoji just for Answers
                        byte[] emojiAnswer = new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x93,(byte)0x8A};
                        String answerEmoji = "Answers:";
                        answerEmoji += new String(emojiAnswer, Charset.forName("UTF-8"));

                        //Label Answers
                        Label labelGif = new Label(answerEmoji);
                        labelGif.setStyle("-fx-font-family: Verdana; -fx-font-size:20px; -fx-background-color:black; -fx-text-fill:white;");
                        labelGif.setAlignment(Pos.CENTER);

                        //ScrollBar for seeing Answers
                        ScrollBar scrollBar = new ScrollBar();
                        scrollBar.setOrientation(Orientation.VERTICAL);
                        scrollBar.setMin(0);
                        scrollBar.setMax(730);
                        scrollBar.setStyle("-fx-background-color: gray");
                        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
                            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                                showPane.setLayoutY(-new_val.doubleValue());
                            }
                        });

                        //Add gif and AnswerLabel in Vbox
                        VBox vBoxGif = new VBox(5);
                        vBoxGif.getChildren().addAll(gif, labelGif);
                        vBoxGif.setAlignment(Pos.CENTER);

                        VBox answerAndButton = new VBox();
                        answerAndButton.getChildren().addAll(vbox1, vBoxCloseButton);
                        //And all of them in new BorderPane
                        showPane.setTop(vBoxGif);
                        showPane.setCenter(answerAndButton);
                        showPane.setRight(scrollBar);

                        //If it choose buttonBack it will Close
                        buttonGoBack.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                if(buttonGoBack.isFocused()){
                                    stage.close();
                                }
                            }
                        });

                        //if show Answers the new scene will Start
                        if(buttonShowAnswer.isFocused()){
                            stage.setScene(new Scene(showPane,width,height));
                        }

                    }
                });

                //Button Close if we don't choose showAnswer
                buttonClose.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(buttonClose.isFocused()){
                            stage.close();
                        }
                    }
                });

            }
        });

        //Tittle, icon and show();
        stage.setTitle("Kahoot");
        stage.getIcons().add(new Image(new FileInputStream("images/k.png")));
        stage.show();

    }

}