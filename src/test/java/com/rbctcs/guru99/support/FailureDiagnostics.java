package com.rbctcs.guru99.support;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * On failure: screenshot + page HTML in target/failures/, and the URL, title and visible text in the log,
 * so a red run on CI explains itself.
 */
public class FailureDiagnostics implements TestWatcher {

    private final Supplier<WebDriver> driver;

    public FailureDiagnostics(Supplier<WebDriver> driver) {
        this.driver = driver;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        WebDriver d = driver.get();
        if (d == null) return;
        String name = context.getRequiredTestMethod().getName();
        try {
            Path dir = Files.createDirectories(Path.of("target", "failures"));
            Files.write(dir.resolve(name + ".png"), ((TakesScreenshot) d).getScreenshotAs(OutputType.BYTES));
            Files.writeString(dir.resolve(name + ".html"), d.getPageSource());
            String text = d.findElement(org.openqa.selenium.By.tagName("body")).getText();
            System.out.println("=== FAILED " + name + " @ " + d.getCurrentUrl() + " | title: " + d.getTitle());
            System.out.println(text.length() > 1500 ? text.substring(0, 1500) + " ..." : text);
        } catch (Exception e) {
            System.out.println("could not save diagnostics for " + name + ": " + e.getMessage());
        }
    }
}
