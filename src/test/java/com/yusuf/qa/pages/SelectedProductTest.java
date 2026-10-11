package com.yusuf.qa.pages;
import org.junit.Test;
import static org.junit.Assert.*;

public class SelectedProductTest {
    @Test public void extractsSiteProductIdsWithoutQueryParameters() {
        assertEquals("12345", SelectedProduct.idFromUrl("https://www.trendyol.com/brand/name-p-12345?merchantId=1"));
        assertEquals("HBC0123", SelectedProduct.idFromUrl("https://www.hepsiburada.com/name-pm-HBC0123"));
        assertThrows(IllegalArgumentException.class, () -> SelectedProduct.idFromUrl("https://www.trendyol.com/sr?q=mouse"));
    }
    @Test public void toleratesBrandAndNameLineWrapping() {
        assertEquals(SelectedProduct.normalizedName("Havit\nKablosuz Mouse"), SelectedProduct.normalizedName("Havit Kablosuz Mouse"));
        assertNotEquals(SelectedProduct.normalizedName("Havit MS48GT"), SelectedProduct.normalizedName("Havit MS49GT"));
    }
}
