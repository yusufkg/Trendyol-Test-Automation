# API otomasyonu

Java 17, RestAssured 6.1.0 ve TestNG 7.11.0 ile bağımsız Maven projesi.

```powershell
mvn -B -ntp -f api-tests/pom.xml clean test
```

- `src/test/java/com/yusuf/qa/api/fixture/LocalApiServer.java`: 127.0.0.1 üzerinde boş portta başlayan kontrollü HTTP API.
- `src/test/java/com/yusuf/qa/api/tests/ProductApiTest.java`: TestNG yaşam döngüsü ve RestAssured doğrulamaları.
- `target/surefire-reports/`: XML ve TestNG raporları.

API-001 GET /products uygulandı: HTTP 200, JSON içerik türü, toplam, liste boyutu, ürün kimliği, adı ve fiyatı doğrulanır. Her test kendi sunucusunu başlatıp kapatır.

28 Ekim 2026 yerel regression sonucu: 10 test çalıştırması, 0 başarısızlık, 0 hata, 0 atlanan; BUILD SUCCESS.

Gerçek Trendyol API entegrasyonu değildir. Ana Maven komutu bu bağımsız projeyi çalıştırmaz. CI yapılandırmasına ayrı api-tests job eklendi; raporlar api-test-reports artifact olarak 14 gün saklanacak. Başarısız testlerde de rapor yükleme adımı çalışır. API raporları Pages sitesine dahil edilmedi. 26 Ekim CI koşusu 40276213865 için API job ve artifact kaydı başarılı olarak doğrulandı; bu koşu DataProvider öncesindeki 5 çalıştırmayı kapsar. 1 Kasım incelemesinde CI #55 (41204004405, commit e7d630b) API job logu 10 başarılı çalıştırma gösterdi; api-test-reports artifact kaydı mevcuttur. Kendi fixture testimiz API otomasyonu mekaniklerini gösterir.

API-002 GET /products/1 uygulandı: pathParam ile gönderilen ürün kimliği, HTTP 200, JSON içerik türü, ürün adı ve fiyatı doğrulanır.

API-003 POST /products uygulandı: 201 ve ürün alanları doğrulanır; dönen kimlikle GET yapılır, ürünün listede bulunduğu ve toplamın arttığı kontrol edilir. Kayıtlar yalnızca test sunucusunun belleğinde tutulur.

API-004 geçersiz POST uygulandı: DataProvider ile negatif fiyat, sıfır fiyat, boş ad, eksik ad, eksik fiyat ve metin fiyat için 400 ve INVALID_PRODUCT doğrulanır. İstek öncesi/sonrası ürün listesi karşılaştırılır ve yeni kimliğin 404 döndürdüğü kontrol edilir.

API-005 GET /products/999 uygulandı: HTTP 404, JSON içerik türü ve tam NOT_FOUND hata gövdesi doğrulanır. Ardından mevcut ürünün hâlâ okunabildiği kontrol edilir. Smoke/regression grupları uygulandı.

DataProvider verileri: src/test/java/com/yusuf/qa/api/data/InvalidProductData.java. Beş senaryo kimliği korunur: API-004 altı veri satırıyla, diğer dört senaryo birer kez çalışır (toplam 10). Her veri satırından önce yeni sunucu başlatılır.

## TestNG suite seçimi

Varsayılan testng.xml, regression grubunu çalıştırır: API-001–API-005, toplam 10 çalıştırma. CI komutu değişmeden bu kapsamı kullanır.

```powershell
mvn -B -ntp -f api-tests/pom.xml clean test
```

Smoke XML yalnızca API-001, API-002 ve API-003 seçer; toplam 3 çalıştırma:

```powershell
mvn -B -ntp -f api-tests/pom.xml clean test '-Dqa.suite=src/test/resources/testng-smoke.xml'
```

Suite dosyaları src/test/resources/ altındadır. Hazırlık/kapanış metotları alwaysRun=true ile grup filtresinden bağımsız çalışır. İki suite de sıralı çalışır; paylaşılan test nesnesinde paralel çalışma yapılandırılmadı.

28 Ekim yerel doğrulama: smoke 3/3, regression 10/10 başarılı. Surefire XML üzerinden smoke yöntem adları ve regression çalıştırma sayısı kontrol edildi. Son çalıştırma önceki target raporlarını yeniler. Regression XML uzak CI #55 içinde 10/10 başarılıdır; smoke XML uzak koşuda ayrıca seçilmedi.

1 Kasım 2026 yerel tekrar: smoke 3/3, regression 10/10 başarılı. Uzak CI kanıtı: https://github.com/yusufkg/Trendyol-Test-Automation/actions/runs/41204004405 . Artifact kaydı kontrol edildi; ZIP içeriği indirilmedi.

## PowerShell çalıştırıcısı

Proje kökünden regresyon için .\scripts\run-api.ps1, üç testlik smoke paketi için .\scripts\run-api.ps1 -Smoke çalıştırılır.

Script yerel örnek API testlerini çalıştırır. Her koşu clean test ile API modülünün önceki target çıktılarını temizler. TestNG HTML raporunun tam yolu terminale yazılır. Maven başarısızsa script hata verir ve başlangıç klasörüne geri döner.

13 Kasım 2026: scripts/run-api.ps1 ile smoke 3/3 ve varsayılan regresyon 10/10 geçti. Her koşunun Surefire TEST-TestSuite.xml dosyasında test sayısı, failures ve errors kontrol edildi. Bu doğrulama yerel örnek API kapsamındadır.

14 Kasım 2026: CI API job'ına önce smoke, ardından regresyon adımı eklendi. Smoke çıktısı qa.report.directory=target/smoke-reports ile ayrılır; regresyon varsayılan target/surefire-reports dizinini kullanır. İkinci adım clean çalıştırmaz, böylece ilk rapor korunur. api-test-reports artifact'i iki klasörü de toplar. İlk smoke adımı başarısızsa regresyon başlatılmaz.

Aynı Maven komutları yerelde doğrulandı: smoke 3/3, regresyon 10/10. Her iki TEST-TestSuite.xml dosyasının aynı anda mevcut olduğu ve failures/errors değerlerinin sıfır olduğu kontrol edildi. Uzak CI sonucu henüz doğrulanmadı.
