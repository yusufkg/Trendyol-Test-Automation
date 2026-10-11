package com.yusuf.qa.pages;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class TrendyolPriceTest {
    @Test public void turkishPriceWithDecimals() {
        assertEquals(new BigDecimal("12499.90"), TrendyolPage.parseTurkishPrice("12.499,90 TL"));
    }
    @Test public void turkishPriceWithoutDecimals() {
        assertEquals(new BigDecimal("12499"), TrendyolPage.parseTurkishPrice("12.499 TL"));
    }
    @Test public void priceMissing() {
        assertNull(TrendyolPage.parseTurkishPrice("Ürün stokta yok"));
    }
}
