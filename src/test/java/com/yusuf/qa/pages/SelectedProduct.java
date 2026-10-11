package com.yusuf.qa.pages;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.regex.Pattern;

public record SelectedProduct(String id, String name, BigDecimal price, String url) {
    public static String idFromUrl(String url) {
        var matcher = Pattern.compile("-pm?-([A-Za-z0-9]+)(?:[/?#]|$)").matcher(url == null ? "" : url);
        if (!matcher.find()) throw new IllegalArgumentException("Ürün kimliği URL'den okunamadı: " + url);
        return matcher.group(1);
    }
    public static String normalizedName(String name) {
        return name == null ? "" : name.toLowerCase(Locale.forLanguageTag("tr"))
            .replace('\u00a0', ' ').replaceAll("\\s+", "");
    }
}
