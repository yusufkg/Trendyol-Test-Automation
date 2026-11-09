# Trendyol Test Automation

[![Maven and Karate CI](https://github.com/yusufkg/Trendyol-Test-Automation/actions/workflows/ci.yml/badge.svg)](https://github.com/yusufkg/Trendyol-Test-Automation/actions/workflows/ci.yml)

Java ile misafir e-ticaret akışlarını ve kontrollü bir örnek API'yi doğrulayan test otomasyon portföyü. Selenium/Cucumber, Karate ve RestAssured/TestNG üç bağımsız Maven projesinde çalışır. Proje; senaryo tasarımı, XPath, Page Object Model, HTTP/JSON doğrulamaları ve GitHub Actions raporlamasını kapsar.

## Uygulanan kapsam

48 kimliğin framework bazındaki durumu: [Senaryo durum tablosu](docs/scenario-status.md). Aşamalı geliştirme planı: [Yol haritası](docs/roadmap.md).

| Senaryo | Selenium/Cucumber | Karate |
|---|---|---|
| TC-001: Ana sayfa kontrolleri | Uygulandı | Uygulandı |
| TC-002: Kategori sayfasından logoyla ana sayfaya dönüş | Uygulandı | Uygulandı |
| TC-003: Kategori menüsünden Elbise alt kategorisine geçiş | Uygulandı | Uygulandı |
| TC-005: Geçerli ürün arama | Uygulandı | Uygulandı |
| TC-008: Sonuç bulunamayan arama | Uygulandı | Planlı |
| TC-010: Arama sorgusunu değiştirme | Uygulandı | Planlı |
| TC-024: Ürün detay doğrulaması | Uygulandı | Planlı |

48 senaryo, 48 başarılı otomasyon anlamına gelmez. Selenium kataloğunda 7 uygulanmış, 41 planlı senaryo vardır. Karate'de TC-001, TC-002, TC-003 ve TC-005 uygulanmıştır; diğer 44 senaryo planlıdır. `@planned` senaryolar varsayılan koşuya dahil edilmez.

Trendyol login ve Samsung S20 alışveriş akışı kapsamda değildir. Bu sürümde SQL logger ve SQL plugin bulunmamaktadır; eski sürümlere ait SQL sonuçları güncel özellik olarak sunulmaz.

## Teknolojiler

| Proje | Teknolojiler | Kapsam |
|---|---|---|
| Kök Maven projesi | Java 17+, Selenium 4.27.0, Cucumber 7.20.1, JUnit 4 | 11 birim testi ve 7 uygulanmış UI senaryosu |
| `karate-ui/` | Karate 1.5.2, JUnit 5, Chrome | 4 uygulanmış UI senaryosu ve yerel tarayıcı fixture testi |
| `api-tests/` | RestAssured 6.1.0, TestNG 7.11.0 | 5 API senaryosu, DataProvider ile 10 çalıştırma |

Bağımlılıklar ilgili POM dosyalarında sabitlenmiştir. Kök dizindeki `mvn test` diğer iki Maven projesini otomatik çalıştırmaz.

## API test kapsamı

| Kimlik | İstek | Doğrulamalar |
|---|---|---|
| API-001 | GET `/products` | 200, JSON, toplam, liste boyutu ve ürün alanları |
| API-002 | GET `/products/1` | 200, ürün kimliği, adı ve fiyatı |
| API-003 | POST `/products` | 201; oluşturulan ürünü GET ve liste üzerinden doğrulama |
| API-004 | Geçersiz POST `/products` | 400, hata gövdesi ve kayıtların değişmemesi |
| API-005 | GET `/products/999` | 404, tam JSON hata gövdesi ve mevcut ürüne erişim |

Her test `127.0.0.1` üzerinde boş bir portta kendi HTTP sunucusunu açar ve sonunda kapatır. Veriler yalnızca test süresince bellekte tutulur. Bu testler gerçek Trendyol API'sini doğrulamaz. TestNG `@BeforeMethod` / `@AfterMethod` ile yaşam döngüsü yönetilir; API-004, DataProvider ile altı geçersiz ürün verisi için çalışır. Smoke grubu API-001/002/003 seçer (3 çalıştırma); varsayılan regression tüm kapsamı seçer (10 çalıştırma). [API projesi](api-tests/README.md).

28 Ekim 2026 yerel regression sonucu: **10 test çalıştırması, 0 başarısızlık, 0 hata, 0 atlanan**.

```powershell
mvn -B -ntp -f api-tests/pom.xml clean test
```

## Hızlı başlangıç

Java 17 veya üzeri ve Maven gerekir; UI testleri için Chrome da kurulu olmalıdır. Komutları proje kökünden PowerShell'de çalıştırın.

Kök projenin 11 birim testi:

```powershell
mvn clean test
```

Aktif 7 Selenium senaryosunun adım eşleşmesi; tarayıcı çalıştırılmaz:

```powershell
mvn test '-Dtest=EcommerceIT' '-Dcucumber.execution.dry-run=true'
```

Yerel HTML örneğinde headless Chrome:

```powershell
mvn -f karate-ui/pom.xml test '-Dtest=SetupRunner' '-Dheadless=true'
```

Canlı Trendyol üzerinde 4 Karate senaryosu:

```powershell
.\scripts\run-karate.ps1 -OpenReport
```

Canlı Trendyol üzerinde yalnızca Selenium TC-002:

```powershell
mvn test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-002'
```

## CI ve raporları inceleme

[GitHub Actions](https://github.com/yusufkg/Trendyol-Test-Automation/actions/workflows/ci.yml), push ve pull request ile çalışır. Sıra: **birim testleri → Cucumber dry-run → yerel HTML fixture üzerinde headless Karate/Chrome testi**. Ayrı RestAssured API tests job yerel örnek API testlerini çalıştıracak ve api-test-reports artifact üretecek şekilde yapılandırılmıştır. Her iki job bağımsız çalışır; Pages yayımlaması ikisinin de başarısını bekler. Bu CI sonucu tek başına canlı Trendyol senaryolarının geçtiğini göstermez.

Canlı testler için **Actions → Maven and Karate CI → Run workflow → live_tests** seçeneğini açın. Bu seçenek Selenium ve Karate'nin uygulanmış Trendyol senaryolarını ayrıca çalıştırır. Site değişikliği, ağ veya güvenlik engeli hataları başarıya çevrilmez.

26 Ekim tarihli [CI koşusunda](https://github.com/yusufkg/Trendyol-Test-Automation/actions/runs/40276213865) API job ve api-test-reports artifact yüklemesi başarılıdır. Aynı koşunun Pages job başarısızlığı ayrı bir yayımlama sorunudur. DataProvider ve regression XML kapsamı da [CI #55](https://github.com/yusufkg/Trendyol-Test-Automation/actions/runs/41204004405) job logunda 10 başarılı çalıştırma ile doğrulandı.

Bir koşuyu açın, adımların sonuçlarını inceleyin. **Artifacts → test-reports** UI/birim raporlarını; **api-test-reports** API sonuçlarını içerir. HTML raporlarını tüm destek dosyalarıyla birlikte çıkartın:

| Rapor | Konum ve anlamı |
|---|---|
| Birim testleri | `target/surefire-reports/TEST-*.xml` |
| API testleri | `api-tests/target/surefire-reports/TEST-*.xml` ve `emailable-report.html`; ayrı `api-test-reports` artifact |
| Cucumber adım eşleşmesi | `target/cucumber-step-mapping.html`; dry-run |
| Karate tarayıcı testi | `karate-ui/target/karate-reports/karate-summary.html` |
| Canlı Selenium, elle seçildiyse | `target/selenium-live.html` |

Raporlar başarısızlıkta da toplanır ve 14 gün saklanır. Badge gerçek Actions durumunu okur; sabit bir başarı rozeti değildir. Eski yerel kayıtlar yerine incelenen koşunun commit SHA ve tarihini esas alın.

Başarısız Selenium senaryolarında tarayıcı kapanmadan önce PNG, failure-screenshot adıyla Cucumber HTML/JSON raporuna eklenir. Görüntü alınamasa bile cleanup denenir ve asıl senaryo hatası korunur. [Davranış, test kanıtı ve kullanım](docs/evidence/failure-screenshots.md).

## Mimari ve kaynak kod

GitHub Pages yayını [18 Ekim tarihli koşuda](https://github.com/yusufkg/Trendyol-Test-Automation/actions/runs/39100021848) doğrulanmış tarihsel bir sonuçtur. Güncel workflow private depoda Pages hazırlama/yayımlama adımlarını atlar; artifact raporlaması devam eder. Public depoda Settings → Pages → Source → GitHub Actions ayarı gerekir. Yalnızca başarılı standart main koşuları yayımlanır; API ve isteğe bağlı canlı UI raporları artifact olarak kalır. Bu, raporların otomatik yayımlanmasıdır; Trendyol uygulamasının production dağıtımı değildir. [Pages ayarları](docs/report-publication.md).

[Mimari açıklaması ve şema](docs/architecture.md).

```text
src/test/resources/features/e-commerce.feature    Selenium senaryoları
src/test/java/com/yusuf/qa/runners/                Cucumber runner
src/test/java/com/yusuf/qa/steps/                  Gherkin → Java adımları
src/test/java/com/yusuf/qa/pages/                  Sayfa işlemleri ve XPath
src/test/java/com/yusuf/qa/support/                Chrome yaşam döngüsü
karate-ui/src/test/java/navigation/               Karate TC-001, TC-002, TC-003, TC-005
karate-ui/src/test/java/common/locators.json       Ortak Karate XPath seçicileri
karate-ui/src/test/java/setup/                    Dış servise bağlı olmayan örnek
api-tests/pom.xml                                Bağımsız RestAssured/TestNG projesi
api-tests/src/test/java/com/yusuf/qa/api/fixture/  Yerel HTTP sunucusu
api-tests/src/test/java/com/yusuf/qa/api/tests/    API-001–API-005 doğrulamaları
.github/workflows/ci.yml                          CI akışı ve rapor artifactleri
```

Cucumber'da `I click "XPath"` ortak Java adımıyla çalışır. Karate'de doğrudan `click()`, `input()`, `waitFor()` ve `match` kullanılır; Java Step Definition gerekmez. Her senaryoda yeni Chrome oturumu kullanılır ve sonunda kapatılır.

## Doğrulama durumu

3 Kasım 2026: hata ekran görüntüsü/cleanup için 6 yeni test eklendi; kök birim testleri 11/11, dry-run 7/7 geçti. Boş gerçek Chrome sayfasındaki kasıtlı hata ile PNG'nin Cucumber JSON/HTML raporuna eklendiği doğrulandı. Bu tanılama canlı Trendyol testi değildir; uzak CI sonucu henüz doğrulanmadı.

2 Kasım 2026: TC-010 sorgu değiştirme canlı Chrome koşusunda geçti. Ortak erişim ve kategori başlığı görünürlük kontrolleri düzeltildikten sonra TC-003/005/008/010 birlikte 4/4 geçti. Ayrıca 5 birim testi ve 7 senaryonun dry-run kontrolü başarılı. Katalog 7 uygulanmış / 41 planlıdır. İlk başarısız koşular ve düzeltmeler: [TC-010 test kaydı](docs/evidence/selenium-tc010.md).

1 Kasım 2026: TC-003, TC-005 ve TC-008 aynı yerel canlı Selenium regresyonunda geçti (3/3). API smoke 3/3, regression 10/10 tekrar geçti. [Yerel regresyon kaydı](docs/evidence/regression-2026-11-01.md). Kaynak e7d630b için [uzak CI #55](docs/evidence/ci-2026-10-31.md) de başarılıdır; uzak koşuda canlı Trendyol adımları çalıştırılmadı.

31 Ekim 2026: Selenium TC-008 canlı Chrome koşusunda geçti (1 senaryo, 0 hata). Katalog 6 uygulanmış / 42 planlıdır. Sonuçsuz aramada öneriler, açık öneri mesajı ve görünür başlıklarda sorgu yokluğu ile doğrulanır. 5 birim testi ve 6 senaryonun dry-run kontrolü de geçti. [TC-008 test kaydı](docs/evidence/selenium-tc008.md).

30 Ekim 2026: Selenium TC-003 canlı Chrome koşusunda geçti (1 senaryo, 0 hata). Güncel Selenium kataloğu 5 uygulanmış / 43 planlıdır. [TC-003 test kaydı](docs/evidence/selenium-tc003.md).

29 Ekim 2026: Selenium TC-005 canlı Chrome koşusunda geçti (1 senaryo, 0 hata). Selenium kataloğu 4 uygulanmış / 44 planlıdır. Diğer üç Selenium senaryosunun canlı sonuçları bu koşuda yenilenmedi. [TC-005 komutu, ölçütü ve test kaydı](docs/evidence/selenium-tc005.md).

17 Ekim 2026 tarihli yerel kontrol: 5 birim testi ve 3 Cucumber dry-run senaryosu geçti. Canlı Trendyol testleri bu kontrolün parçası değildir. CI'nin uzaktaki gerçek sonucu yukarıdaki Actions bağlantısından takip edilir; yerel bir kontrol uzak CI başarısı olarak gösterilmez.

Karate smoke ve CI kanıtı: [doğrulama kaydı](docs/test-evidence.md).

19 Ekim 2026 yerel canlı Chrome regresyonunda Karate TC-001, TC-002, TC-003 ve TC-005 birlikte geçti: 4 test, 0 hata. Bu sonuç henüz bu değişikliğin uzak CI veya Pages sonucu değildir.

26 Ekim 2026 yerel API kontrolünde API-001–API-005 birlikte geçti. YAML sözdizimi, API çalıştırma komutu, başarısızlıkta artifact yükleme koşulu ve Pages job bağımlılığı yerelde kontrol edildi. 27 Ekim kontrolünde uzak API job başarısı ve artifact kaydı doğrulandı; artifact içeriği indirilmedi. DataProvider ile 10 çalıştırma yerelde geçti; bu kapsam 1 Kasım kontrolünde CI #55 üzerinden de doğrulandı.

## API smoke seçimi

```powershell
mvn -B -ntp -f api-tests/pom.xml clean test '-Dqa.suite=src/test/resources/testng-smoke.xml'
```

28 Ekim yerel doğrulama: smoke 3/3 ve varsayılan regression 10/10 başarılı. Suite dosyaları api-tests/src/test/resources/testng-smoke.xml ve testng.xml içindedir. CI mevcut komutuyla regression suite kullanır. Regression suite için CI #55 başarılıdır; smoke XML seçimi yalnızca yerelde doğrulandı.

## Playwright UI

`playwright-ui/` bağımsız bir Java 17 Maven projesidir. Smoke testi headless Chromium'da yerel bir HTML örneğini açar, XPath ile arama alanına metin girer, düğmeye tıklar ve sonucu doğrular.

```powershell
mvn -f playwright-ui/pom.xml exec:java '-Dexec.classpathScope=test' '-Dexec.mainClass=com.microsoft.playwright.CLI' '-Dexec.args=install --only-shell chromium'
mvn -f playwright-ui/pom.xml test
```

Surefire raporları `playwright-ui/target/surefire-reports/` altında oluşur. Bu test yerelde ayrı komutla ve GitHub Actions içinde Playwright local browser smoke job'ıyla çalışır. Chromium headless shell ve Linux bağımlılıkları testten önce kurulur. Surefire sonuçları başarısızlıkta da playwright-test-reports artifact'ine yüklenir ve 14 gün tutulur. Pages yayını Playwright job'ının da başarılı olmasını bekler.

7 Kasım 2026: Son CI kanıtı job loglarından doğrulandı. 28a0a67 commit'inin PR koşusunda 11 birim testi, 7 Cucumber dry-run senaryosu, 1 yerel Karate Chrome testi, 10 API regresyon çalıştırması ve 1 Playwright Chromium testi geçti. Üç rapor artifact'i kontrol edildi. İlk push koşusundaki Chrome başlangıç hatası ve başarılı tekrar da kayıtta yer alır. [CI doğrulama kaydı](docs/evidence/ci-2026-11-06.md).

### Playwright TC-001 canlı ana sayfa kontrolü

TC-001 uygulaması HomePageIT.java içindedir. Canlı koşu komutu:

```powershell
mvn -f playwright-ui/pom.xml test '-Dtest=HomePageIT'
```

URL, logo, arama alanı, kategori bağlantıları ve uygulama hata mesajları kontrol edilir. Hata anındaki HTTP durumu, URL, HTML ve PNG, playwright-ui/target/playwright-live/ altında tutulur.

8 Kasım yerel koşusunda sayfa boş HTML döndürdü ve logo doğrulaması başarısız oldu. Canlı TC-001 başarıyla doğrulanmayı bekliyor. Varsayılan Maven ve CI smoke koşusu SearchSmokeTest sınıfını çalıştırır. [Koşu kaydı](docs/evidence/playwright-tc001.md).

9 Kasım Playwright TC-001 incelemesinde canlı ana sayfa HTTP 403 döndürdü. Test HTTP durumunu doğrular ve hata halinde navigation.txt, HTML ve PNG kayıtları üretir. [Güncel tanılama kaydı](docs/evidence/playwright-tc001.md).
