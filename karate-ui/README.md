# Karate tarayıcı senaryoları

Kodlar src/test/java/navigation/navigation.feature içinde, XPath seçicileri common/locators.json dosyasındadır.

Uygulanmış Karate senaryoları:

- TC-001: Ana sayfa kontrolleri.
- TC-002: Kategoriden logoyla ana sayfaya dönüş.
- TC-005: kablosuz mouse araması; URL sorgusu, sonuç başlığı, ürün kartları ve ilk beş dolu başlıkta mouse/fare kontrolü.

Diğer tasarımlar @planned etiketlidir ve varsayılan runner tarafından dışlanır. TC-024'ün Selenium kodları Karate adımı değildir; Karate uyarlaması henüz yapılmadığı için planlıdır. TC-005 arama kontrolü tüm kataloğu veya tüm ürünlerin niteliklerini doğrulamaz.

Proje kökünden:

```powershell
.\scripts\run-karate.ps1 -OpenReport
```

HTML raporu karate-ui/target/karate-reports/karate-summary.html dosyasındadır. Karate sonuçları SQL Server'a yazılmaz.
