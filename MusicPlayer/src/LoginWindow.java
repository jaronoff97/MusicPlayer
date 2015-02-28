

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

public class LoginWindow extends Application {


    public static String password = "";
    public static GridPane loginGrid = new GridPane();
    public static Scene loginScene = new Scene(loginGrid, 300, 275);
    public static Text loginTitle = new Text("Welcome");
    public static Label userName = new Label("User Name:");
    public static TextField userLoginBox = new TextField();
    public static PasswordField userPasswordBox = new PasswordField();
    public static Label pw = new Label("Password:");
    public static Button signIn = new Button("Sign in");
    public static HBox hbsignIn = new HBox(10);
    public static Button newName = new Button("New Account");
    public static HBox hNewName = new HBox(10);
    public static final Text loginTarget = new Text();
    public static String userLogin;
    public static String userPassword;
    public static boolean goodLogin = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(25, 25, 25, 25));
        loginTitle.setId("welcome-text");

        loginGrid.add(loginTitle, 0, 0, 2, 1);
        if (goodLogin == false) {
            loginScreen();

            System.out.println("LoginScreen");
        }


        primaryStage.setScene(loginScene);

        loginScene.getStylesheets().add(LoginWindow.class.getResource("Styling.css").toExternalForm());
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);
        primaryStage.show();
        System.out.println(primaryStage.getWidth() + " , " + primaryStage.getHeight());
        System.out.println(primaryStage.getX() + " , " + primaryStage.getY());
        paint(primaryStage);

    }
    public static void paint(Stage primaryStage) {
        if (goodLogin == true) {
            System.out.println("working");
        }
    }
    public static void loginScreen() {

        loginGrid.add(userName, 0, 1);
        loginGrid.add(userLoginBox, 1, 1);
        loginGrid.add(pw, 0, 2);
        loginGrid.add(userPasswordBox, 1, 2);
        hbsignIn.setAlignment(Pos.BOTTOM_RIGHT);
        hbsignIn.getChildren().add(signIn);
        loginGrid.add(hbsignIn, 1, 4);
        hNewName.setAlignment(Pos.BOTTOM_RIGHT);
        hNewName.getChildren().add(newName);
        loginGrid.add(hNewName, 1, 3);


        loginGrid.add(loginTarget, 1, 6);
        loginTarget.setId("loginTarget");
        signIn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                userLogin = makeString(userLoginBox.getCharacters());
                userPassword = makeString(userPasswordBox.getCharacters());
                if (checkLogin() == true) {
                    goodLogin = true;
                    loginTarget.setText("Good Login");
                    MusicPlayer mainPlayer = new MusicPlayer();
                    try {
						mainPlayer.start(new Stage());
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}


                }
                if (checkLogin() == false) {
                    loginTarget.setText("Bad Login");
                }

            }
        });
        signIn.getScene().getAccelerators().put(
            new KeyCodeCombination(KeyCode.ENTER),
        new Runnable() {
            @Override public void run() {
                signIn.fire();
            }
        }
        );
        newName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                userLogin = makeString(userLoginBox.getCharacters());
                userPassword = makeString(userPasswordBox.getCharacters());
                makeAccount();
            }
        });
    }
    @SuppressWarnings("finally")
	public static boolean checkLogin() {
        int i = 0;
        boolean bpass = false;
        boolean blogin = false;
        boolean toReturn = false;
        //System.out.println("In check login");
        try(BufferedReader br = new BufferedReader(new FileReader("login.txt"))) {

            //System.out.println("In check login try");
            for (String line; (line = br.readLine()) != null; ) {
                System.out.println(line + ", " + userLogin + ", " + userPassword);
                if (line.equals(userLogin) && i == 0) {
                    blogin = true;
                    //System.out.println("user worked");
                }
                if (line.equals(userPassword) && i == 1) {
                    bpass = true;
                    //System.out.println("password worked");
                }
                i++;
            }
            // line is not visible here.
        } catch (Exception e) {

        } finally {
            if (bpass && blogin) {
                //System.out.println("in finally");
                toReturn = true;
            }
            return (toReturn);
        }
    }
    public static void makeAccount() {

        String fileName = "login.txt";

        try {
            FileWriter filewriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(filewriter);
            bufferedWriter.write(userLogin);
            bufferedWriter.newLine();
            bufferedWriter.write(userPassword);
            System.out.println("Your file has been written");
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
        } finally {
        }
    }
    public static String makeString(CharSequence r) {
        final StringBuilder sb = new StringBuilder(r.length());
        sb.append(r);
        //System.out.println("String is: " + sb.toString());
        return sb.toString();
    }



}
/*
What does a music player need:
(Sections)
Playlists
Artists
Songs
Albums
Genres

(Design choices)
Flat
Circular
Skinable Via CSS styling (possible in javaFX)
Default is dark
Background: 00153B
Text: ABE7FF
Circles: 5B6880

(Features)
Secure Login
Extreme Playlist control



*/