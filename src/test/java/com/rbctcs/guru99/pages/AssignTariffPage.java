package com.rbctcs.guru99.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Assigning a plan is two steps: enter the customer id, then pick a plan and confirm.
 * The old test stopped after the first step and printed "assigned successfully" only if
 * a "Congratulation" heading happened to be there - the plan was never actually chosen.
 */
public class AssignTariffPage extends BasePage {

    public AssignTariffPage(WebDriver driver) {
        super(driver);
    }

    public AssignTariffPage open() {
        driver.get(BASE + "assigntariffplantocustomer.php");
        visible(By.id("customer_id"));
        return this;
    }

    public void enterCustomer(String customerId) {
        type(By.id("customer_id"), customerId);
        click(By.name("submit"));
    }

    /** Number of tariff plans offered (one radio button per plan). */
    public int plansOffered() {
        wait.until(d -> !d.findElements(By.cssSelector("input[type='radio']")).isEmpty()
                || bodyText().toLowerCase().contains("please input"));
        return driver.findElements(By.cssSelector("input[type='radio']")).size();
    }

    /** Picks the newest plan (last radio) and confirms. */
    public void chooseNewestPlanAndConfirm() {
        var radios = driver.findElements(By.cssSelector("input[type='radio']"));
        var radio = radios.get(radios.size() - 1);
        String id = radio.getAttribute("id");
        var labels = id == null || id.isEmpty() ? java.util.List.<org.openqa.selenium.WebElement>of()
                : driver.findElements(By.cssSelector("label[for='" + id + "']"));
        if (!labels.isEmpty() && labels.get(0).isDisplayed()) {
            labels.get(0).click(); // the site styles radios as labels; the input itself is hidden
        } else {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click()", radio);
        }
        click(By.xpath("(//input[@type='submit'])[last()]"));
    }
}
