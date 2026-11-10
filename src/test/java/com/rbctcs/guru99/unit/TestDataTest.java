package com.rbctcs.guru99.unit;

import com.rbctcs.guru99.support.TestData;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TestDataTest {

    @RepeatedTest(20)
    void customersMeetTheFormRules() {
        TestData.Customer c = TestData.customer();
        assertTrue(c.firstName().matches("[A-Za-z]+"), "letters only: " + c.firstName());
        assertTrue(c.lastName().matches("[A-Za-z]+"), "letters only: " + c.lastName());
        assertTrue(c.email().matches("[a-z0-9.]+@example\\.com"), c.email());
        assertTrue(c.phone().matches("\\d{10}"), "10 digits: " + c.phone());
        assertFalse(c.address().isBlank());
    }

    @Test
    void phoneNumbersAreFromTheFictional555Range() {
        assertTrue(TestData.customer().phone().startsWith("20155501"));
    }

    @Test
    void everyTariffValueIsAPositiveWholeNumber() {
        TestData.Tariff t = TestData.standardTariff();
        for (int v : new int[]{t.monthlyRental(), t.localMinutes(), t.internationalMinutes(), t.smsPack(),
                t.localPerMinute(), t.internationalPerMinute(), t.smsCharge()}) {
            assertTrue(v > 0, String.valueOf(v));
        }
    }

    @Test
    void customerIdIsFoundInConfirmationText() {
        assertEquals("843219", TestData.extractCustomerId("Customer ID 843219"));
        assertEquals("843219", TestData.extractCustomerId("  843219 "));
        assertEquals("123456", TestData.extractCustomerId("Please Note Down Your CustomerID 123456"));
        assertNull(TestData.extractCustomerId("Customer ID"));
        assertNull(TestData.extractCustomerId(null));
        assertNull(TestData.extractCustomerId("id 12"), "too short to be a customer id");
    }

    /** Guard rail: no real personal data in the tests again (the old file had a home address and mobile number). */
    @Test
    void noRealPersonalDataInTheSource() throws Exception {
        try (Stream<Path> files = Files.walk(Path.of("src"))) {
            files.filter(p -> p.toString().endsWith(".java") && !p.endsWith("TestDataTest.java")).forEach(p -> {
                String s;
                try {
                    s = Files.readString(p);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                assertFalse(s.matches("(?s).*\\b267\\d{7}\\b.*"), p + " contains a real-looking 267 phone number");
                assertFalse(s.contains("Jefferson St"), p + " contains a real street address");
            });
        }
    }
}
