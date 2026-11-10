package com.rbctcs.guru99.pages;

import com.rbctcs.guru99.support.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddCustomerPage extends BasePage {

    private static final By DONE     = By.xpath("//input[@id='done']/following-sibling::label | //label[@for='done']");
    private static final By FIRST    = By.id("fname");
    private static final By LAST     = By.id("lname");
    private static final By EMAIL    = By.id("email");
    private static final By ADDRESS  = By.name("addr");
    private static final By PHONE    = By.id("telephoneno");
    private static final By SUBMIT   = By.name("submit");

    public AddCustomerPage(WebDriver driver) {
        super(driver);
    }

    public AddCustomerPage open() {
        driver.get(BASE + "addcustomer.php");
        visible(FIRST);
        return this;
    }

    public AddCustomerPage fill(TestData.Customer c) {
        click(DONE); // background check "Done"
        type(FIRST, c.firstName());
        type(LAST, c.lastName());
        type(EMAIL, c.email());
        type(ADDRESS, c.address());
        type(PHONE, c.phone());
        return this;
    }

    public void submit() {
        click(SUBMIT);
    }

    /** After a successful submit the confirmation page shows the new customer id. */
    public String customerId() {
        wait.until(d -> TestData.extractCustomerId(customerIdText()) != null);
        return TestData.extractCustomerId(customerIdText());
    }

    private String customerIdText() {
        var cells = driver.findElements(By.xpath("//td[contains(normalize-space(),'Customer ID')]/following-sibling::td"));
        return cells.isEmpty() ? null : cells.get(0).getText();
    }

    /** True while the browser is still on the form (submit refused). */
    public boolean stillOnForm() {
        return driver.getCurrentUrl().contains("addcustomer.php") && !driver.findElements(FIRST).isEmpty();
    }

    public void typeFirstName(String name) {
        type(FIRST, name);
    }
}
