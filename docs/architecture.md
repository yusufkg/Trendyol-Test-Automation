# Otomasyon mimarisi

```mermaid
flowchart TD
    A[GitHub Actions] --> B[Maven: birim testleri]
    B --> C[Cucumber: adım eşleşmesi / dry-run]
    C --> D[Karate: yerel HTML sayfasında headless Chrome]
    D --> E[JUnit XML ve HTML rapor artifactleri]
    F[Elle seçilen canlı testler] --> G[Selenium / Cucumber]
    F --> H[Karate TestRunner]
    G --> I[Steps: Gherkin ifadeleri]
    I --> J[Pages ve ortak XPath işlemleri]
    J --> K[Chrome: Trendyol]
    H --> K
    K --> E
```

Ana Maven projesi JUnit 4, Cucumber ve Selenium kullanır. Karate ayrı POM ile JUnit 5 çalıştırır. Bağımlılıklar ve test keşfi birbirinden ayrıdır.

Selenium: e-commerce.feature → EcommerceIT → steps → pages/TestContext → Chrome. XPathSteps doğrudan Gherkin'den XPath alabilir. Hooks tarayıcı oturumunu kapatır.

Karate: TestRunner → navigation.feature → common/locators.json + karate-config.js → Chrome. Java Step Definition gerekmez. SetupRunner, Trendyol'a bağlanmadan fixture.html üzerinde input/click/assertion ve screenshot işlemlerini doğrular.

Standart CI, canlı site erişiminin başarılı olduğunu iddia etmez. Cucumber dry-run yalnızca adımların Java'ya eşleşmesini doğrular. Canlı testler workflow_dispatch seçeneğiyle ayrıca çalıştırılabilir. Sayfa değişiklikleri veya güvenlik engelleri test başarısızlığı olarak korunur.

Güncel kaynakta SQL logger veya SQL sonuç tablosuna bağlanan plugin bulunmuyor. Önceki sürümlerdeki SQL doğrulamaları bu sürümün özelliği olarak gösterilmez. Raporlar Maven/Cucumber/Karate tarafından üretilir ve CI artifact'i olarak saklanır.
