package com.rbctcs.guru99.pages;

import com.rbctcs.guru99.support.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddTariffPage extends BasePage {

    public AddTariffPage(WebDriver driver) {
        super(driver);
    }

    public AddTariffPage open() {
        driver.get(BASE + "addtariffplans.php");
        visible(By.id("rental1"));
        return this;
    }

    public void add(TestData.Tariff t) {
        type(By.id("rental1"), String.valueOf(t.monthlyRental()));
        type(By.id("local_minutes"), String.valueOf(t.localMinutes()));
        type(By.id("inter_minutes"), String.valueOf(t.internationalMinutes()));
        type(By.id("sms_pack"), String.valueOf(t.smsPack()));
        type(By.id("minutes_charges"), String.valueOf(t.localPerMinute()));
        type(By.id("inter_charges"), String.valueOf(t.internationalPerMinute()));
        type(By.id("sms_charges"), String.valueOf(t.smsCharge()));
        click(By.name("submit"));
    }
}
