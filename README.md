# Trendyol Test Automation

[![Maven and Karate CI](https://github.com/yusufkg/Trendyol-Test-Automation/actions/workflows/ci.yml/badge.svg)](https://github.com/yusufkg/Trendyol-Test-Automation/actions/workflows/ci.yml)

Java tabanlı QA otomasyon portföyü. Misafir e-ticaret akışları, kontrollü yerel API testleri, tarayıcı hata analizi ve GitHub Actions raporlaması içerir. Selenium/Cucumber, Karate, RestAssured/TestNG ve Playwright dört bağımsız Maven projesinde çalışır.

## Güncel kapsam

| Proje | Teknolojiler | Uygulanan kapsam |
|---|---|---|
| Kök Maven projesi | Java 17, Selenium 4.27.0, Cucumber 7.20.1, JUnit 4 | 11 birim testi, 7 UI senaryosu |
| karate-ui | Karate 1.5.2, JUnit 5 | 4 Trendyol senaryosu, 1 yerel Chrome smoke testi |
| api-tests | RestAssured 6.1.0, TestNG 7.11.0 | 5 API senaryosu; smoke 3, regresyon 10 çalıştırma |
| playwright-ui | Playwright Java 1.64.0, JUnit 4 | 1 yerel Chromium smoke testi, canlı TC-001 uygulaması |

48 senaryoluk katalogda Selenium için 7 uygulanmış / 41 planlı, Karate için 4 uygulanmış / 44 planlı senaryo vardır. @planned etiketli senaryolar varsayılan koşuya alınmaz. Aynı TC kimliği birden fazla framework içinde uygulanabilir.

| Senaryo | Selenium | Karate |
|---|---|---|
| TC-001 ana sayfa kontrolleri | Uygulandı | Uygulandı |
| TC-002 logoyla ana sayfaya dönüş | Uygulandı | Uygulandı |
| TC-003 kategori menüsünden Elbise alt kategorisi | Uygulandı | Uygulandı |
| TC-005 geçerli ürün arama | Uygulandı | Uygulandı |
| TC-008 sonuç bulunamayan arama | Uygulandı | Planlı |
| TC-010 arama sorgusunu değiştirme | Uygulandı | Planlı |
| TC-024 ürün detay doğrulaması | Uygulandı | Planlı |

Playwright canlı TC-001 son yerel koşusunda HTTP 403 aldı. [HTTP durumu ve hata kaydı](docs/evidence/playwright-tc001.md). [Tam senaryo tablosu](docs/scenario-status.md).

## Kurulum ve çalıştırma

Java 17+ ve Maven gerekir. Selenium/Karate UI testleri için Chrome kurulu olmalıdır. Playwright kendi Chromium sürümünü kurar. Komutları proje kökünden PowerShell'de çalıştırın. Her Maven projesi ayrı çalıştırılır.

| Amaç | Komut |
|---|---|
| Kök birim testleri | mvn clean test |
| Cucumber adım eşleşmesi | mvn test '-Dtest=EcommerceIT' '-Dcucumber.execution.dry-run=true' |
| Yerel Karate/Chrome smoke | mvn -f karate-ui/pom.xml test '-Dtest=SetupRunner' '-Dheadless=true' |
| API smoke | .\scripts\run-api.ps1 -Smoke |
| API regresyon | .\scripts\run-api.ps1 |
| İlk Playwright kurulumu ve smoke | .\scripts\run-playwright.ps1 -InstallBrowser |
| Playwright yerel smoke | .\scripts\run-playwright.ps1 |

Canlı Trendyol koşuları:

```powershell
mvn test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-002'
.\scripts\run-karate.ps1 -OpenReport
.\scripts\run-playwright.ps1 -Live
```

API ve Playwright scriptleri clean test çalıştırır, önceki modül çıktılarını temizler, rapor yolunu gösterir ve hata halinde başarısız çıkar. Canlı hata kanıtlarını sonraki clean koşusundan önce saklayın.

## API doğrulamaları

Testler 127.0.0.1 üzerinde her test için ayrı bir HTTP sunucusu açar. Veriler bellekte tutulur. API-001 listeleme, API-002 ürün getirme, API-003 ürün oluşturma ve kayıt kontrolü, API-004 geçersiz veri reddi ve kayıtların korunması, API-005 bulunamayan ürün yanıtını doğrular.

API-004, TestNG DataProvider ile altı geçersiz veri setini kullanır. TestNG XML dosyaları smoke ve regresyon gruplarını seçer. [API ayrıntıları ve Maven komutları](api-tests/README.md).

## CI ve raporlar

[GitHub Actions](https://github.com/yusufkg/Trendyol-Test-Automation/actions/workflows/ci.yml) push ve pull request olaylarında çalışır.

| Job | Standart kapsam | Artifact |
|---|---|---|
| verify | 11 birim testi, 7 Cucumber dry-run, yerel Karate smoke | test-reports |
| api-tests | Önce API smoke, ardından regresyon | api-test-reports |
| playwright-tests | Chromium kurulumu ve yerel smoke | playwright-test-reports |

Actions → Maven and Karate CI → Run workflow ekranındaki live_tests seçeneği canlı Selenium ve Karate senaryolarını, live_playwright seçeneği canlı Playwright TC-001'i açar. Her iki seçenek varsayılan olarak kapalıdır. Canlı test hataları job sonucuna yansır.

| Rapor | Yerel konum |
|---|---|
| Birim/Selenium Surefire XML | target/surefire-reports/ |
| CI Cucumber dry-run HTML | target/cucumber-step-mapping.html |
| CI canlı Selenium HTML | target/selenium-live.html |
| Karate HTML ve ekran görüntüleri | karate-ui/target/karate-reports/karate-summary.html |
| API regresyon XML/TestNG HTML | api-tests/target/surefire-reports/ |
| CI API smoke XML/TestNG HTML | api-tests/target/smoke-reports/ |
| Playwright Surefire XML | playwright-ui/target/surefire-reports/ |
| Playwright canlı HTTP, HTML ve PNG | playwright-ui/target/playwright-live/ |

CI raporları başarısızlıkta da toplanır ve 14 gün saklanır. HTML raporlarını destek dosyalarıyla birlikte çıkartın. Selenium başarısızlıklarında PNG rapora eklenir; Playwright canlı koşusunda HTTP durumu, URL, HTML ve PNG kaydı denenir. [Selenium hata raporlama davranışı](docs/evidence/failure-screenshots.md).

Pages yayını public depoda, başarılı standart main koşularında üç test job'ının sonucunu bekler. Canlı seçenek açıkken ve private depoda yayın atlanır. Pages yapılandırması Settings → Pages → Source → GitHub Actions üzerinden yapılır. [Yayın koşulları](docs/report-publication.md).

## Doğrulanmış sonuçlar

| Kayıt | Sonuç |
|---|---|
| [14 Kasım yerel toplu kontrol](docs/evidence/regression-2026-11-14.md) | 11 birim, 7 dry-run, API smoke 3, API regresyon 10, Karate 1 ve Playwright 1 geçti |
| [6 Kasım CI #73](docs/evidence/ci-2026-11-06.md) | 11 birim, 7 dry-run, API regresyon 10, Karate 1 ve Playwright 1 geçti |
| [2 Kasım canlı Selenium](docs/evidence/selenium-tc010.md) | TC-003/005/008/010 birlikte 4/4 geçti |
| [Playwright canlı TC-001](docs/evidence/playwright-tc001.md) | 9 Kasım HTTP 403; başarılı canlı koşu bekleniyor |

14 Kasım Playwright smoke testi ilk denemede eksik tarayıcı dosyası nedeniyle başlayamadı. -InstallBrowser ile kurulumdan sonra geçti. API smoke CI adımı ve live_playwright workflow seçeneğinin uzak çalıştırması ayrıca doğrulanmayı bekliyor.

Cucumber dry-run Gherkin–Java eşleşmesini kontrol eder. Yerel HTML tarayıcı testleri kontrollü örnekleri doğrular. Canlı site sonuçları tarih ve TC kimliğiyle belgelenir. [Tüm test kanıtları](docs/test-evidence.md).

## Mimari ve belgeler

```text
src/main/java/com/yusuf/qa/pricing/       Fiyat ayrıştırma
src/test/resources/features/            Selenium Gherkin kataloğu
src/test/java/com/yusuf/qa/runners/      Cucumber çalıştırıcısı
src/test/java/com/yusuf/qa/steps/        Gherkin → Java
src/test/java/com/yusuf/qa/pages/        Sayfa işlemleri ve XPath
src/test/java/com/yusuf/qa/support/      Oturum, Hooks, cleanup ve hata görüntüsü
karate-ui/src/test/java/                Karate feature, locator ve runner dosyaları
api-tests/src/test/                     Yerel API, RestAssured testleri ve TestNG suite
playwright-ui/src/test/java/            Yerel smoke ve canlı HomePageIT
scripts/                               Çalıştırıcılar ve rapor sitesi hazırlama
.github/workflows/ci.yml                CI ve rapor toplama
docs/                                  Mimari, plan, durum ve tarihli kanıtlar
```

Cucumber I click "XPath" adımını Java Step Definition üzerinden Selenium'a bağlar. Karate feature içinde click(), input(), waitFor() ve match kullanır. Playwright Java testleri locator(), fill(), click() ve assertion API'lerini kullanır.

[Mimari şema](docs/architecture.md) · [Test planı](docs/test-plani.md) · [Yol haritası](docs/roadmap.md) · [Gherkin senaryo tasarımları](docs/guest-test-scenarios/README.md)

Kökteki Dockerfile, src/WebSteps.java, specs/, env/default/ ve manifest.json eski Gauge çalışmasından kalmıştır. Güncel CI Maven projelerini kullanır. Güncel kapsam misafir akışlarıdır; SQL logger, Trendyol login ve Samsung S20 alışveriş akışı kaynakta bulunmaz.
