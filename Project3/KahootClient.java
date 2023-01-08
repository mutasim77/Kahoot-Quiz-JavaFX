import com.example.java2semester.Project2.Question;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class KahootClient extends Application {
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;
    private ObjectOutputStream objOut = null;
    private ObjectInputStream objIn = null;
    private final int width = 600;
    private final int height = 600;
    private int randomNumber = 0;
    private int clientNumber = 0;
    private boolean start = false;
    private ArrayList<Button> chooseButtons = new ArrayList<>();
    private Socket socket = new Socket();
    private int numberOfQuestion = 0;
    private int nextQuestion = 0;
    private Label labelName;
    private ArrayList<String> answerButtons = new ArrayList<>();
    private ArrayList<String> nick = new ArrayList<>();
    private char chooseButton = ' ';
    private String trueOrFalse = "";
    private String questions = "";
    private ArrayList<String> questionsArrayList;
    private String [] questionsArray;
    private int score = 0;
    //Buttons
    Button red = new Button();
    Button blue = new Button();
    Button orange = new Button();
    Button green = new Button();
    Button trueButton = new Button();
    Button falseButton = new Button();
    Button goButton = new Button();
    VBox vBox1 = new VBox(10);//Red and Orange
    VBox vBox2 = new VBox(10);//Blue and Green
    HBox hBox = new HBox(10);// All of them

    @Override
    public void start(Stage stage) throws Exception {
        StackPane loginPane = new StackPane();

        ImageView loginImage = new ImageView(new Image(new FileInputStream("images/colorsLogin.gif")));
        loginImage.setFitHeight(height);
        loginImage.setFitWidth(width);
        TextField loginField = new TextField();
        loginField.setMinSize(200,35);
        loginField.setMaxSize(200,35);
        loginField.setPromptText("Game PIN");
        loginField.setFocusTraversable(false);
        loginField.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));


        Label labelLogin = new Label("Kahoot!");
        labelLogin.setFont(Font.font("Billabong", FontWeight.BOLD, FontPosture.REGULAR, 60));

        Button loginButton = new Button("Enter");
        loginButton.setStyle("-fx-font-family: Verdana; -fx-background-color: DIMGRAY");
        loginButton.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        loginButton.setMinWidth(200);
        loginButton.setMinHeight(35);

        ImageView whiteLogin = new ImageView(new Image(new FileInputStream("images/loginWhite.jpg")));
        whiteLogin.setFitWidth(270);
        whiteLogin.setFitHeight(130);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(labelLogin,loginField, loginButton);
        vBox.setAlignment(Pos.CENTER);

        Label nickResult = new Label();
        nickResult.setAlignment(Pos.CENTER_LEFT);
        nickResult.setTextFill(Color.BLACK);
        nickResult.setFont(Font.font("Verdana", FontWeight.BOLD, 19));

        ImageView imageResult = new ImageView(new Image(new FileInputStream("images/resultKahoot2.png")));
        imageResult.setFitWidth(width);
        imageResult.setFitHeight(height);


        ImageView imageView3 = new ImageView(new Image(new FileInputStream("images/coolgif.gif")));
        imageView3.setFitWidth(90);
        imageView3.setFitHeight(90);

        ImageView imageView5 = new ImageView(new Image(new FileInputStream("images/cool2.gif")));
        imageView5.setFitWidth(390);
        imageView5.setFitHeight(190);

        loginPane.getChildren().addAll(loginImage,vBox);


        //LOADING
        StackPane stackPaneLoading = new StackPane();

        ImageView imageLoading = new ImageView(new Image(new FileInputStream("images/loading.gif")));

        ImageView imageLoadingPic = new ImageView(new Image(new FileInputStream("images/share2.png")));
        imageLoadingPic.setFitWidth(150);
        imageLoadingPic.setFitHeight(100);


        ImageView imageShare4 = new ImageView(new Image(new FileInputStream("images/share3.png")));
        imageShare4.setFitWidth(100);
        imageShare4.setFitHeight(100);
        VBox vBoxShare4 = new VBox();
        vBoxShare4.getChildren().addAll(imageShare4);
        vBoxShare4.setAlignment(Pos.BOTTOM_LEFT);

        Label labelLoading = new Label("Kahoot!\n\n\n ");
        labelLoading.setFont(Font.font("Billabong", FontWeight.BOLD, FontPosture.REGULAR, 80));
        labelLoading.setTextFill(Color.BLUEVIOLET);

        VBox vBoxLoading = new VBox();
        vBoxLoading.getChildren().addAll(labelLoading);
        vBoxLoading.setAlignment(Pos.CENTER);


        VBox vBoxLoadingPic = new VBox();
        vBoxLoadingPic.getChildren().addAll(imageLoadingPic);
        vBoxLoadingPic.setAlignment(Pos.BASELINE_RIGHT);

        Label labelLoading2 = new Label("\nKahoot!");
        labelLoading2.setFont(Font.font("Billabong", FontWeight.BOLD, FontPosture.REGULAR, 70));
        labelLoading2.setTextFill(Color.LIMEGREEN);

        FadeTransition ft = new FadeTransition(Duration.millis(3000), labelLoading2);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(10);
        ft.setAutoReverse(true);
        ft.play();

        //END OF LOADING


        //RESULT SHOW
        ImageView imageLoadingClient = new ImageView(new Image(new FileInputStream("images/loadingClient.gif")));
        imageLoadingClient.setFitWidth(600);
        imageLoadingClient.setFitHeight(600);

        // ImageLogin color changing

        ImageView imageConfetti = new ImageView(new Image(new FileInputStream("images/confetti.gif")));

        ImageView imageGold = new ImageView(new Image(new FileInputStream("images/goldResult.png")));
        imageGold.setFitHeight(100);
        imageGold.setFitWidth(100);

        ImageView imagelastResult = new ImageView(new Image(new FileInputStream("images/lastResult.png")));
        VBox vBoxLast = new VBox();
        vBoxLast.getChildren().addAll(imagelastResult);
        vBoxLast.setAlignment(Pos.BOTTOM_CENTER);


        Label labelLoading3 = new Label("Kahoot!\n\n\n ");
        labelLoading3.setFont(Font.font("Billabong", FontWeight.BOLD, FontPosture.REGULAR, 70));
        labelLoading3.setTextFill(Color.DARKMAGENTA);

        VBox vBoxLoadingPic2 = new VBox();
        vBoxLoadingPic2.getChildren().addAll(labelLoading3);
        vBoxLoadingPic2.setAlignment(Pos.CENTER);

        Label labelNameResult = new Label();
        labelNameResult.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
        labelNameResult.setTextFill(Color.WHITE);

        Label labelQuote = new Label("\n\n\n\"If you can dream it, you can do it!\"");
        labelQuote.setFont(Font.font("Monospace", FontWeight.BOLD, FontPosture.REGULAR, 10));
        labelQuote.setTextFill(Color.BLACK);
        VBox vBoxQuote = new VBox();
        vBoxQuote.getChildren().add(labelQuote);
        vBoxQuote.setAlignment(Pos.TOP_CENTER);

        VBox vBoxLoadingPic3 = new VBox();
        vBoxLoadingPic3.getChildren().addAll(labelLoading3);
        vBoxLoadingPic3.setAlignment(Pos.CENTER);
        //END OF RESULT


        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if(loginField.getText().equals(String.valueOf(randomNumber))){
                    loginPane.getChildren().clear();
                    TextField nickField = new TextField();
                    nickField.setMinSize(200,35);
                    nickField.setMaxSize(200,35);
                    nickField.setPromptText("Nickname");
                    nickField.setFocusTraversable(false);
                    nickField.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));

                    Button loginButton2 = new Button("Ok, GO!");
                    loginButton2.setStyle("-fx-background-color: DIMGRAY");
                    loginButton2.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
                    loginButton2.setMinWidth(200);
                    loginButton2.setMinHeight(35);

                    VBox vBox = new VBox(10);
                    vBox.getChildren().addAll(labelLogin, nickField, loginButton2);
                    vBox.setAlignment(Pos.CENTER);


                    loginPane.getChildren().addAll(loginImage, vBox);

                    loginButton2.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            clientNumber++;

                            try {
                                toServer.writeUTF(nickField.getText());

                                toServer.writeInt(clientNumber);

                                toServer.writeUTF("");

                            } catch (IOException e) {
                                System.out.println(e);
                            }

                            Label labelWait = new Label("You're in!");
                            labelWait.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 30));

                            Label labelWait2 = new Label("See your nickname on screen!");
                            labelWait2.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

                            Label labelError = new Label("");
                            labelError.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

                            Button buttonGo = new Button("Start");
                            buttonGo.setStyle("-fx-background-color: DIMGRAY; -fx-text-fill: white");
                            buttonGo.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
                            buttonGo.setMinWidth(200);
                            buttonGo.setMinHeight(35);

                            VBox vBoxWait = new VBox(10);
                            vBoxWait.getChildren().addAll(labelWait, labelWait2, buttonGo, labelError);
                            vBoxWait.setAlignment(Pos.CENTER);

                            loginPane.getChildren().clear();
                            loginPane.getChildren().addAll(loginImage, vBoxWait);


                            //BORDERPANE
                            BorderPane borderButton = new BorderPane();

                            red = forButtons("crimson");
                            blue = forButtons("royalblue");
                            orange = forButtons("orange");
                            green = forButtons("yellowgreen");
                            trueButton = forButtons("royalblue");
                            falseButton = forButtons("crimson");
                            goButton = forButtons("crimson");

                            //SET ID NAME FOR ALL BUTTONS
                            red.setId("RED");
                            blue.setId("BLUE");
                            orange.setId("ORANGE");
                            green.setId("GREEN");
                            trueButton.setId("TRUE");
                            falseButton.setId("FALSE");

                            //Triangle
                            Polygon polygon = new Polygon();
                            polygon.getPoints().addAll(new Double[]{25.0, 0.0, 0.0, 60.0, 50.0, 60.0});
                            polygon.setFill(Color.WHITE);
                            red.setGraphic(polygon);
                            falseButton.setGraphic(polygon);
                            goButton.setGraphic(polygon);
                            //Circle
                            Circle circle = new Circle(30);
                            circle.setFill(Color.WHITE);
                            orange.setGraphic(circle);

                            //Square
                            Rectangle rectangle1 = new Rectangle(50,50,50,50);
                            rectangle1.setFill(Color.WHITE);
                            rectangle1.setRotate(45);
                            blue.setGraphic(rectangle1);
                            trueButton.setGraphic(rectangle1);

                            //Square2
                            Rectangle rectangle2 = new Rectangle(50,50,50,50);
                            rectangle2.setFill(Color.WHITE);
                            green.setGraphic(rectangle2);


                            Label labelPin = new Label("PIN: " + randomNumber);
                            labelPin.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
                            labelPin.setMaxWidth(700);
                            labelPin.setMinHeight(70);

                            Label labelScore = new Label("\t\t\t\t" + nickField.getText());
                            labelScore.setFont(Font.font("Verdana", FontWeight.BOLD, 17));

                            Label labelName = new Label("\t\t\t\t\t\t" + nextQuestion + " of " + numberOfQuestion, labelScore);
                            labelName.setContentDisplay(ContentDisplay.RIGHT);
                            labelName.setFont(Font.font("Verdana", FontWeight.BOLD, 17));
                            labelName.setMinHeight(70);

                            StackPane stackLabel = new StackPane();
                            stackLabel.getChildren().addAll(labelPin, labelName);

                            //BUTTON START
                            buttonGo.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {

                                    //WE shood start after new value from server
                                    try {
                                        start = fromServer.readBoolean();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    //When start become true you can also start
                                    if(start == false){
                                        labelError.setText("Wait for the Server!");

                                    }else{
                                        //GET Questions to ArrayList
                                        for (int i = 0; i < numberOfQuestion; i++) {
                                            questionsArray = questions.split(",");
                                        }
                                        questionsArrayList = new ArrayList<>(Arrays.asList(questionsArray));

                                        if(!questionsArrayList.get(0).contains("___")){

                                            vBox1.getChildren().clear();
                                            vBox2.getChildren().clear();
                                            vBox1.getChildren().addAll(red,orange);
                                            vBox2.getChildren().addAll(blue,green);
                                            hBox.getChildren().clear();
                                            hBox.getChildren().addAll(vBox1, vBox2);
                                            hBox.setAlignment(Pos.CENTER);

                                            borderButton.setTop(stackLabel);
                                            borderButton.setCenter(hBox);

                                            stage.setScene(new Scene(borderButton, width,height));
                                        }
                                        else {
                                            //TRUE FALSE
                                            vBox1.getChildren().clear();
                                            vBox2.getChildren().clear();
                                            vBox1.getChildren().addAll(falseButton);
                                            vBox2.getChildren().add(trueButton);
                                            hBox.getChildren().clear();
                                            hBox.getChildren().addAll(vBox1, vBox2);
                                            hBox.setAlignment(Pos.BOTTOM_CENTER);

                                            borderButton.setTop(stackLabel);
                                            borderButton.setCenter(hBox);

                                            stage.setScene(new Scene(borderButton, width,height));

                                        }

                                        //Buttons setOnAction
                                        //RedButton
                                        red.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                if(red.isFocused()){
                                                    if(nextQuestion != numberOfQuestion){
                                                        nextQuestion ++;
                                                        labelName.setText("\t\t\t\t\t\t" + nextQuestion + " of " + numberOfQuestion);

                                                        try {
                                                            toServer.writeUTF(nickField.getText());

                                                            toServer.writeInt(clientNumber);

                                                            toServer.writeUTF(red.getId());

                                                        } catch (IOException e) {}


                                                        //ADD FROM HERE
                                                        Timeline timeline = new Timeline(

                                                                new KeyFrame(Duration.ZERO, e -> {
                                                                    StackPane stackPaneLoading = new StackPane();

                                                                    FadeTransition ft = new FadeTransition(Duration.millis(3000), labelLoading2);
                                                                    ft.setFromValue(1.0);
                                                                    ft.setToValue(0.0);
                                                                    ft.setCycleCount(10);
                                                                    ft.setAutoReverse(true);
                                                                    ft.play();

                                                                    stackPaneLoading.getChildren().addAll(imageLoading, vBoxLoading, vBoxLoadingPic, vBoxLoadingPic2, vBoxShare4);
                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                }),

                                                                new KeyFrame(Duration.seconds(5), e -> {

                                                                    if(nextQuestion  >= numberOfQuestion){

                                                                        Timeline timeline2 = new Timeline(

                                                                                new KeyFrame(Duration.ZERO, e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();
                                                                                    stackPaneLoading.getChildren().addAll(imageLoadingClient);
                                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(2), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelNameResult, 1);

                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, labelNameResult);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));
                                                                                }),

                                                                                new KeyFrame(Duration.seconds(3), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelLoading3, 1000);
                                                                                    setAnimation(imageConfetti, 1000);
                                                                                    setAnimation(imageGold, 1);
                                                                                    setAnimation(labelQuote, 1);

                                                                                    labelNameResult.setText("\n\n\n\n\n\n"+nickField.getText());
                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(6), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    stackPaneLoading.getChildren().addAll(loginImage, vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                    imageGold.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                                                        @Override
                                                                                        public void handle(MouseEvent mouseEvent) {
                                                                                            stage.close();
                                                                                        }
                                                                                    });

                                                                                })
                                                                        );
                                                                        timeline2.play();

                                                                    }

                                                                    //SHould set this one in all BUTTONS
                                                                    if(!questionsArrayList.get(nextQuestion).contains("___")){

                                                                        //WRITE THIS ONE IN EACH BUTTON
                                                                        BorderPane borderButton = new BorderPane();

                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(red,orange);
                                                                        vBox2.getChildren().addAll(blue,green);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);

                                                                        stage.setScene(new Scene(borderButton, width,height));

                                                                    }

                                                                    else {
                                                                        BorderPane borderButton = new BorderPane();

                                                                        Polygon polygon2 = new Polygon();
                                                                        polygon2.getPoints().addAll(new Double[]{25.0, 0.0, 0.0, 60.0, 50.0, 60.0});
                                                                        polygon2.setFill(Color.WHITE);
                                                                        falseButton.setGraphic(polygon2);

                                                                        Rectangle rectangle2 = new Rectangle(50,50,50,50);
                                                                        rectangle2.setFill(Color.WHITE);
                                                                        rectangle2.setRotate(45);
                                                                        trueButton.setGraphic(rectangle2);

                                                                        //TRUE FALSE
                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(falseButton);
                                                                        vBox2.getChildren().add(trueButton);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.BOTTOM_CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);
                                                                        stage.setScene(new Scene(borderButton, width,height));


                                                                    }
                                                                })
                                                        );
                                                        timeline.play();


                                                    }

                                                }

                                            }
                                        });
                                        //blueButton
                                        blue.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                if(blue.isFocused()){
                                                    if(nextQuestion != numberOfQuestion){
                                                        nextQuestion ++;
                                                        labelName.setText("\t\t\t\t\t\t" + nextQuestion + " of " + numberOfQuestion);

                                                        try {
                                                            toServer.writeUTF(nickField.getText());

                                                            toServer.writeInt(clientNumber);

                                                            toServer.writeUTF(blue.getId());

                                                        } catch (IOException e) {}

                                                        Timeline timeline = new Timeline(

                                                                new KeyFrame(Duration.ZERO, e -> {
                                                                    StackPane stackPaneLoading = new StackPane();

                                                                    FadeTransition ft = new FadeTransition(Duration.millis(3000), labelLoading2);
                                                                    ft.setFromValue(1.0);
                                                                    ft.setToValue(0.0);
                                                                    ft.setCycleCount(10);
                                                                    ft.setAutoReverse(true);
                                                                    ft.play();

                                                                    stackPaneLoading.getChildren().addAll(imageLoading, vBoxLoading, vBoxLoadingPic, vBoxLoadingPic2, vBoxShare4);
                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                }),

                                                                new KeyFrame(Duration.seconds(5), e -> {

                                                                    if(nextQuestion  >= numberOfQuestion){

                                                                        Timeline timeline2 = new Timeline(

                                                                                new KeyFrame(Duration.ZERO, e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();
                                                                                    stackPaneLoading.getChildren().addAll(imageLoadingClient);
                                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(2), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelNameResult, 1);

                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, labelNameResult);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));
                                                                                }),

                                                                                new KeyFrame(Duration.seconds(3), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelLoading3, 1000);
                                                                                    setAnimation(imageConfetti, 1000);
                                                                                    setAnimation(imageGold, 1);
                                                                                    setAnimation(labelQuote, 1);

                                                                                    labelNameResult.setText("\n\n\n\n\n\n"+nickField.getText());
                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(6), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    stackPaneLoading.getChildren().addAll(loginImage, vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                    imageGold.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                                                        @Override
                                                                                        public void handle(MouseEvent mouseEvent) {
                                                                                            stage.close();
                                                                                        }
                                                                                    });

                                                                                })
                                                                        );
                                                                        timeline2.play();

                                                                    }


                                                                    //SHould set this one in all BUTTONS
                                                                    if(!questionsArrayList.get(nextQuestion).contains("___")){

                                                                        //WRITE THIS ONE IN EACH BUTTON
                                                                        BorderPane borderButton = new BorderPane();

                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(red,orange);
                                                                        vBox2.getChildren().addAll(blue,green);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);

                                                                        stage.setScene(new Scene(borderButton, width,height));

                                                                    }

                                                                    else {
                                                                        BorderPane borderButton = new BorderPane();

                                                                        Polygon polygon2 = new Polygon();
                                                                        polygon2.getPoints().addAll(new Double[]{25.0, 0.0, 0.0, 60.0, 50.0, 60.0});
                                                                        polygon2.setFill(Color.WHITE);
                                                                        falseButton.setGraphic(polygon2);

                                                                        Rectangle rectangle2 = new Rectangle(50,50,50,50);
                                                                        rectangle2.setFill(Color.WHITE);
                                                                        rectangle2.setRotate(45);
                                                                        trueButton.setGraphic(rectangle2);

                                                                        //TRUE FALSE
                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(falseButton);
                                                                        vBox2.getChildren().add(trueButton);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.BOTTOM_CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);
                                                                        stage.setScene(new Scene(borderButton, width,height));


                                                                    }
                                                                })
                                                        );
                                                        timeline.play();


                                                    }

                                                }

                                            }
                                        });
                                        //OrangeButton
                                        orange.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                if(orange.isFocused()){
                                                    if(nextQuestion != numberOfQuestion){
                                                        nextQuestion ++;
                                                        labelName.setText("\t\t\t\t\t\t" + nextQuestion + " of " + numberOfQuestion);
                                                        try {
                                                            toServer.writeUTF(nickField.getText());

                                                            toServer.writeInt(clientNumber);

                                                            toServer.writeUTF(orange.getId());

                                                        } catch (IOException e) {}

                                                        Timeline timeline = new Timeline(

                                                                new KeyFrame(Duration.ZERO, e -> {
                                                                    StackPane stackPaneLoading = new StackPane();

                                                                    FadeTransition ft = new FadeTransition(Duration.millis(3000), labelLoading2);
                                                                    ft.setFromValue(1.0);
                                                                    ft.setToValue(0.0);
                                                                    ft.setCycleCount(10);
                                                                    ft.setAutoReverse(true);
                                                                    ft.play();

                                                                    stackPaneLoading.getChildren().addAll(imageLoading, vBoxLoading, vBoxLoadingPic, vBoxLoadingPic2, vBoxShare4);
                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                }),

                                                                new KeyFrame(Duration.seconds(5), e -> {

                                                                    if(nextQuestion  >= numberOfQuestion){

                                                                        Timeline timeline2 = new Timeline(

                                                                                new KeyFrame(Duration.ZERO, e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();
                                                                                    stackPaneLoading.getChildren().addAll(imageLoadingClient);
                                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(2), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelNameResult, 1);

                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, labelNameResult);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));
                                                                                }),

                                                                                new KeyFrame(Duration.seconds(3), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelLoading3, 1000);
                                                                                    setAnimation(imageConfetti, 1000);
                                                                                    setAnimation(imageGold, 1);
                                                                                    setAnimation(labelQuote, 1);

                                                                                    labelNameResult.setText("\n\n\n\n\n\n"+nickField.getText());
                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(6), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    stackPaneLoading.getChildren().addAll(loginImage, vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                    imageGold.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                                                        @Override
                                                                                        public void handle(MouseEvent mouseEvent) {
                                                                                            stage.close();
                                                                                        }
                                                                                    });

                                                                                })
                                                                        );
                                                                        timeline2.play();

                                                                    }


                                                                    //SHould set this one in all BUTTONS
                                                                    if(!questionsArrayList.get(nextQuestion).contains("___")){

                                                                        //WRITE THIS ONE IN EACH BUTTON
                                                                        BorderPane borderButton = new BorderPane();

                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(red,orange);
                                                                        vBox2.getChildren().addAll(blue,green);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);

                                                                        stage.setScene(new Scene(borderButton, width,height));

                                                                    }

                                                                    else {
                                                                        BorderPane borderButton = new BorderPane();

                                                                        Polygon polygon2 = new Polygon();
                                                                        polygon2.getPoints().addAll(new Double[]{25.0, 0.0, 0.0, 60.0, 50.0, 60.0});
                                                                        polygon2.setFill(Color.WHITE);
                                                                        falseButton.setGraphic(polygon2);

                                                                        Rectangle rectangle2 = new Rectangle(50,50,50,50);
                                                                        rectangle2.setFill(Color.WHITE);
                                                                        rectangle2.setRotate(45);
                                                                        trueButton.setGraphic(rectangle2);

                                                                        //TRUE FALSE
                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(falseButton);
                                                                        vBox2.getChildren().add(trueButton);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.BOTTOM_CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);
                                                                        stage.setScene(new Scene(borderButton, width,height));


                                                                    }
                                                                })
                                                        );
                                                        timeline.play();


                                                    }

                                                }

                                            }
                                        });
                                        //GreenButton
                                        green.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                if(green.isFocused()){
                                                    if(nextQuestion != numberOfQuestion){
                                                        nextQuestion ++;
                                                        labelName.setText("\t\t\t\t\t\t" + nextQuestion + " of " + numberOfQuestion);


                                                        try {
                                                            toServer.writeUTF(nickField.getText());

                                                            toServer.writeInt(clientNumber);

                                                            toServer.writeUTF(green.getId());


                                                        } catch (IOException e) {}

                                                        Timeline timeline = new Timeline(

                                                                new KeyFrame(Duration.ZERO, e -> {
                                                                    StackPane stackPaneLoading = new StackPane();

                                                                    FadeTransition ft = new FadeTransition(Duration.millis(3000), labelLoading2);
                                                                    ft.setFromValue(1.0);
                                                                    ft.setToValue(0.0);
                                                                    ft.setCycleCount(10);
                                                                    ft.setAutoReverse(true);
                                                                    ft.play();

                                                                    stackPaneLoading.getChildren().addAll(imageLoading, vBoxLoading, vBoxLoadingPic, vBoxLoadingPic2, vBoxShare4);
                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                }),

                                                                new KeyFrame(Duration.seconds(5), e -> {

                                                                    if(nextQuestion  >= numberOfQuestion){

                                                                        Timeline timeline2 = new Timeline(

                                                                                new KeyFrame(Duration.ZERO, e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();
                                                                                    stackPaneLoading.getChildren().addAll(imageLoadingClient);
                                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(2), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelNameResult, 1);

                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, labelNameResult);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));
                                                                                }),

                                                                                new KeyFrame(Duration.seconds(3), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelLoading3, 1000);
                                                                                    setAnimation(imageConfetti, 1000);
                                                                                    setAnimation(imageGold, 1);
                                                                                    setAnimation(labelQuote, 1);

                                                                                    labelNameResult.setText("\n\n\n\n\n\n"+nickField.getText());
                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(6), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    stackPaneLoading.getChildren().addAll(loginImage, vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                    imageGold.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                                                        @Override
                                                                                        public void handle(MouseEvent mouseEvent) {
                                                                                            stage.close();
                                                                                        }
                                                                                    });

                                                                                })
                                                                        );
                                                                        timeline2.play();

                                                                    }


                                                                    //SHould set this one in all BUTTONS
                                                                    if(!questionsArrayList.get(nextQuestion).contains("___")){

                                                                        //WRITE THIS ONE IN EACH BUTTON
                                                                        BorderPane borderButton = new BorderPane();

                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(red,orange);
                                                                        vBox2.getChildren().addAll(blue,green);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);

                                                                        stage.setScene(new Scene(borderButton, width,height));

                                                                    }

                                                                    else {
                                                                        BorderPane borderButton = new BorderPane();

                                                                        Polygon polygon2 = new Polygon();
                                                                        polygon2.getPoints().addAll(new Double[]{25.0, 0.0, 0.0, 60.0, 50.0, 60.0});
                                                                        polygon2.setFill(Color.WHITE);
                                                                        falseButton.setGraphic(polygon2);

                                                                        Rectangle rectangle2 = new Rectangle(50,50,50,50);
                                                                        rectangle2.setFill(Color.WHITE);
                                                                        rectangle2.setRotate(45);
                                                                        trueButton.setGraphic(rectangle2);

                                                                        //TRUE FALSE
                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(falseButton);
                                                                        vBox2.getChildren().add(trueButton);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.BOTTOM_CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);
                                                                        stage.setScene(new Scene(borderButton, width,height));


                                                                    }
                                                                })
                                                        );
                                                        timeline.play();


                                                    }

                                                }

                                            }
                                        });
                                        //TrueButton
                                        trueButton.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                if(trueButton.isFocused()){
                                                    if(nextQuestion != numberOfQuestion){
                                                        nextQuestion ++;
                                                        labelName.setText("\t\t\t\t\t\t" + nextQuestion + " of " + numberOfQuestion);

                                                        try {
                                                            toServer.writeUTF(nickField.getText());

                                                            toServer.writeInt(clientNumber);

                                                            toServer.writeUTF(trueButton.getId());


                                                        } catch (IOException e) {}

                                                        Timeline timeline = new Timeline(

                                                                new KeyFrame(Duration.ZERO, e -> {
                                                                    StackPane stackPaneLoading = new StackPane();

                                                                    FadeTransition ft = new FadeTransition(Duration.millis(3000), labelLoading2);
                                                                    ft.setFromValue(1.0);
                                                                    ft.setToValue(0.0);
                                                                    ft.setCycleCount(10);
                                                                    ft.setAutoReverse(true);
                                                                    ft.play();

                                                                    stackPaneLoading.getChildren().addAll(imageLoading, vBoxLoading, vBoxLoadingPic, vBoxLoadingPic2, vBoxShare4);
                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                }),

                                                                new KeyFrame(Duration.seconds(5), e -> {

                                                                    if(nextQuestion  >= numberOfQuestion){

                                                                        Timeline timeline2 = new Timeline(

                                                                                new KeyFrame(Duration.ZERO, e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();
                                                                                    stackPaneLoading.getChildren().addAll(imageLoadingClient);
                                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(2), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelNameResult, 1);

                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, labelNameResult);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));
                                                                                }),

                                                                                new KeyFrame(Duration.seconds(3), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelLoading3, 1000);
                                                                                    setAnimation(imageConfetti, 1000);
                                                                                    setAnimation(imageGold, 1);
                                                                                    setAnimation(labelQuote, 1);

                                                                                    labelNameResult.setText("\n\n\n\n\n\n"+nickField.getText());
                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(6), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    stackPaneLoading.getChildren().addAll(loginImage, vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                    imageGold.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                                                        @Override
                                                                                        public void handle(MouseEvent mouseEvent) {
                                                                                            stage.close();
                                                                                        }
                                                                                    });

                                                                                })
                                                                        );
                                                                        timeline2.play();

                                                                    }


                                                                    //SHould set this one in all BUTTONS
                                                                    if(!questionsArrayList.get(nextQuestion).contains("___")){

                                                                        //WRITE THIS ONE IN EACH BUTTON
                                                                        BorderPane borderButton = new BorderPane();

                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(red,orange);
                                                                        vBox2.getChildren().addAll(blue,green);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);

                                                                        stage.setScene(new Scene(borderButton, width,height));

                                                                    }

                                                                    else {
                                                                        BorderPane borderButton = new BorderPane();

                                                                        Polygon polygon2 = new Polygon();
                                                                        polygon2.getPoints().addAll(new Double[]{25.0, 0.0, 0.0, 60.0, 50.0, 60.0});
                                                                        polygon2.setFill(Color.WHITE);
                                                                        falseButton.setGraphic(polygon2);

                                                                        Rectangle rectangle2 = new Rectangle(50,50,50,50);
                                                                        rectangle2.setFill(Color.WHITE);
                                                                        rectangle2.setRotate(45);
                                                                        trueButton.setGraphic(rectangle2);

                                                                        //TRUE FALSE
                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(falseButton);
                                                                        vBox2.getChildren().add(trueButton);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.BOTTOM_CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);
                                                                        stage.setScene(new Scene(borderButton, width,height));


                                                                    }
                                                                })
                                                        );
                                                        timeline.play();


                                                    }

                                                }
                                            }
                                        });
                                        //FalseButton
                                        falseButton.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                if(falseButton.isFocused()){
                                                    if(nextQuestion != numberOfQuestion){
                                                        nextQuestion ++;
                                                        labelName.setText("\t\t\t\t\t\t" + nextQuestion + " of " + numberOfQuestion);

                                                        try {
                                                            toServer.writeUTF(nickField.getText());

                                                            toServer.writeInt(clientNumber);

                                                            toServer.writeUTF(falseButton.getId());


                                                        } catch (IOException e) {}


                                                        Timeline timeline = new Timeline(

                                                                new KeyFrame(Duration.ZERO, e -> {
                                                                    StackPane stackPaneLoading = new StackPane();

                                                                    FadeTransition ft = new FadeTransition(Duration.millis(3000), labelLoading2);
                                                                    ft.setFromValue(1.0);
                                                                    ft.setToValue(0.0);
                                                                    ft.setCycleCount(10);
                                                                    ft.setAutoReverse(true);
                                                                    ft.play();

                                                                    stackPaneLoading.getChildren().addAll(imageLoading, vBoxLoading, vBoxLoadingPic, vBoxLoadingPic2, vBoxShare4);
                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                }),

                                                                new KeyFrame(Duration.seconds(5), e -> {

                                                                    if(nextQuestion  >= numberOfQuestion){

                                                                        Timeline timeline2 = new Timeline(

                                                                                new KeyFrame(Duration.ZERO, e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();
                                                                                    stackPaneLoading.getChildren().addAll(imageLoadingClient);
                                                                                    stage.setScene(new Scene(stackPaneLoading, 600, 600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(2), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelNameResult, 1);

                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, labelNameResult);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));
                                                                                }),

                                                                                new KeyFrame(Duration.seconds(3), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    setAnimation(labelLoading3, 1000);
                                                                                    setAnimation(imageConfetti, 1000);
                                                                                    setAnimation(imageGold, 1);
                                                                                    setAnimation(labelQuote, 1);

                                                                                    labelNameResult.setText("\n\n\n\n\n\n"+nickField.getText());
                                                                                    stackPaneLoading.getChildren().addAll(vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                }),

                                                                                new KeyFrame(Duration.seconds(6), e2 -> {
                                                                                    StackPane stackPaneLoading = new StackPane();

                                                                                    stackPaneLoading.getChildren().addAll(loginImage, vBoxLast, vBoxLoadingPic3, imageConfetti, labelNameResult, vBoxQuote, imageGold);

                                                                                    stage.setScene(new Scene(stackPaneLoading, 600,600));

                                                                                    imageGold.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                                                        @Override
                                                                                        public void handle(MouseEvent mouseEvent) {
                                                                                            stage.close();
                                                                                        }
                                                                                    });

                                                                                })
                                                                        );
                                                                        timeline2.play();

                                                                    }


                                                                    //SHould set this one in all BUTTONS
                                                                    if(!questionsArrayList.get(nextQuestion).contains("___")){

                                                                        //WRITE THIS ONE IN EACH BUTTON
                                                                        BorderPane borderButton = new BorderPane();

                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(red,orange);
                                                                        vBox2.getChildren().addAll(blue,green);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);

                                                                        stage.setScene(new Scene(borderButton, width,height));

                                                                    }

                                                                    else {
                                                                        BorderPane borderButton = new BorderPane();

                                                                        Polygon polygon2 = new Polygon();
                                                                        polygon2.getPoints().addAll(new Double[]{25.0, 0.0, 0.0, 60.0, 50.0, 60.0});
                                                                        polygon2.setFill(Color.WHITE);
                                                                        falseButton.setGraphic(polygon2);

                                                                        Rectangle rectangle2 = new Rectangle(50,50,50,50);
                                                                        rectangle2.setFill(Color.WHITE);
                                                                        rectangle2.setRotate(45);
                                                                        trueButton.setGraphic(rectangle2);

                                                                        //TRUE FALSE
                                                                        vBox1.getChildren().clear();
                                                                        vBox2.getChildren().clear();
                                                                        vBox1.getChildren().addAll(falseButton);
                                                                        vBox2.getChildren().add(trueButton);
                                                                        hBox.getChildren().clear();
                                                                        hBox.getChildren().addAll(vBox1, vBox2);
                                                                        hBox.setAlignment(Pos.BOTTOM_CENTER);

                                                                        borderButton.setTop(stackLabel);
                                                                        borderButton.setCenter(hBox);
                                                                        stage.setScene(new Scene(borderButton, width,height));


                                                                    }
                                                                })
                                                        );
                                                        timeline.play();


                                                    }

                                                }

                                            }
                                        });

                                    }
                                }
                            });
                        }
                    });
                }

                else{
                    loginField.setPromptText("PIN not found!");
                    loginField.setStyle("-fx-text-box-border: red; ");

                    TranslateTransition tr = new TranslateTransition(Duration.millis(90), loginField);
                    tr.setFromX(0f);
                    tr.setByX(10f);
                    tr.setCycleCount(4);
                    tr.setAutoReverse(true);
                    tr.playFromStart();
                }
            }

        });

        try {
            socket = new Socket("localhost", 777);

            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());

            objOut = new ObjectOutputStream(socket.getOutputStream());
            objIn = new ObjectInputStream(socket.getInputStream());


        }catch (IOException ex) {
            System.out.println(ex);
        }

        try {
            //NickNames
            toServer.writeUTF("");
            toServer.flush();
            //clientNumbers
            toServer.writeInt(clientNumber);
            toServer.flush();
            //Answers
            toServer.writeUTF("");

            randomNumber = fromServer.readInt();

            start = fromServer.readBoolean();

            numberOfQuestion = fromServer.readInt();

            questions = fromServer.readUTF();


        } catch (IOException e) {
            System.out.println(e);
        }

        stage.setScene(new Scene(loginPane, width, height));
        stage.show();
    }

    public static Button forButtons(String c){
        Button b = new Button();
        b.setStyle("-fx-background-color: " + c);
        b.setMinWidth(280);
        b.setMinHeight(250);
        b.setTextFill(Color.WHITE);
        b.setFont(Font.font("monospace",FontWeight.BOLD,18));

        return b;
    }

    public static FadeTransition setAnimation(Node node, int a){
        FadeTransition ft = new FadeTransition(Duration.millis(3000), node);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.setCycleCount(a);
        ft.setAutoReverse(true);
        ft.play();

        return ft;
    }
}
