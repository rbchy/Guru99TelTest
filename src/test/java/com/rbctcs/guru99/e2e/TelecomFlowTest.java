package com.rbctcs.guru99.e2e;

import com.rbctcs.guru99.pages.AddCustomerPage;
import com.rbctcs.guru99.pages.AddTariffPage;
import com.rbctcs.guru99.pages.AssignTariffPage;
import com.rbctcs.guru99.support.Browser;
import com.rbctcs.guru99.support.FailureDiagnostics;
import com.rbctcs.guru99.support.TestData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * The business flow of the Guru99 telecom demo, in order:
 * add a customer -> add a tariff plan -> assign the plan to that customer.
 * Each step asserts its result; the old main() only printed messages and could not fail.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TelecomFlowTest {

    private WebDriver driver;
    private String customerId;

    @RegisterExtension
    final FailureDiagnostics diagnostics = new FailureDiagnostics(() -> driver);

    @BeforeAll
    void start() {
        driver = Browser.start();
    }

    @AfterAll
    void stop() {
        if (driver != null) driver.quit();
    }

    @Test
    @Order(1)
    @DisplayName("TC-01 Add customer: a numeric customer id is issued")
    void addCustomer() {
        AddCustomerPage page = new AddCustomerPage(driver).open();
        page.fill(TestData.customer()).submit();
        customerId = page.customerId();
        assertNotNull(customerId, "customer id on the confirmation page");
        assertTrue(customerId.matches("\\d{3,}"), "customer id should be numeric: " + customerId);
    }

    @Test
    @Order(2)
    @DisplayName("TC-02 Add tariff plan: confirmation is shown")
    void addTariff() {
        AddTariffPage page = new AddTariffPage(driver).open();
        page.add(TestData.standardTariff());
        assertTrue(page.waitForText("Congratulation"), "tariff plan confirmation expected");
    }

    @Test
    @Order(3)
    @DisplayName("TC-03 Assign the plan to the new customer")
    void assignTariff() {
        assumeTrue(customerId != null, "needs the customer from TC-01");
        AssignTariffPage page = new AssignTariffPage(driver).open();
        page.enterCustomer(customerId);
        assertTrue(page.plansOffered() > 0, "at least one tariff plan should be offered to the customer");
        page.chooseNewestPlanAndConfirm();
        assertTrue(page.waitForText("Congratulation"), "assignment confirmation expected");
    }

    @Test
    @Order(4)
    @DisplayName("TC-04 Negative: digits in the first name are refused")
    void digitsInNameRefused() {
        AddCustomerPage page = new AddCustomerPage(driver).open();
        TestData.Customer c = TestData.customer();
        page.fill(new TestData.Customer("Ayan123", c.lastName(), c.email(), c.address(), c.phone()));
        assertTrue(page.waitForText("Numbers are not allowed"), "inline validation message expected");
    }

    @Test
    @Order(5)
    @DisplayName("TC-05 Negative: an unknown customer id gets no plans")
    void unknownCustomer() {
        AssignTariffPage page = new AssignTariffPage(driver).open();
        page.enterCustomer("99999999");
        assertEquals(0, page.plansOffered(), "no plans for a customer that does not exist");
    }
}
