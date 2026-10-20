# API otomasyonu — tasarım aşaması

Bu klasör henüz Maven modülü veya çalışan API testleri içermez. Sonraki küçük geliştirmede bağımsız POM ile RestAssured/TestNG kurulması planlanır. Ana Selenium ve Karate projelerinin bağımlılıkları değiştirilmeyecek.

## Hedef yapı

```text
api-tests/
  pom.xml
  src/test/java/com/yusuf/qa/api/
    fixture/LocalApiServer.java
    tests/ProductApiTest.java
  src/test/resources/testng.xml
```

Yerel, testin başlatıp kapattığı bir örnek HTTP API tercih edilir. Dış demo servislerinin erişilebilirliği veya yazma kısıtları test sonucunu etkilemez. Bu, gerçek Trendyol API entegrasyonu olarak sunulmaz.

| Test | İstek | Beklenti |
|---|---|---|
| API-001 | GET /products | 200, JSON içerik türü ve liste alanları |
| API-002 | GET /products/1 | 200, doğru ürün kimliği/adı |
| API-003 | POST /products, geçerli gövde | 201, kimlik ve gönderilen alanlar; GET ile kalıcılık kontrolü |
| API-004 | POST /products, geçersiz gövde | 400, beklenen hata alanları; yeni kayıt oluşmaması |
| API-005 | GET /products/999 | 404, beklenen hata şeması |

Negatif veriler TestNG DataProvider ile çoğaltılabilir. Her test izole veriyle başlar; fixture teardown adımında kapatılır. Assertions yalnızca durum kodunu değil JSON alanlarını ve yazma davranışını da doğrular. Kendi fixture'ını sınamak gerçek bir ürün servisinin doğruluğunu kanıtlamaz; amaç API test otomasyonu mekaniklerini göstermektir.

TestNG smoke/regression grupları ve Surefire XML sonuçları sonraki aşamada eklenecek. POM ve testler oluşturulmadan hiçbir API başarı sonucu bildirilmeyecek.
