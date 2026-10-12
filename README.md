# E-commerce test automation

Java 17+, Selenium, Cucumber/Gherkin, JUnit ve Maven.

Aktif Selenium senaryoları: Trendyol ve Hepsiburada misafir ürün araması, TC-024 ürün detay kontrolü ve ayrı demo sitesinde giriş testi. Tek feature dosyası `src/test/resources/features/e-commerce.feature` kullanılır. Misafir kataloğundaki diğer 47 senaryo `@planned` olarak tasarımdır; Selenium uygulamaları henüz yoktur.

Trendyol hesap girişi, Samsung S20 arama/sıralama/sepet akışı, SQL Server test kayıtları ve otomatik HTML/görsel raporlama kaldırıldı.

## Çalıştırma

PowerShell'de proje klasöründen:

```powershell
# Birim testleri
mvn clean test
# Aktif Gherkin adımlarını tarayıcı açmadan kontrol et
mvn test '-Dtest=EcommerceIT' '-Dcucumber.execution.dry-run=true'
# Trendyol misafir araması
mvn test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@search and @trendyol'
# Hepsiburada misafir araması
mvn test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@search and @hepsiburada'
# Ürün detay kontrolü
mvn test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-024' '-Dqa.site=Trendyol'
```

Canlı testler Chrome açar. Sonuçlar terminalde ve Maven'in standart `target/surefire-reports` çıktılarında bulunur. Dry-run yalnızca Gherkin ile Java adımlarının eşleşmesini kontrol eder.

Test planı: `docs/test-plani.md`. 48 misafir senaryo tasarımı: `docs/guest-test-scenarios`.