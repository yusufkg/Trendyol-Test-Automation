package com.yusuf.qa.pricing;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** General price parsing for the planned e-commerce price checks. */
public final class TurkishPrice {
    private TurkishPrice() {}
    public static BigDecimal parse(String raw) {
        if (raw == null) return null;
        Matcher match = Pattern.compile("([0-9][0-9.,\\s]*[0-9]|[0-9])\\s*(?:TL|₺)", Pattern.CASE_INSENSITIVE).matcher(raw);
        if (!match.find()) return null;
        String number = match.group(1).replaceAll("\\s", "");
        if (number.contains(",")) number = number.replace(".", "").replace(',', '.');
        else if (number.matches("\\d{1,3}(\\.\\d{3})+")) number = number.replace(".", "");
        try { return new BigDecimal(number); } catch (NumberFormatException e) { return null; }
    }
}
