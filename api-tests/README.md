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

Gerçek Trendyol API entegrasyonu değildir. Ana Maven komutu bu bağımsız projeyi çalıştırmaz. CI yapılandırmasına ayrı api-tests job eklendi; raporlar api-test-reports artifact olarak 14 gün saklanacak. Başarısız testlerde de rapor yükleme adımı çalışır. API raporları Pages sitesine dahil edilmedi. 26 Ekim CI koşusu 40276213865 için API job ve artifact kaydı başarılı olarak doğrulandı; bu koşu DataProvider öncesindeki 5 çalıştırmayı kapsar. Yeni 10 çalıştırmalık kapsamın uzak CI sonucu ayrıca kontrol edilmelidir. Kendi fixture testimiz API otomasyonu mekaniklerini gösterir.

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

28 Ekim yerel doğrulama: smoke 3/3, regression 10/10 başarılı. Surefire XML üzerinden smoke yöntem adları ve regression çalıştırma sayısı kontrol edildi. Son çalıştırma önceki target raporlarını yeniler. Bu değişikliğin uzak CI sonucu henüz doğrulanmadı.
