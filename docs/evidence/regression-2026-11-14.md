# Toplu yerel doğrulama — 14 Kasım 2026

Kaynak commit: 7e8ac13. Windows üzerinde çalıştırılan paketler:

| Paket | Başarılı | Kapsam |
|---|---|---|
| SelectedProductTest, TurkishPriceTest, ScenarioCleanupTest | 11 | Birim testleri |
| EcommerceIT | 7 | Cucumber dry-run |
| TestNG smoke | 3 | Yerel API |
| TestNG regresyon | 10 | Yerel API |
| SetupRunner | 1 | Yerel HTML üzerinde Karate/Chrome |
| SearchSmokeTest | 1 | Yerel HTML üzerinde Playwright/Chromium |

Nihai koşularda failures, errors ve skipped sayıları sıfırdır. Surefire XML dosyaları test sayıları ve hata değerleri için kontrol edildi. Karate dizininde önceki TestRunner raporu da bulunmaktadır; bu çalıştırmada yalnızca SetupRunner seçildi.

## Çalıştırılan komutlar

```powershell
mvn -B -ntp clean test
mvn -B -ntp test '-Dtest=EcommerceIT' '-Dcucumber.execution.dry-run=true'
mvn -B -ntp -f api-tests/pom.xml clean test '-Dqa.suite=src/test/resources/testng-smoke.xml' '-Dqa.report.directory=target/smoke-reports'
mvn -B -ntp -f api-tests/pom.xml test
mvn -B -ntp -f karate-ui/pom.xml test '-Dtest=SetupRunner' '-Dheadless=true'
.\scripts\run-playwright.ps1
.\scripts\run-playwright.ps1 -InstallBrowser
```

## Ortam sorunu ve tekrar

Playwright ilk denemede chromium_headless_shell-1248 yürütülebilir dosyası bulunamadığı için hata verdi. -InstallBrowser ile Chromium tekrar kuruldu; ardından aynı smoke testi 1/1 geçti. Bu nedenle ilk hata ve başarılı tekrar birlikte kaydedildi.

Canlı Trendyol Selenium, Karate ve Playwright testleri bu doğrulamada çalıştırılmadı. Playwright canlı TC-001 için son kayıt 9 Kasım HTTP 403 sonucudur. Bu kayıt yerel doğrulamadır; uzak CI sonucu ayrıca incelenmelidir.
