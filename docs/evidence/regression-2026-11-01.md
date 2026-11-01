# Yerel regresyon — 1 Kasım 2026

Kaynak commit: e7d630bd3af11e5e08dae31c10fbf0610bb364d8. Windows, Chrome 156.0.8078.74 ve Java 17 ile çalıştırıldı. Uzak CI koşusuyla aynı kaynak sürümü kullanıldı; aşağıdaki canlı testler yereldir.

| Koşu | Sonuç | Kapsam |
|---|---|---|
| Selenium TC-003 / TC-005 / TC-008 birlikte | 3 geçti, 0 failure/error/skipped | Canlı kategori geçişi, ürün arama ve sonuçsuz arama |
| API smoke XML | 3 geçti | Temel GET/POST işlemleri, yerel HTTP fixture |
| API regression XML | 10 geçti | 5 senaryo, DataProvider dahil |

```powershell
mvn -B -ntp test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-003 or @TC-005 or @TC-008' '-Dcucumber.plugin=pretty,html:target/selenium-new-regression.html,json:target/selenium-new-regression.json,junit:target/selenium-new-regression.xml'
mvn -B -ntp -f api-tests/pom.xml clean test '-Dqa.suite=src/test/resources/testng-smoke.xml'
mvn -B -ntp -f api-tests/pom.xml clean test
```

Canlı Selenium senaryoları aynı Maven koşusunda sıralı çalıştı; her senaryoda yeni Chrome oturumu açıldı ve kapatıldı. Toplam test süresi 121,6 saniye, Maven toplamı yaklaşık 2 dakika 5 saniye. Üçü de BUILD SUCCESS ile tamamlandı. Tek bir başarılı regresyon uzun dönem kararlılık garantisi değildir.

Selenium HTML, JSON ve JUnit çıktıları target altında; API Surefire XML/HTML çıktıları api-tests/target/surefire-reports altındadır. İkinci API clean koşusu smoke raporlarının üzerine regression raporlarını oluşturur; smoke 3, regression 10 sayıları kendi koşularında XML üzerinden kontrol edildi. Üretilen çıktılar Git'e eklenmez.

Diğer üç aktif Selenium senaryosu (TC-001, TC-002, TC-024) ve dört canlı Karate senaryosu bu yerel koşuda tekrar çalıştırılmadı. Güncel uzak standart CI sonucu [ayrı kayıtta](ci-2026-10-31.md) açıklanır; o koşuda canlı Trendyol adımları atlanmıştır. Chrome CDP sürüm uyarısı görüldü; standart WebDriver adımları tamamlandı.
