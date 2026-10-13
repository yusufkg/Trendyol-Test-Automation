# Misafir e-ticaret test otomasyonu

Java, Selenium, Cucumber, JUnit ve Maven. Tek feature dosyasında yalnızca TC-001–TC-048 misafir senaryoları bulunur. TC-001, TC-002 ve TC-024 uygulanmıştır; kalan 45 senaryo @planned etiketli tasarımdır.

Trendyol login, Samsung S20 alışveriş akışı, SQL kayıtları, HTML/görsel raporlama ve katalog dışındaki demo/arama senaryoları kaldırılmıştır. SearchPage, TC-024 ürün detay akışının arama işlemleri için korunur.

Birim testleri: mvn clean test

Adım eşleşmesi: mvn test '-Dtest=EcommerceIT' '-Dcucumber.execution.dry-run=true'

TC-002 canlı test: mvn test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-002' '-Dqa.site=Trendyol'

XPath doğrudan Gherkin'de kullanılabilir: I click "..." ve I verify "..." is visible. Sonuçlar terminal ve standart Maven surefire çıktılarından incelenir.