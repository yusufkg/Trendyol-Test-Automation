# E-ticaret Test Otomasyonu

Java 17, Selenium, Cucumber/Gherkin, JUnit, Maven ve SQL Server.

## Mevcut kapsam

Tek feature: src/test/resources/features/e-commerce.feature. Trendyol ve Hepsiburada aynı SearchSteps / SearchPage sınıflarıyla giriş gerektirmeyen kablosuz mouse aramasını çalıştırır. Ayrı @demo etiketi mevcut örnek giriş testini seçer. Genel fiyat ayrıştırma yardımcı sınıfı ve üç birim testi vardır.

Telefon rehberi ve Samsung S20'ye özel giriş/model/sıralama/sepet akışı kaldırıldı. SQL test loglama, HTML/JSON/JUnit raporları ve 48 misafir senaryo planı korunur. TestStatus ve FailureCategory enum'ları vardır; özel sınıflandırıcı entegrasyonu henüz yapılmadı. Mevcut logger framework durumlarını (PASSED/FAILED/SKIPPED) kaydeder.

## Çalıştırma

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17'
# Birim testleri
mvn test
# Birim testleri ve zorunlu SQL kayıtları
.\scripts\run-sql-tests.ps1 -TrustServerCertificate
# Trendyol misafir araması
.\scripts\run-sql-tests.ps1 -Trendyol -TrustServerCertificate
# Hepsiburada misafir araması
.\scripts\run-sql-tests.ps1 -Hepsiburada -TrustServerCertificate
# İki site birlikte
.\scripts\run-sql-tests.ps1 -Trendyol -Hepsiburada -TrustServerCertificate
# Örnek demo giriş testi
mvn test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@demo'
# Tarayıcı açmadan senaryo/step eşleşmesi
mvn test '-Dtest=EcommerceIT' '-Dcucumber.execution.dry-run=true'
```

Misafir arama senaryoları hesap veya parola istemez. Site erişimi güvenlik nedeniyle engellenirse test başarısız olur; doğrulama atlanmaz. Dry-run ve birim testleri canlı site başarısını kanıtlamaz.

## SQL Server

Instance: DESKTOP-SDLI8OI\SQLEXPRESS. Veritabanı: QaAutomation. Mevcut yerel JDBC adresi: localhost:14330.

```powershell
# TestResults tablosunu kur ve Windows auth DLL dosyasını indir
.\scripts\setup-sql.ps1
# Gerektiğinde yönetici PowerShell ile yerel TCP etkinleştir
.\scripts\enable-local-sql.ps1
```

Şema yalnızca dbo.TestResults tanımını içerir. Mevcut veritabanı tabloları ve geçmiş sonuçlar otomatik silinmez. run-sql-tests.ps1 Windows kimlik doğrulamasını ve target/native DLL yolunu ayarlar. SQL kaydı başarısızsa çalıştırma başarısız olur.

TrustServerCertificate varsayılanı false'tur. -TrustServerCertificate yerel geliştirme sertifikasının doğrulanmasını kapatır; bağlantı şifreli kalır. Bu seçenek yerel test için kullanıcı tarafından onaylandı. Java .env dosyasını otomatik okumaz; parolaları kaynak koda yazmayın. mvn clean native DLL dosyasını da siler; sonrasında setup-sql.ps1 yeniden çalıştırılmalıdır.

## Raporlar ve belgeler

- target/qa-dashboard.html: süreler, sonuçlar, hata ayrıntıları ve SQL kayıt durumu.
- target/ecommerce-report.html ve .json: Cucumber senaryoları.
- target/cucumber-results.xml: Jenkins için JUnit biçimli rapor.
- target/surefire-reports/: Maven çıktıları.
- target/screenshots/: alınabildiğinde hata görselleri.
- docs/test-terimleri.md ve docs/test-plani.md: tanımlar ve kabul ölçütleri.
- docs/guest-test-scenarios/: 48 planlanan misafir senaryosu; tamamı uygulanmış değildir.

```sql
SELECT TOP (100) * FROM dbo.TestResults ORDER BY Id DESC;
SELECT Status, COUNT(*) AS TestCount FROM dbo.TestResults GROUP BY Status;
```

SQL sütunu SAVED başarılı kayıt, NOT_CONFIGURED eksik bağlantı, FAILED kayıt hatası anlamındadır. Senaryoları Hooks, sıradan JUnit testlerini JunitResultListener kaydeder.

HTML ve SQL kullanım adımları: [docs/html-sql-reporting.md](docs/html-sql-reporting.md). Raporu otomatik açmak için run-sql-tests.ps1 komutuna -OpenReport ekleyin. SQL sonuç sorguları database/test-results.sql dosyasındadır.

## Gherkin tanımlarının feature dosyasındaki yeri

48 misafir senaryosu artık src/test/resources/features/e-commerce.feature içindeki "Planlanan 48 misafir kullanıcı senaryosu" kuralında da bulunur. Her biri @planned ve TC etiketi taşır. Tek feature korunur; yeni bir feature dosyası açılmadı.

Varsayılan runner ve IntelliJ Cucumber yapılandırması not @planned filtresini kullanır. Böylece Selenium adımları henüz uygulanmamış tanımlar, çalışan arama/demo senaryolarını bozmaz. @planned tanımlarını çalıştırmak henüz desteklenmez; step kodları tamamlandıkça bu etiket kaldırılmalıdır. guest-gherkin.md açıklamalı senaryo referansı olarak korunur.

Gherkin dili İngilizceye çevrilmiştir. Bütün adımlar birinci tekil şahısla yazılır: I click, I enter, I press, I verify. Çalışan arama senaryosundaki bu adımlar SearchSteps.java içinde ayrı metotlara bağlanır. Aranan kelimeler Türkçe site verisi olarak korunur. @planned tanımlarının Selenium uygulama durumu değişmemiştir.
