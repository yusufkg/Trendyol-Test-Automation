# XPath'i doğrudan Gherkin'de yazma

`XPathSteps.java` ortak adımları çalıştırır. Her element için yeni Java metodu yazmak gerekmez. XPath çift tırnak, XPath içindeki nitelik değerleri tek tırnak içinde yazılır.

Örnek (mevcut feature dosyasına senaryo olarak eklenebilir):

```gherkin
Scenario: I search using XPath
  Given I open the "Trendyol" home page
  When I click "//input[@data-testid='browsing-search-input']"
  And I enter "mouse" into xpath "//input[@data-testid='browsing-search-input']"
  And I press Enter
  Then I verify search results for "mouse" are displayed
```

Görünürlük kontrolü:

```gherkin
Then I verify "//*[@id='logo']" is visible
```

`I click "..."` içindeki değer doğrudan `By.xpath(xpath)` metoduna gider. Tarayıcı önceden açılmalıdır. Mevcut HomePage tabanlı adımlar da korunur; doğrudan XPath adımları için HomePage'e seçici eklemen gerekmez. XPath'ler canlı sitenin güncel HTML yapısına göre seçilmelidir.
