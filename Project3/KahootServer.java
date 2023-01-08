import com.example.java2semester.Project2.Test;
import com.example.java2semester.Project3.Question;
import com.example.java2semester.Project3.Quiz;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.CookieHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.*;

public class KahootServer extends Application {
    private DataOutputStream toClient = null;
    private DataInputStream fromClient = null;
    private ObjectInputStream objIn = null;
    private ObjectOutputStream objOut = null;
    private final int width = 800;
    private final int height = 600;
    private int second = 31;
    private String nickClient = "";
    private int counterClient = 0;
    private int clientCounter = 0;
    private Text nickNames = new Text();
    private int randomNumber = 0;
    private Text textCounter = new Text();
    private String fileName = "";
    private int nextCount = 0;
    private ArrayList<String> arrayAnswers = new ArrayList<>();
    private ArrayList<String> getOptions = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<String> answerButtons = new ArrayList<>();
    private ServerSocket serverSocket;
    private Socket socket;
    private int nextQuestion = 0;
    private int numberOfQuestion = 0;
    private int a = 0;
    private String chooseButton = "";
    private String chooseButtonServer = "";
    private int score = 0;
    private int answersInt = 0;
    private Text textAnswersInt = new Text();
    private String questionsString = "";
    private int countCorrectAnswers = 0;
    private int score2 = 0;
    //
    private BorderPane mainPane = new BorderPane();
    private Label testQuestions = new Label();
    //Buttons
    Button red = new Button();
    Button blue = new Button();
    Button orange = new Button();
    Button green = new Button();
    Button trueButton = new Button();
    Button falseButton = new Button();
    Button buttonGoBack = new Button("Close");

    VBox vBox1 = new VBox(4);//Red and Orange
    VBox vBox2 = new VBox(4);//Blue and Green
    HBox hBox = new HBox(4);// All of them


    @Override
    public void start(Stage stage) throws Exception {

        Text textScene = new Text("\n\n\n\n\n\n\n\n\n\n\nBefore starting Choose a file:");
        textScene.setFill(Color.WHITE);
        textScene.setFont(Font.font("Verdana", FontWeight.BOLD,FontPosture.ITALIC ,18));
        VBox vBoxScene = new VBox();
        vBoxScene.setAlignment(Pos.TOP_CENTER);
        vBoxScene.getChildren().add(textScene);
        //Button for Choosing file
        Button buttonChooseFile = new Button("Choose a file");
        buttonChooseFile.setFont(Font.font("Verdana", FontWeight.BOLD, 16));

        //file chooser
        FileChooser fileChooser = new FileChooser();

        //image for First Scene
        ImageView imageFile = new ImageView(new Image(new FileInputStream("images/background.jpg")));
        imageFile.setFitHeight(height);//600
        imageFile.setFitWidth(width);//800
        StackPane stackScene = new StackPane();
        stackScene.getChildren().addAll(imageFile,vBoxScene,buttonChooseFile);

        //Show the first scene
        Scene sceneFirst = new Scene(stackScene, width, height);
        stage.setScene(sceneFirst);
        stage.show();


        StackPane stackPane = new StackPane();
//      stage.setScene(new Scene(stackPane, width, height));

        buttonChooseFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(new Scene(stackPane, width, height));
                File file = fileChooser.showOpenDialog(stage);
                fileName = file.getName();

                Quiz quiz = new Quiz();

                try {
                    quiz.loadFromFile(fileName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                questions = quiz.getAll();

                //Send to Client number of Questions
                numberOfQuestion  = questions.size();

                for (int i = 0; i < questions.size(); i++) {
                    questionsString += questions.get(i).getDescription() + ",";
                }

            }
        });



        ImageView loginImage = new ImageView(new Image(new FileInputStream("images/colorsLogin.gif")));
        loginImage.setFitHeight(height);
        loginImage.setFitWidth(width);

        randomNumber = getSixNumber();

        Label labelServer = new Label("Join at KahootClient!\n     with Game PIN: " + "\n\t   " + randomNumber);
        labelServer.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));

        Label labelKahoot = new Label("Kahoot!");
        labelKahoot.setFont(Font.font("Billabong", FontWeight.BOLD, FontPosture.REGULAR, 80));

        VBox vBoxLabel = new VBox();
        vBoxLabel.getChildren().addAll(labelServer);
        vBoxLabel.setAlignment(Pos.TOP_CENTER);


        StackPane stackLeft = new StackPane();
        textCounter = new Text("\t"+clientCounter+  "\n    Player");
        textCounter.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 17));
        stackLeft.getChildren().add(textCounter);
        stackLeft.setAlignment(Pos.CENTER_LEFT);

        StackPane stackBottom  = new StackPane();
        nickNames.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        stackBottom.getChildren().add(nickNames);
        stackBottom.setAlignment(Pos.BOTTOM_LEFT);

        StackPane stackRight = new StackPane();
        Button buttonStart = new Button("Start");
        buttonStart.setStyle("-fx-background-color: DIMGRAY; -fx-text-fill: white");
        buttonStart.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        stackRight.getChildren().add(buttonStart);
        stackRight.setAlignment(Pos.CENTER_RIGHT);

        stackPane.getChildren().addAll(loginImage, vBoxLabel, labelKahoot, stackLeft, stackBottom, stackRight);


        ImageView imageGreen = new ImageView(new Image(new FileInputStream("images/greenKahoot.png")));
        ImageView imageRed = new ImageView(new Image(new FileInputStream("images/redKahoot.png")));
        ImageView imageStart = new ImageView(new Image(new FileInputStream("images/startPic.png")));
        ImageView imageResult = new ImageView(new Image(new FileInputStream("images/resultLast.png")));
        ImageView imageGifResult = new ImageView(new Image(new FileInputStream("images/podium.gif")));
        ImageView imageConfetti = new ImageView(new Image(new FileInputStream("images/confetti.gif")));
        ImageView imageResultLast2 = new ImageView(new Image(new FileInputStream("images/resultLast2.png")));


        Text textScore = new Text();
        textScore.setFill(Color.WHITE);
        textScore.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        StackPane stackScore = new StackPane();
        stackScore.getChildren().addAll(imageGreen, textScore);
        stackScore.setAlignment(Pos.CENTER);

        Text textError = new Text();
        textError.setFill(Color.WHITE);
        textError.setStyle("-fx-background-color: red");
        textError.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        StackPane stackError = new StackPane();
        stackError.getChildren().addAll(imageRed, textError);
        stackError.setAlignment(Pos.CENTER);

        imageGreen.setFitWidth(500);
        imageGreen.setFitHeight(300);

        imageRed.setFitWidth(500);
        imageRed.setFitHeight(300);

        imageStart.setFitWidth(width);
        imageStart.setFitHeight(height);

        imageResult.setFitWidth(width);
        imageResult.setFitHeight(height);

        imageGifResult.setFitWidth(width);
        imageGifResult.setFitHeight(height);

        imageResultLast2.setFitWidth(300);
        imageResultLast2.setFitHeight(300);

        StackPane stackAnswersInt = new StackPane();
        textAnswersInt.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        stackAnswersInt.getChildren().addAll(textAnswersInt);
        stackAnswersInt.setAlignment(Pos.CENTER_RIGHT);



        new Thread( () -> {
            try {
                serverSocket = new ServerSocket(777);


                while (true) {
                    socket = serverSocket.accept();

                    counterClient++;

                    Platform.runLater( () -> {});

                    new Thread(new KahootServer.HandleAClient(socket)).start();

                }
            }
            catch(IOException ex) {
                System.out.println(ex);
            }
        }).start();


        //Start
        buttonStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(clientCounter <= 0){
                    nickNames.setText("\t\t\t\t\tNo client has joined!\n\n\n\n\n\n");
                }else{

                    //After stating the game we send to Client value of True for startingGAME
                    try {
                        toClient.writeBoolean(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Quiz quiz = new Quiz();

                    try {
                        quiz.loadFromFile(fileName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    ArrayList<Question> questions = quiz.getAll();
                    //Shuffling
//                    Collections.shuffle(questions);


                    //Skip Button
                    StackPane stackSkip = new StackPane();
                    Button skipButton = new Button("Now you can Start!\n\tGood look!");
                    skipButton.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
                    skipButton.setTextFill(Color.WHITE);
                    skipButton.setStyle("-fx-background-color: RED");
                    skipButton.setMinHeight(40);
                    skipButton.setMinWidth(60);
                    stackSkip.getChildren().add(skipButton);
                    stackSkip.setAlignment(Pos.CENTER_RIGHT);

                    Button skipButton2 = new Button("SKIP");
                    skipButton2.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
                    StackPane stackSkip2 = new StackPane();
                    skipButton2.setTextFill(Color.WHITE);
                    skipButton2.setStyle("-fx-background-color: RED");
                    skipButton2.setMinHeight(40);
                    skipButton2.setMinWidth(60);
                    stackSkip2.getChildren().add(skipButton2);
                    stackSkip2.setAlignment(Pos.CENTER_RIGHT);

                    //Answers and SKipBUtton
                    VBox rightSide = new VBox(10);
                    rightSide.getChildren().addAll(stackSkip2, textAnswersInt);
                    rightSide.setAlignment(Pos.BASELINE_RIGHT);

                    //Timer for Questions
                    StackPane stackTime = new StackPane();
                    Circle circleTime = new Circle(40);
                    circleTime.setFill(Color.SLATEBLUE);

                    //Timer for All Questions
                    Timer timer = new Timer();
                    Text textTime = new Text();
                    textTime.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
                    textTime.setFill(Color.WHITE);

                    //Write our algorithm
                    TimerTask task = new TimerTask(){
                        public void run(){
                            second--;
                            if(second == 0){
                                second = 30;
                            }
                            if(second < 10){
                                textTime.setText("   " +second);
                            }else
                                textTime.setText("  "+second);
                        }
                    };


                    timer.scheduleAtFixedRate(task, 1000,1000);


                    Label labelCountQuestion = new Label(nextQuestion + " / " + numberOfQuestion);
                    labelCountQuestion.setContentDisplay(ContentDisplay.RIGHT);
                    labelCountQuestion.setFont(Font.font("Verdana", FontWeight.BOLD, 17));

                    //Image
                    ImageView imageView = null;
                    try {
                        imageView = new ImageView(new Image(new FileInputStream("images/gifKahoot.gif")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    imageView.setFitHeight(250);
                    imageView.setFitWidth(400);
                    VBox vBoxImage = new VBox(10);
                    vBoxImage.getChildren().addAll(labelCountQuestion, imageView);
                    vBoxImage.setAlignment(Pos.CENTER);


                    //Buttons
                    red = forButtons("crimson");
                    blue = forButtons("royalblue");
                    orange = forButtons("orange");
                    green = forButtons("yellowgreen");
                    trueButton = forButtons("royalblue");
                    falseButton = forButtons("crimson");

                    //SET ID NAME FOR ALL BUTTONS
                    red.setId("RED");
                    blue.setId("BLUE");
                    orange.setId("ORANGE");
                    green.setId("GREEN");
                    trueButton.setId("TRUE");
                    falseButton.setId("FALSE");

                    //Possition of text
                    red.setAlignment(Pos.CENTER_LEFT);
                    blue.setAlignment(Pos.CENTER_LEFT);
                    orange.setAlignment(Pos.CENTER_LEFT);
                    green.setAlignment(Pos.CENTER_LEFT);
                    trueButton.setAlignment(Pos.CENTER_LEFT);
                    falseButton.setAlignment(Pos.CENTER_LEFT);

                    //Question
                    testQuestions = forLabel(questions.get(nextCount).getDescription());

                    //Shapes for Answers
                    Polygon polygon = new Polygon();
                    polygon.getPoints().addAll(new Double[]{25.0, 10.0, 10.0, 35.0, 40.0, 35.0});
                    polygon.setFill(Color.WHITE);
                    red.setGraphic(polygon);

                    //Circle
                    Circle circle = new Circle(12);
                    circle.setFill(Color.WHITE);
                    orange.setGraphic(circle);

                    //Square
                    Rectangle rectangle1 = new Rectangle(20,20,20,20);
                    rectangle1.setFill(Color.WHITE);
                    rectangle1.setRotate(45);
                    blue.setGraphic(rectangle1);

                    //Square2
                    Rectangle rectangle2 = new Rectangle(20,20,20,20);
                    rectangle2.setFill(Color.WHITE);
                    green.setGraphic(rectangle2);


                    vBox1.getChildren().addAll(red,orange);
                    vBox2.getChildren().addAll(blue,green);
                    hBox.getChildren().addAll(vBox1, vBox2);
                    hBox.setAlignment(Pos.CENTER);


                    StackPane stackStart = new StackPane();
                    stackStart.getChildren().addAll(imageStart, skipButton);

                    mainPane.setCenter(stackStart);

                    BorderPane.setMargin(hBox, new Insets(3, 3, 3, 3));

                    //First Music
                    String musicFile = "showMusic.mp3";
                    Media sound = new Media(new File(musicFile).toURI().toString());
                    MediaPlayer musicPlayer = new MediaPlayer(sound);

                    //Second Music
                    String musicFile2 = "showMusic.mp3";
                    Media sound2 = new Media(new File(musicFile2).toURI().toString());
                    MediaPlayer musicPlayer2 = new MediaPlayer(sound2);


                    stage.setScene(new Scene(mainPane, width, height));

                    skipButton.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent actionEvent) {

                            musicPlayer.play();

                            if(nextQuestion != numberOfQuestion ){
                                nextQuestion++;
                                labelCountQuestion.setText(nextQuestion + " of " + numberOfQuestion);
                            }


                            if(nextCount <= questions.size() - 1){

                                textTime.setText("");
                                second = 31;
                                stackTime.getChildren().addAll(circleTime, textTime);
                                stackTime.setAlignment(Pos.CENTER_LEFT);
                                Button [] buttons = {red, blue, orange, green, trueButton, falseButton};

                                if(!questions.get(nextCount).getDescription().contains("___")){
                                    String [] answer = questions.get(nextCount).option();
                                    Collections.shuffle(Arrays.asList(answer));

                                    red.setText(" "+ answer[0]);
                                    blue.setText(" "+ answer[1]);
                                    orange.setText(" " + answer[2]);
                                    green.setText(" " + answer[3]);

                                    vBox1.getChildren().clear();
                                    vBox2.getChildren().clear();
                                    vBox1.getChildren().addAll(red,orange);
                                    vBox2.getChildren().addAll(blue,green);
                                    hBox.getChildren().clear();
                                    hBox.getChildren().addAll(vBox1, vBox2);
                                    hBox.setAlignment(Pos.CENTER);

                                    //Question LABEL
                                    testQuestions = forLabel(questions.get(nextCount).getDescription());


                                    red.setDisable(false);
                                    blue.setDisable(false);
                                    orange.setDisable(false);
                                    green.setDisable(false);
                                    trueButton.setDisable(false);
                                    falseButton.setDisable(false);

                                    mainPane.setTop(testQuestions);
                                    mainPane.setCenter(vBoxImage);
                                    mainPane.setBottom(hBox);
                                    mainPane.setRight(rightSide);
                                    mainPane.setLeft(stackTime);

                                }
                                else {

                                    testQuestions = forLabel(questions.get(nextCount).getDescription().replace("___","."));


                                    Polygon polygon2 = new Polygon();
                                    polygon2.getPoints().addAll(new Double[]{25.0, 10.0, 10.0, 35.0, 40.0, 35.0});
                                    polygon2.setFill(Color.WHITE);
                                    falseButton.setGraphic(polygon2);

                                    Rectangle rectangle2 = new Rectangle(20,20,20,20);
                                    rectangle2.setFill(Color.WHITE);
                                    rectangle2.setRotate(45);
                                    trueButton.setGraphic(rectangle2);

                                    falseButton.setText(" False");
                                    trueButton.setText(" True");
                                    vBox1.getChildren().clear();
                                    vBox2.getChildren().clear();
                                    vBox1.getChildren().addAll(falseButton);
                                    vBox2.getChildren().add(trueButton);
                                    hBox.getChildren().clear();
                                    hBox.getChildren().addAll(vBox1, vBox2);
                                    hBox.setAlignment(Pos.CENTER);

                                    trueButton.setDisable(false);
                                    falseButton.setDisable(false);


                                    mainPane.setTop(testQuestions);
                                    mainPane.setCenter(vBoxImage);
                                    mainPane.setBottom(hBox);
                                    mainPane.setRight(rightSide);
                                    mainPane.setLeft(stackTime);
                                }

                                skipButton2.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {

                                        if(nextCount > questions.size()-1){
                                            byte[] emojiByte = new byte[]{(byte)0xE2, (byte)0x9C, (byte)0x94};
                                            String emoji = new String(emojiByte, Charset.forName("UTF-8"));
                                            skipButton.setText("FINISH");
                                        }else {
                                            skipButton.setText("NEXT");
                                        }

                                        skipButton.setStyle("-fx-background-color: MEDIUMSLATEBLUE");
                                        stackSkip.getChildren().clear();
                                        stackSkip.getChildren().add(skipButton);

                                        red.setDisable(true);
                                        blue.setDisable(true);
                                        orange.setDisable(true);
                                        green.setDisable(true);
                                        trueButton.setDisable(true);
                                        falseButton.setDisable(true);


                                        if(a <= numberOfQuestion){ //7
                                            for (int i = 0; i < 6; i++) {
                                                if(buttons[i].getText().contains(questions.get(a).getAnswer())){
                                                    buttons[i].setDisable(false);
                                                    chooseButtonServer = buttons[i].getId();
                                                }
                                            }
                                            a++;
                                        }


                                        if(chooseButtonServer.equals(chooseButton)){
                                            score = score + 783;
                                            textScore.setText("\n\n\n\n"+ "+" + score);
                                            mainPane.setCenter(stackScore);
                                            countCorrectAnswers ++;
                                        }else{

                                            for (int i = 0; i < 4; i++) {
                                                if(chooseButton.equals(buttons[i].getId()))
                                                    chooseButton = buttons[i].getText();
                                            }

                                            textError.setText("Your answer: " + chooseButton + "\n\n\n\n\n\n\n\n\n\n\n\n\n");
                                            score = score - 246;
                                            mainPane.setCenter(stackError);
                                        }
                                        //clear them
                                        stackTime.getChildren().clear();
                                        textAnswersInt.setText("");

                                        mainPane.setTop(testQuestions);
                                        mainPane.setBottom(hBox);
                                        mainPane.setRight(stackSkip);

                                    }
                                });


                                if(!chooseButton.equals("1")){
                                    if(answersInt == 1){
                                        answersInt = 0;
                                    }
                                    answersInt++;
                                }
                                textAnswersInt.setText("\n\n      "+ 0 + "\nAnswers  ");

                                nextCount++;
                            }

                            else {

                                musicPlayer.pause();
                                musicPlayer2.play();

                                //RESULST
                                mainPane.getChildren().clear();
                                StackPane stackResult = new StackPane();

                                score2 = score;
                                System.out.println(score2);


                                Timeline timeline = new Timeline(

                                        new KeyFrame(Duration.ZERO, e -> {

                                            stackResult.getChildren().add(imageGifResult);
                                            stage.setScene(new Scene(stackResult, 800, 600));

                                        }),

                                        new KeyFrame(Duration.seconds(3), e -> {

                                            String nick = nickClient.split("\t")[1];

                                            Label labelResult = new Label(  nick + "\n\n\n\n\n" );
                                            labelResult.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
                                            labelResult.setTextFill(Color.WHITE);
                                            labelResult.setAlignment(Pos.TOP_CENTER);

                                            Label labelResultScore = new Label();
                                            labelResultScore.setFont(Font.font("Verdana",FontWeight.BOLD, 15));
                                            labelResultScore.setTextFill(Color.WHITE);

                                            if(score > 0){
                                                labelResultScore.setText( "\n\n\n\n\n\n  " + score + "  points" + "\n    " +  countCorrectAnswers + " out of " + questions.size());

                                            }else{
                                                labelResultScore.setText( "  " + 0 + "  points" + "\n  " +  countCorrectAnswers + " out of " + questions.size());
                                            }

                                            Label labelClose  = new Label(" Thanks for taking Quiz!");
                                            labelClose.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
                                            labelClose.setStyle("-fx-text-fill:white;");
                                            labelClose.setAlignment(Pos.BOTTOM_CENTER);

                                            buttonGoBack.setStyle("-fx-background-color: WHITE; -fx-text-fill: BLACK; -fx-font-family: Times New Roman; -fx-font-size: 17px");
                                            buttonGoBack.setMinWidth(220);
                                            buttonGoBack.setMinHeight(30);

                                            //Add in Vbox Text and Close Button
                                            VBox vBoxCloseButton = new VBox(10);
                                            vBoxCloseButton.getChildren().addAll(labelClose, buttonGoBack);
                                            vBoxCloseButton.setMaxHeight(100);
                                            vBoxCloseButton.setMaxWidth(270);

                                            StackPane stackPane1 = new StackPane();
                                            stackPane1.getChildren().addAll(vBoxCloseButton);
                                            stackPane1.setAlignment(Pos.BOTTOM_CENTER);


                                            stackResult.getChildren().addAll(imageResult,imageConfetti, imageResultLast2, labelResult, labelResultScore, stackPane1);

                                            stage.setScene(new Scene(stackResult, width, height));


                                        })
                                );

                                timeline.play();

                                buttonGoBack.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        musicPlayer2.pause();
                                        stage.close();
                                    }
                                });

                            }

                        }
                    });
                }
            }
        });

    }

    //Client Handler
    class HandleAClient implements Runnable {
        private Socket socket;

        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try{

                toClient   = new DataOutputStream(socket.getOutputStream());
                fromClient = new DataInputStream(socket.getInputStream());

                objIn = new ObjectInputStream(socket.getInputStream());
                objOut = new ObjectOutputStream(socket.getOutputStream());


                while(true){
                    // get name clients;
                    nickClient += fromClient.readUTF() + "\t";

                    clientCounter += fromClient.readInt();

                    chooseButton = fromClient.readUTF();

                    //send to Client six random number
                    toClient.writeInt(randomNumber);
                    toClient.flush();

                    toClient.writeBoolean(false);
                    toClient.flush();

                    toClient.writeInt(numberOfQuestion);
                    toClient.flush();

                    toClient.writeUTF(questionsString);
                    toClient.flush();


                    Platform.runLater(() -> {
                        nickNames.setText("\t\t\t" + nickClient +"\n\n\n\n\n");
                        textCounter.setText("\t"+clientCounter+  "\n    Player");
                        textAnswersInt.setText("\n\n      "+ answersInt + "\nAnswers  ");

                    });
                }

            } catch (IOException  e) {
                e.printStackTrace();
            }
        }
    }

    public static int getSixNumber(){

        return (int) (Math.random() * 1000000);
    }

    public static Button forButtons(String c){

        Button b = new Button();
        b.setStyle("-fx-background-color: " + c);
        b.setMinWidth(395);
        b.setMinHeight(90);
        b.setTextFill(Color.WHITE);
        b.setFont(Font.font("monospace",FontWeight.BOLD,18));

        return b;
    }
    public static Label forLabel(String s){
        Label txtLabel = new Label(s);
        txtLabel.setFont(Font.font("monospace", FontWeight.BOLD, 20));
        txtLabel.setTextFill(Color.BLACK);
        txtLabel.setMinHeight(100);
        txtLabel.setMinWidth(800);
        txtLabel.setAlignment(Pos.CENTER);
        txtLabel.setContentDisplay(ContentDisplay.BOTTOM);

        return txtLabel;
    }


}
