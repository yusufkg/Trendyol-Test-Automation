# Test kanıtları

| Doğrulama | Sonuç | Kayıt |
|---|---|---|
| Selenium hata raporu, 3 Kasım | 11 birim testi ve 7 dry-run senaryosu geçti. Kasıtlı XPath hatasında gerçek Chrome görüntüsü HTML ve JSON içinde doğrulandı. | [Hata ekran görüntüsü](evidence/failure-screenshots.md) |
| Canlı Selenium, 2 Kasım | TC-003, TC-005, TC-008 ve TC-010 birlikte 4/4 geçti. | [Toplu Chrome koşusu](evidence/selenium-tc010.md) |
| Yerel API, 1 Kasım | TestNG smoke 3/3, regresyon 10/10 geçti. RestAssured yerel test sunucusunu kullandı. | [API ve Selenium koşuları](evidence/regression-2026-11-01.md) |
| GitHub Actions, 31 Ekim | API regresyonu, kök birim testleri, Cucumber dry-run ve yerel Karate Chrome testi başarılı. | [CI #55](evidence/ci-2026-10-31.md) |

Canlı Selenium kayıtları koşulan senaryo kimliklerini gösterir. Cucumber dry-run adım eşleşmelerini kontrol eder. Karate smoke testi yerel HTML sayfasını kullanır.

## Tarihsel yerel kayıt — 17 Ekim 2026

Bu kayıt yerel Windows ortamında gerçek komutlarla oluşturuldu. Uzak GitHub Actions başarısı veya canlı Trendyol sonucu olarak sunulmaz.

| Kontrol | Sonuç | Kanıtladığı kapsam |
|---|---|---|
| SelectedProductTest | 2 geçti | Ürün kimliği / ad yardımcıları |
| TurkishPriceTest | 3 geçti | Türkçe fiyat ayrıştırma |
| EcommerceIT dry-run | 3 adım eşleşmesi geçti | Gherkin–Java eşleşmesi; tarayıcı işlemi çalışmaz |
| SetupRunner | 1 geçti | Gerçek headless Chrome: yerel HTML'de metin yazma, tıklama, sonuç doğrulama |

[Makine tarafından okunabilir sonuç özeti](evidence/local-results.json), Surefire XML dosyalarından çıkarılmıştır. sourceCommit doğrulanan kaynak tabanını gösterir; kanıt dokümanlarını içeren sonraki commit ile karıştırılmamalıdır.

```powershell
mvn clean test '-Dtest=*Test,EcommerceIT' '-Dcucumber.execution.dry-run=true'
mvn -f karate-ui/pom.xml test '-Dtest=SetupRunner' '-Dheadless=true'
```

## Tarayıcı ekran görüntüsü

Bu, yerel fixture testinin `screenshot()` adımından alınmıştır; Trendyol arama sonucu değildir.

![Karate yerel Chrome testi: mouse girdisi ve Search: mouse sonucu](evidence/karate-local-smoke.png)

## Güncel CI sonuçlarını görme

[Actions sayfasından](https://github.com/yusufkg/Trendyol-Test-Automation/actions/workflows/ci.yml) ilgili commit koşusunu açın. `test-reports` artifact'ini indirip Surefire XML, Cucumber dry-run HTML ve Karate HTML raporlarını inceleyin. Canlı senaryolar yalnızca elle seçilen koşuda çalışır. Raporların kapsamı job özetinde ayrıca belirtilir.
