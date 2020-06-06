package com.miracutor.deeptextovertop;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX App
 */
public class MainApp extends Application {

    private static final String btn_style_normal = "-fx-border-color: gray; -fx-border-style: solid; -fx-text-fill: white;-fx-background-color: rgb(0,0,0,0.4)";
    private static final String btn_style_hover = "-fx-border-color: gray; -fx-border-style: solid; -fx-text-fill: white;-fx-background-color: rgb(0,0,0,1.0)";
    private static final Image icon = new Image(MainApp.class.getResourceAsStream("/icon.png"));
    public static Text error;
    private static Stage primaryStage;
    private static AnchorPane mainLayout;
    private static String currentTranslation = "First load may take longer time.\nProgram initialization...";
    private static FetchClipboard trans;
    private static Border border_window;
    private static List<String> translation_log;
    private double xOffset = 0;
    private double yOffset = 0;

    public static void main(String[] args) {
        launch();
    }

    public static void changeText(String newText) {
        currentTranslation = newText;
        Text currentText = (Text) mainLayout.getChildren().get(1);
        currentText.setText(currentTranslation);
        translation_log.add(newText);
    }

    public void setUpUI() {
        mainLayout.setBackground(null);
        mainLayout.setBorder(border_window);
        mainLayout.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        mainLayout.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
        Button exitBtn = new Button("X");
        exitBtn.setOnMouseClicked((event -> {
            primaryStage.close();
            trans.stopProgram();
        }));
        exitBtn.setStyle(btn_style_normal);
        exitBtn.setOnMouseEntered(mouseEvent -> exitBtn.setStyle(btn_style_hover));
        exitBtn.setOnMouseExited(mouseEvent -> exitBtn.setStyle(btn_style_normal));
        AnchorPane.setTopAnchor(exitBtn, 0.0);
        AnchorPane.setLeftAnchor(exitBtn, 560.0);
        AnchorPane.setRightAnchor(exitBtn, 0.0);
        AnchorPane.setBottomAnchor(exitBtn, 120.0);
        mainLayout.getChildren().add(exitBtn);

        Text mainText = new Text();
        mainText.setText(currentTranslation);
        mainText.setFont(new Font(18));
        mainText.setFill(Color.WHITE);
        mainText.setWrappingWidth(520.0);
        mainText.setTextAlignment(TextAlignment.JUSTIFY);
        AnchorPane.setTopAnchor(mainText, 10.0);
        AnchorPane.setLeftAnchor(mainText, 15.0);
        AnchorPane.setRightAnchor(mainText, 40.0);
        AnchorPane.setBottomAnchor(mainText, 0.0);
        mainLayout.getChildren().add(mainText);

        error = new Text();
        error.setFill(Color.RED);
        AnchorPane.setTopAnchor(error, 120.0);
        AnchorPane.setLeftAnchor(error, 560.0);
        AnchorPane.setRightAnchor(error, 0.0);
        AnchorPane.setBottomAnchor(error, 0.0);
        mainLayout.getChildren().add(error);
    }

    public static void setError(boolean e) {
        if (e)
            error.setText("ERROR");
        else
            error.setText("");
    }

    public static void errorDialog(String e) {
        errorDialog(e, false);
    }

    /*public void loadFilter(){
        File file = new File("filter.json");
        JSONObject json = new JSONObject(file.toString());
    }

    public static String filter(String text){
        return text;
    }*/

    public static void errorDialog(String e, boolean severe) {
        Stage errorStage = new Stage();
        errorStage.setTitle("Error");
        //primaryStage.setAlwaysOnTop(false);
        errorStage.setAlwaysOnTop(true);
        BorderPane errorLayout = new BorderPane();

        Text message = new Text("\n" + e);
        message.setFill(Color.WHITE);
        message.setTextAlignment(TextAlignment.JUSTIFY);
        errorLayout.setCenter(message);
        errorLayout.setBackground(null);
        errorLayout.setBorder(border_window);

        AnchorPane buttomBtn = new AnchorPane();
        Button exitBtn = new Button();
        AnchorPane.setRightAnchor(exitBtn, 125.0);
        AnchorPane.setLeftAnchor(exitBtn, 125.0);
        AnchorPane.setTopAnchor(exitBtn, 35.0);
        AnchorPane.setBottomAnchor(exitBtn, 10.0);
        buttomBtn.getChildren().add(exitBtn);
        exitBtn.setPrefSize(50.0, 20.0);
        exitBtn.setText("OK");
        exitBtn.setStyle(btn_style_normal);
        exitBtn.setOnMouseEntered(mouseEvent -> exitBtn.setStyle(btn_style_hover));
        exitBtn.setOnMouseExited(mouseEvent -> exitBtn.setStyle(btn_style_normal));

        if (severe) {//If severe error, when closing window will end program
            exitBtn.setOnMouseClicked((event -> {
                errorStage.close();
                primaryStage.close();
                trans.stopProgram();
            }));
            errorStage.setOnCloseRequest(windowEvent -> {
                errorStage.close();
                primaryStage.close();
                trans.stopProgram();
            });
        } else {
            exitBtn.setOnMouseClicked((event -> errorStage.close()));
        }
        errorLayout.setBottom(buttomBtn);

        Scene scene = new Scene(errorLayout, 300, 100);
        scene.setFill(Color.rgb(0, 0, 0, 0.8));
        errorStage.setScene(scene);
        errorStage.getIcons().add(icon);
        errorStage.initModality(Modality.APPLICATION_MODAL);
        errorStage.initStyle(StageStyle.TRANSPARENT);
        errorStage.show();
    }

    public static void logDialog() {
        Stage logStage = new Stage();
        logStage.setTitle("Translation Log");
        ListView<String> logLayout = new ListView<>();
        logLayout.getItems().setAll(translation_log);

        Scene scene = new Scene(logLayout, 100, 300);
        scene.setFill(Color.rgb(0, 0, 0, 0.8));
        logStage.setScene(scene);
        logStage.getIcons().add(icon);
        logStage.initStyle(StageStyle.TRANSPARENT);
        logStage.show();
    }

    @Override
    public void start(Stage stage) {
        mainLayout = new AnchorPane();
        border_window = new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, null, null));
        setUpUI();
        Scene scene = new Scene(mainLayout, 600, 150);
        scene.setFill(Color.rgb(0, 0, 0, 0.4));
        MainApp.primaryStage = stage;
        MainApp.primaryStage.setTitle("Deep Text Overtop");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(icon);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
        translation_log = new ArrayList<>();
        trans = new FetchClipboard();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                trans.start();
                return null;
            }
        };
        new Thread(task).start();
    }

}
