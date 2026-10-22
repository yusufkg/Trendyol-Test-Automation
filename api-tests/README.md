# API otomasyonu

Java 17, RestAssured 6.1.0 ve TestNG 7.11.0 ile bağımsız Maven projesi.

```powershell
mvn -B -ntp -f api-tests/pom.xml clean test
```

- `src/test/java/com/yusuf/qa/api/fixture/LocalApiServer.java`: 127.0.0.1 üzerinde boş portta başlayan kontrollü HTTP API.
- `src/test/java/com/yusuf/qa/api/tests/ProductApiTest.java`: TestNG yaşam döngüsü ve RestAssured doğrulamaları.
- `target/surefire-reports/`: XML ve TestNG raporları.

API-001 GET /products uygulandı: HTTP 200, JSON içerik türü, toplam, liste boyutu, ürün kimliği, adı ve fiyatı doğrulanır. Her test kendi sunucusunu başlatıp kapatır.

21 Ekim 2026 yerel Maven sonucu: 2 test, 0 başarısızlık, 0 hata, 0 atlanan; BUILD SUCCESS.

Gerçek Trendyol API entegrasyonu değildir. Ana Maven komutu, CI ve Pages bu projeyi henüz çalıştırmaz. Kendi fixture testimiz API otomasyonu mekaniklerini gösterir.

API-002 GET /products/1 uygulandı: pathParam ile gönderilen ürün kimliği, HTTP 200, JSON içerik türü, ürün adı ve fiyatı doğrulanır.

Planlı: API-003 geçerli POST ve kayıt kontrolü; API-004 geçersiz POST; API-005 bulunamayan ürün. DataProvider, smoke/regression grupları ve CI entegrasyonu sonraki aşamalardır.
