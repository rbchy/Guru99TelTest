package com.rbctcs.guru99.support;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Made-up customer and tariff data. The old test typed the author's real home address and mobile
 * number into a public demo site (and published them in the source); everything here is fictitious:
 * example.com e-mail, a 555-01xx phone number reserved for fiction, an invented street.
 */
public final class TestData {

    private static final String[] FIRST = {"Ayan", "Nila", "Rafi", "Tania", "Omar", "Mira", "Sami", "Lina"};
    private static final String[] LAST  = {"Sarker", "Hossain", "Roy", "Das", "Karim", "Paul", "Biswas", "Khan"};

    private TestData() {
    }

    public record Customer(String firstName, String lastName, String email, String address, String phone) {
    }

    public record Tariff(int monthlyRental, int localMinutes, int internationalMinutes, int smsPack,
                         int localPerMinute, int internationalPerMinute, int smsCharge) {
    }

    /** A fresh customer; names are letters only because the form rejects digits in names. */
    public static Customer customer() {
        var r = ThreadLocalRandom.current();
        String first = FIRST[r.nextInt(FIRST.length)];
        String last = LAST[r.nextInt(LAST.length)];
        String tag = Long.toString(System.currentTimeMillis(), 36);
        return new Customer(first, last,
                (first + "." + last + "." + tag).toLowerCase() + "@example.com",
                (100 + r.nextInt(800)) + " Test Street, Springfield",
                "20155501" + String.format("%02d", r.nextInt(100)));
    }

    /** The tariff the original test used (all positive whole numbers, as the form requires). */
    public static Tariff standardTariff() {
        return new Tariff(110, 510, 310, 160, 260, 370, 50);
    }

    /** Pulls the numeric customer id out of the confirmation text ("Customer ID 123456" etc.). */
    public static String extractCustomerId(String text) {
        if (text == null) return null;
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\b(\\d{3,})\\b").matcher(text);
        return m.find() ? m.group(1) : null;
    }
}
