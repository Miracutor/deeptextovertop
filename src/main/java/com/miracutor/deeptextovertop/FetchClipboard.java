package com.miracutor.deeptextovertop;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
            System.out.println("[" + logTime.format(new Date()) + "] " + "Change: " + currentText);
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
            System.out.println("[" + logTime.format(new Date()) + "] " + "Clipboard content not supported.");
        }
    }));
    private volatile boolean exit = false;

    public FetchClipboard() {
        deepLTranslator = new DeepLTranslator();
    }

    private void translate(String text) {
        try {
            String currentTranslation = deepLTranslator.translate(text, "en", "ja");
            System.out.println("[" + logTime.format(new Date()) + "] " + "Translation: " + currentTranslation);
            Platform.runLater(() -> {
                MainApp.changeText(currentTranslation);
                MainApp.setError(false);
            });
        } catch (StaleElementReferenceException e) {
            System.out.println("[" + logTime.format(new Date()) + "] " + "StaleElementReferenceException happens.\nRetrying...");
            Platform.runLater(() -> {
                MainApp.errorDialog("Translation cannot be loaded.\nRetrying...");
                MainApp.setError(true);
            });
            currentText = "";
        } catch (TimeoutException e) {
            System.out.println("[" + logTime.format(new Date()) + "] " + "Timeout reached.");
            Platform.runLater(() -> MainApp.errorDialog("Timeout reached.\nPlease check your connections and try again."));
        } catch (IllegalStateException e) {
            System.out.println("[" + logTime.format(new Date()) + "] " + "Text is not valid: " + e.getMessage());
            Platform.runLater(() -> MainApp.errorDialog(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        repeatTask.setCycleCount(Timeline.INDEFINITE);
        repeatTask.play();

        System.out.println("[" + logTime.format(new Date()) + "] " + "Start hearing...");
        if (exit) {
            repeatTask.stop();
            DeepLTranslator.shutdown();
        }
    }

    public void stopProgram() {
        System.out.println("[" + logTime.format(new Date()) + "] " + "Program closed...");
        exit = true;
    }
}
