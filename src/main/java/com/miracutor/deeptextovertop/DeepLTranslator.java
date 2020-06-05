package com.miracutor.deeptextovertop;

import com.google.common.net.UrlEscapers;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class DeepLTranslator {

    private static final Duration TIMEOUT = Duration.ofSeconds(30);
    private static ChromeOptions chromeOptions;
    private static WebDriver driver;

    DeepLTranslator() {
        Path chrome = Paths.get("chromedriver.exe");
        if (!chrome.toFile().exists()) {
            MainApp.errorDialog("'chromedriver.exe' is missing.", true);
        }
        System.setProperty("webdriver.chrome.driver", chrome.toAbsolutePath().toString());
        chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-extensions");
        driver = new ChromeDriver(chromeOptions);
    }

    public static void shutdown() {
        driver.close();
        driver.quit();
    }

    private void isValid(String text, String from, String to) throws IllegalStateException {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalStateException("Text is null or empty");
        } else if (from == null || to == null) {
            throw new IllegalStateException("Language is null");
        } else if (text.length() > 5000) {
            throw new IllegalStateException("Text length is limited to 5000 characters");
        }
    }

    private String getTranslation(String text, String from, String to) {
        if (driver == null) {
            driver = new ChromeDriver(chromeOptions);
            System.out.println("New Chrome instance is created!");
        }

        String url = "https://www.deepl.com/translator#"
                + from
                + "/"
                + to
                + "/"
                + UrlEscapers.urlFragmentEscaper().escape(text);

        driver.get(url);
        String result;

        do {
            new WebDriverWait(driver, TIMEOUT.getSeconds()).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            WebElement webElement = new WebDriverWait(driver, TIMEOUT.getSeconds()).until(ExpectedConditions.refreshed(new ExpectedCondition<>() {
                @NullableDecl
                @Override
                public WebElement apply(@NullableDecl WebDriver input) {
                    WebElement element = driver.findElement(By.className("lmt__translations_as_text__text_btn"));
                    String result_chk = element.getAttribute("textContent");
                    if (result_chk == null || result_chk.isEmpty()) {
                        return null;
                    } else
                        return element;
                }
            }));

            result = webElement.getAttribute("textContent");

        } while (result == null || result.isEmpty());

        return result.trim()
                .replaceAll("(?:\r?\n)+", " ")
                .replaceAll("\\s{2,}", " ");
    }

    public String translate(String text, String from, String to) {
        isValid(text, from, to);

        return getTranslation(text, from, to);
    }
}
