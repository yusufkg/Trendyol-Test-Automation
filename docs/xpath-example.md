# İlk aktif senaryo: XPath ile misafir araması

Tek feature dosyasının ilk senaryosu ürün aramasıdır. TC-001 ana sayfa senaryosu ise hâlâ planlıdır. Bu örnek ilk aktif arama senaryosunu kullanır.

```gherkin
Scenario: I search for a product on Trendyol as a guest
  Given I open the "Trendyol" home page
  When I click the search field
  And I enter "kablosuz mouse" into the search field
  And I press Enter
  Then I verify search results for "kablosuz mouse" are displayed
```

Bu örnek mevcut Scenario Outline'ın Trendyol satırıdır; ikinci bir feature dosyası oluşturulmaz.

Java adım kodları `src/test/java/com/yusuf/qa/steps/SearchSteps.java`, Selenium işlemleri `src/test/java/com/yusuf/qa/pages/SearchPage.java` dosyasındadır.

Trendyol arama alanının XPath seçicisi:

```java
searchInput = By.xpath("//input[@data-testid='browsing-search-input' or @data-testid='suggestion' or @type='search']");
```

`//input` sayfadaki input elementlerini arar. Köşeli parantez içindeki koşul, bu alanlardan uygun niteliğe sahip olanları seçer. Java kodu görünür ve etkin alanı bekler; tıklar, metni yazar ve Enter gönderir. Sonuç başlığının sorguyu içerdiği ve görünür ürün kartlarının bulunduğu doğrulanır.

Bir kartın içinden başlık ararken `card.findElements(By.xpath(".//h2"))` kullanılır. Başındaki nokta, aramayı o kartın içinde tutar; `//h2` ise tüm belgede arama yapar.

Çalıştırma:

```powershell
mvn test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@search and @trendyol'
```

Arama, ürün detay ve demo giriş sayfalarındaki element seçicileri XPath'e dönüştürüldü. Gherkin tanımları element seçicisi içermez; seçiciler Page sınıflarında tutulur. Site yapısı değişirse ilgili XPath burada güncellenir.
