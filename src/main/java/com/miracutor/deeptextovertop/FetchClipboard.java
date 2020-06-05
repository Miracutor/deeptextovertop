package com.miracutor.deeptextovertop;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Duration;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FetchClipboard extends Thread {
    private final Clipboard clipboard = Clipboard.getSystemClipboard();
    private final DeepLTranslator deepLTranslator;
    DateFormat logTime = new SimpleDateFormat("kk:hh:ss");
    private String currentText = "";
    private boolean initialStart = false;
    Timeline repeatTask = new Timeline(new KeyFrame(Duration.millis(200), event -> {
        if (!initialStart) {
            clipboard.clear();
            currentText = "プログラムの初期化... OK";
            ClipboardContent content = new ClipboardContent();
            content.putString(currentText);
            clipboard.setContent(content);
            translate(currentText);
            initialStart = true;
        } else if (clipboard.hasString()) {
            String newText = clipboard.getString();
            if (!currentText.equals(newText) && initialStart) {
                System.out.println("[" + logTime.format(new Date()) + "] " + "Change: " + newText);
                currentText = newText;
                translate(currentText);
            }
        } else {
            System.out.println("Clipboard content not supported.");
        }
    }));
    private volatile boolean exit = false;

    public FetchClipboard() {
        deepLTranslator = new DeepLTranslator();
    }

    private void translate(String text) {
        try {
            String currentTranslation = deepLTranslator.translate(text, "en", "ja");
            MainApp.changeText(currentTranslation);
            System.out.println("[" + logTime.format(new Date()) + "] " + "Translation: " + currentTranslation);
            MainApp.setError(false);
        } catch (StaleElementReferenceException e) {
            System.out.println("StaleElementReferenceException happens.\nRetrying...");
            MainApp.errorDialog("Translation cannot be loaded.\nRetrying...");
            MainApp.setError(true);
            currentText = "";
        } catch (TimeoutException e) {
            System.out.println("Timeout reached.");
            MainApp.errorDialog("Timeout reached.\nPlease check your connections and try again.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        repeatTask.setCycleCount(Timeline.INDEFINITE);
        repeatTask.play();

        System.out.println("Start hearing...");
        if (exit) {
            repeatTask.stop();
            DeepLTranslator.shutdown();
        }
    }

    public void stopProgram() {
        System.out.println("Program closed...");
        exit = true;
    }
}
