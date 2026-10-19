# Karate tarayıcı senaryoları

Kodlar src/test/java/navigation/navigation.feature içinde, XPath seçicileri common/locators.json dosyasındadır.

Uygulanmış Karate senaryoları:

- TC-001: Ana sayfa kontrolleri.
- TC-002: Kategoriden logoyla ana sayfaya dönüş.
- TC-003: Kategori menüsünü açma, görünür Elbise alt kategorisine tıklama; hedef URL, sayfa başlığı ve ürün kartlarını doğrulama.
- TC-005: kablosuz mouse araması; URL sorgusu, sonuç başlığı, ürün kartları ve ilk beş dolu başlıkta mouse/fare kontrolü.

Diğer tasarımlar @planned etiketlidir ve varsayılan runner tarafından dışlanır. TC-024'ün Selenium kodları Karate adımı değildir; Karate uyarlaması henüz yapılmadığı için planlıdır. TC-005 arama kontrolü tüm kataloğu veya tüm ürünlerin niteliklerini doğrulamaz.

Proje kökünden:

```powershell
.\scripts\run-karate.ps1 -OpenReport

mvn -f karate-ui/pom.xml test '-Dtest=TestRunner' '-Dkarate.options=--tags @TC-003'
```

HTML raporu karate-ui/target/karate-reports/karate-summary.html dosyasındadır. Karate sonuçları SQL Server'a yazılmaz.

19 Ekim 2026 yerel canlı Chrome kontrolünde TC-001, TC-002, TC-003 ve TC-005 birlikte geçti: 4 test, 0 hata. TC-003 menünün gerçekten açılmasını, görünür alt kategori bağlantısını, hedef URL'yi, Elbise başlığını ve görünür ürün kartını doğrular. TC-005'te yazılan metin doğrulandıktan sonra geç açılan modal kapatılıp Enter ayrı gönderilir. Bu sonuç uzak CI veya Pages deployment sonucu değildir.
