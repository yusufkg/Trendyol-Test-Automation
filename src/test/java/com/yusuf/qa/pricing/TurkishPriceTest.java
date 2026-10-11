package com.yusuf.qa.pricing;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class TurkishPriceTest {
    @Test public void parsesDecimalPrice() { assertEquals(new BigDecimal("12499.90"), TurkishPrice.parse("12.499,90 TL")); }
    @Test public void parsesIntegerPrice() { assertEquals(new BigDecimal("12499"), TurkishPrice.parse("12.499 TL")); }
    @Test public void returnsNullWhenPriceIsMissing() { assertNull(TurkishPrice.parse("Ürün stokta yok")); }
}
