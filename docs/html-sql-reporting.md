# HTML raporu ve SQL Server kayıtları

Her gerçek test çalıştırmasından sonra iki ayrı çıktı oluşur:

1. **HTML:** `target/qa-dashboard.html` test adı, durum, süre, hata ve SQL kayıt sonucunu gösterir. Cucumber runner çalıştıysa `target/ecommerce-report.html` adımları ve alınabilen ekran görüntülerini içerir.
2. **SQL Server:** `QaAutomation.dbo.TestResults` sonuçları kalıcı olarak saklar. Başlangıç ve bitiş UTC olarak yazılır. HTML özeti son çalıştırmayı, SQL tablosu geçmiş çalıştırmaları da içerir.

## Çalıştır ve raporu aç

```powershell
cd D:\Projects\Trendyol-Test-Automation
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17'

# Birim testleri + zorunlu SQL kayıtları + HTML raporunu aç
.\scripts\run-sql-tests.ps1 -TrustServerCertificate -OpenReport

# Trendyol misafir aramasını da çalıştır
.\scripts\run-sql-tests.ps1 -Trendyol -TrustServerCertificate -OpenReport

# Hepsiburada misafir aramasını da çalıştır
.\scripts\run-sql-tests.ps1 -Hepsiburada -TrustServerCertificate -OpenReport
```

SQL bağlantısı bu bilgisayardaki `localhost:14330` üzerinden Windows kimlik doğrulamasıyla açılır. İlk kurulum gerekiyorsa `scripts/setup-sql.ps1` çalıştırılır. `-TrustServerCertificate` yerel geliştirme sertifikası içindir; bağlantı şifreli kalır fakat sunucu sertifikası doğrulaması kapatılır.

## SQL sonuçlarını gör

SSMS'te `DESKTOP-SDLI8OI\SQLEXPRESS` sunucusuna bağlanıp `QaAutomation` veritabanında `database/test-results.sql` dosyasını çalıştırın. Son testler, durum özeti ve başarısız kayıtlar listelenir. Alternatif:

```powershell
sqlcmd -S '.\SQLEXPRESS' -E -d QaAutomation -b -i database/test-results.sql
```

`Status` mevcut JUnit/Cucumber durumudur (`PASSED`, `FAILED`, `SKIPPED` vb.). `TestStatus` ve `FailureCategory` enum'ları tek başına bu değerleri değiştirmez; özel sınıflandırma entegrasyonu ayrı iştir.

HTML'deki SQL alanı: `SAVED` başarılı insert, `NOT_CONFIGURED` bağlantı ayarının yokluğu, `FAILED` SQL kayıt hatasıdır. SQL kapalıysa HTML yine üretilir; zorunlu SQL çalıştırması başarısız olur. SQL kayıt hatası ile testin fonksiyonel sonucu farklı alanlarda gösterilir.

## Otomasyon kapsamı

48 Gherkin senaryo tanımı dokümanda tamamlandı; bunların tamamının Selenium kodu yazılmadı. Rapor gerçek birim testlerini ve aktif feature içinde çalıştırılan senaryoları içerir. `dry-run` step eşleşmesi kontrolüdür; gerçek site sonucu veya SQL senaryo kaydı olarak değerlendirilmez.

Jenkins için mevcut çıktılar: `target/surefire-reports/TEST-*.xml` ve `target/cucumber-results.xml`. HTML raporu ekran görüntüsü bağlantılarıyla taşınacaksa `target/screenshots` dizinini de birlikte saklayın.

## IntelliJ'den doğrudan feature çalıştırma

Feature dosyasındaki çalıştır düğmesi, EcommerceIT üzerindeki rapor plugin ayarlarını kullanmayabilir. Ayrıca terminal scriptinin ortam değişkenleri IntelliJ'nin ayrı Java işlemine kendiliğinden aktarılmaz. Bu nedenle çıktıda `SQL logging skipped: QA_DB_URL is not configured` görülebilir.

En kolay yol IntelliJ terminalinden run-sql-tests.ps1 scriptini kullanmaktır. Doğrudan Cucumber çalıştırma kullanılacaksa ilgili run configuration için:

- Working directory: D:\Projects\Trendyol-Test-Automation
- Ortam değişkeni QA_DB_URL: jdbc:sqlserver://localhost:14330;databaseName=QaAutomation;integratedSecurity=true;encrypt=true;trustServerCertificate=true;loginTimeout=10
- VM seçeneği: -Djava.library.path=D:\Projects\Trendyol-Test-Automation\target\native
- Java 17 seçin.
- Cucumber CLI seçeneklerine mevcut IntelliJ formatter'ını koruyarak --plugin com.yusuf.qa.support.FailureCapture --plugin html:target/ecommerce-report.html --plugin json:target/ecommerce-report.json ekleyin; alternatif olarak EcommerceIT JUnit runner'ını çalıştırın.

Bu bağlantı ve sertifika seçeneği yalnızca mevcut bilgisayardaki onaylanmış yerel geliştirme ayarı içindir. SQL şifresi gerekmez; Windows kimlik doğrulaması kullanılır.
