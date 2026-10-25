# API otomasyonu

Java 17, RestAssured 6.1.0 ve TestNG 7.11.0 ile bağımsız Maven projesi.

```powershell
mvn -B -ntp -f api-tests/pom.xml clean test
```

- `src/test/java/com/yusuf/qa/api/fixture/LocalApiServer.java`: 127.0.0.1 üzerinde boş portta başlayan kontrollü HTTP API.
- `src/test/java/com/yusuf/qa/api/tests/ProductApiTest.java`: TestNG yaşam döngüsü ve RestAssured doğrulamaları.
- `target/surefire-reports/`: XML ve TestNG raporları.

API-001 GET /products uygulandı: HTTP 200, JSON içerik türü, toplam, liste boyutu, ürün kimliği, adı ve fiyatı doğrulanır. Her test kendi sunucusunu başlatıp kapatır.

24 Ekim 2026 yerel Maven sonucu: 5 test, 0 başarısızlık, 0 hata, 0 atlanan; BUILD SUCCESS.

Gerçek Trendyol API entegrasyonu değildir. Ana Maven komutu, CI ve Pages bu projeyi henüz çalıştırmaz. Kendi fixture testimiz API otomasyonu mekaniklerini gösterir.

API-002 GET /products/1 uygulandı: pathParam ile gönderilen ürün kimliği, HTTP 200, JSON içerik türü, ürün adı ve fiyatı doğrulanır.

API-003 POST /products uygulandı: 201 ve ürün alanları doğrulanır; dönen kimlikle GET yapılır, ürünün listede bulunduğu ve toplamın arttığı kontrol edilir. Kayıtlar yalnızca test sunucusunun belleğinde tutulur.

API-004 geçersiz POST uygulandı: negatif fiyat için 400 ve INVALID_PRODUCT doğrulanır. İstek öncesi/sonrası ürün listesi karşılaştırılır ve yeni kimliğin 404 döndürdüğü kontrol edilir.

API-005 GET /products/999 uygulandı: HTTP 404, JSON içerik türü ve tam NOT_FOUND hata gövdesi doğrulanır. Ardından mevcut ürünün hâlâ okunabildiği kontrol edilir. DataProvider, smoke/regression grupları ve CI entegrasyonu sonraki aşamalardır.
