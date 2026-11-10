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
        // The site answers an invalid form with a JavaScript alert ("please fill all fields").
        // Turn that into a clear failure that names the fields the page complained about.
        try {
            var alert = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(2))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            alert.accept();
            throw new AssertionError("Form refused: '" + text + "'. Field messages: " + validationMessages()
                    + " | values: " + fieldValues());
        } catch (org.openqa.selenium.TimeoutException noAlert) {
            // submitted
        }
    }

    /** Inline messages the page shows next to invalid fields. */
    public java.util.List<String> validationMessages() {
        return driver.findElements(By.cssSelector("label[id^='message'], span[id^='message'], [id^='message']"))
                .stream().map(e -> e.getAttribute("id") + "=" + e.getText().trim())
                .filter(t -> !t.endsWith("=")).toList();
    }

    private String fieldValues() {
        StringBuilder sb = new StringBuilder();
        for (var e : driver.findElements(By.cssSelector("form input, form textarea"))) {
            sb.append(e.getAttribute("name")).append('=').append(e.getAttribute("value"))
              .append(e.isSelected() ? "(checked)" : "").append("; ");
        }
        return sb.toString();
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
