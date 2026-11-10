package com.rbctcs.guru99.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Explicit waits instead of the old Thread.sleep(5000) after every page load. */
public abstract class BasePage {

    public static final String BASE = "https://demo.guru99.com/telecom/";

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    protected WebElement visible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected void type(By by, String text) {
        WebElement e = visible(by);
        e.clear();
        e.sendKeys(text);
    }

    /** Click; if an overlay (ad) intercepts it, click through JavaScript. */
    protected void click(By by) {
        WebElement e = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'})", e);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(e)).click();
        } catch (ElementClickInterceptedException blocked) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click()", e);
        }
    }

    public String bodyText() {
        return driver.findElement(By.tagName("body")).getText();
    }

    /** Waits until the page body contains the text (case-insensitive). */
    public boolean waitForText(String text) {
        try {
            wait.until(d -> bodyText().toLowerCase().contains(text.toLowerCase()));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}
