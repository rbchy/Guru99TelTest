package com.rbctcs.guru99.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public final class Browser {

    private Browser() {
    }

    public static WebDriver start() {
        ChromeOptions o = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", System.getenv().getOrDefault("CI", "false")))) {
            o.addArguments("--headless=new");
        }
        o.addArguments("--window-size=1366,1000", "--lang=en-US");
        // The demo site is full of Google ads; a full-screen "vignette" ad steals clicks and makes runs flaky.
        // Resolve the ad domains to nowhere so the page under test is what gets clicked.
        o.addArguments("--host-resolver-rules=MAP *.doubleclick.net 127.0.0.1, MAP *.googlesyndication.com 127.0.0.1, "
                + "MAP *.googleadservices.com 127.0.0.1, MAP *.adtrafficquality.google 127.0.0.1, "
                + "MAP *.google-analytics.com 127.0.0.1, MAP *.googletagmanager.com 127.0.0.1");
        WebDriver d = new ChromeDriver(o); // Selenium Manager picks the matching chromedriver
        d.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        return d;
    }
}
