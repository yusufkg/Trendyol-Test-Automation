# Trendyol Test Automation

[![Maven and Karate CI](https://github.com/yusufkg/Trendyol-Test-Automation/actions/workflows/ci.yml/badge.svg)](https://github.com/yusufkg/Trendyol-Test-Automation/actions/workflows/ci.yml)

Java ile misafir e-ticaret akışlarını doğrulayan test otomasyon portföyü. Selenium/Cucumber ve Karate ayrı Maven projelerinde çalışır. Amaç, senaryo tasarımını, XPath kullanımını, bekleme stratejilerini ve test kanıtlarını görünür kılmaktır.

## Uygulanan kapsam

48 kimliğin framework bazındaki durumu: [Senaryo durum tablosu](docs/scenario-status.md). Aşamalı geliştirme planı: [Yol haritası](docs/roadmap.md).

| Senaryo | Selenium/Cucumber | Karate |
|---|---|---|
| TC-001: Ana sayfa kontrolleri | Uygulandı | Uygulandı |
| TC-002: Kategori sayfasından logoyla ana sayfaya dönüş | Uygulandı | Uygulandı |
| TC-003: Kategori menüsünden Elbise alt kategorisine geçiş | Planlı | Uygulandı |
| TC-005: Geçerli ürün arama | Planlı | Uygulandı |
| TC-024: Ürün detay doğrulaması | Uygulandı | Planlı |

48 senaryo, 48 başarılı otomasyon anlamına gelmez. Selenium kataloğunda 3 uygulanmış, 45 planlı senaryo vardır. Karate'de TC-001, TC-002, TC-003 ve TC-005 uygulanmıştır; diğer 44 senaryo planlıdır. `@planned` senaryolar varsayılan koşuya dahil edilmez.

Trendyol login ve Samsung S20 alışveriş akışı kapsamda değildir. Bu sürümde SQL logger ve SQL plugin bulunmamaktadır; eski sürümlere ait SQL sonuçları güncel özellik olarak sunulmaz.

## Teknolojiler

Bağımsız `api-tests/` projesinde RestAssured 6.1.0 ve TestNG 7.11.0 ile ürün listeleme, ürün detay ve POST oluşturma/GET kayıt kontrolü ve geçersiz POST testi uygulandı. 23 Ekim 2026 yerel koşusunda 4 test geçti. Kontrollü yerel API kullanır; mevcut CI/Pages kapsamına henüz dahil değildir. [API kaynakları ve kalan testler](api-tests/README.md).

```powershell
mvn -B -ntp -f api-tests/pom.xml clean test
```

Java 17+, Maven, Selenium WebDriver 4.27.0, Cucumber 7.20.1, JUnit 4; ayrı Karate 1.5.2 / JUnit 5 projesi ve Chrome. Bağımlılıklar ilgili POM dosyalarında sabitlenmiştir.

## Hızlı başlangıç

Proje kökünden PowerShell:

```powershell
# 5 birim testi; tarayıcı açmaz
mvn clean test

# Aktif 3 Selenium senaryosunun adım eşleşmesi; canlı test değildir
mvn test '-Dtest=EcommerceIT' '-Dcucumber.execution.dry-run=true'

# Yerel örnek sayfada headless Chrome; Trendyol'a bağlanmaz
mvn -f karate-ui/pom.xml test '-Dtest=SetupRunner' '-Dheadless=true'

# 4 Karate senaryosu; canlı Trendyol
.\scripts\run-karate.ps1 -OpenReport

# Selenium TC-002; canlı Trendyol
mvn test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-002'
```

## CI ve raporları inceleme

[GitHub Actions](https://github.com/yusufkg/Trendyol-Test-Automation/actions/workflows/ci.yml), push ve pull request ile çalışır. Sıra: **birim testleri → Cucumber dry-run → yerel HTML fixture üzerinde headless Karate/Chrome testi**. Bu CI sonucu tek başına canlı Trendyol senaryolarının geçtiğini göstermez.

Canlı testler için **Actions → Maven and Karate CI → Run workflow → live_tests** seçeneğini açın. Bu seçenek Selenium ve Karate'nin uygulanmış Trendyol senaryolarını ayrıca çalıştırır. Site değişikliği, ağ veya güvenlik engeli hataları başarıya çevrilmez.

Bir koşuyu açın, adımların sonuçlarını inceleyin ve alttaki **Artifacts → test-reports** dosyasını indirin. HTML raporlarını tüm destek dosyalarıyla birlikte çıkartın:

| Rapor | Konum ve anlamı |
|---|---|
| Birim testleri | `target/surefire-reports/TEST-*.xml` |
| Cucumber adım eşleşmesi | `target/cucumber-step-mapping.html`; dry-run |
| Karate tarayıcı testi | `karate-ui/target/karate-reports/karate-summary.html` |
| Canlı Selenium, elle seçildiyse | `target/selenium-live.html` |

Raporlar başarısızlıkta da toplanır ve 14 gün saklanır. Badge gerçek Actions durumunu okur; sabit bir başarı rozeti değildir. Eski yerel kayıtlar yerine incelenen koşunun commit SHA ve tarihini esas alın.

## Mimari ve kaynak kod

GitHub Pages rapor yayımlaması, `67192df` commit'inin [başarılı ana dal koşusunda](https://github.com/yusufkg/Trendyol-Test-Automation/actions/runs/39100021848) doğrulandı. [Rapor sitesi](https://yusufkg.github.io/Trendyol-Test-Automation/) yalnızca main dalındaki başarılı standart CI koşularının raporlarını yayımlar; canlı test raporlarını yayımlamaz. Bu, test raporlarının otomatik dağıtımıdır; Trendyol uygulamasının production dağıtımı değildir. Ayrıntılar: [Rapor yayımlama](docs/report-publication.md).

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
.github/workflows/ci.yml                          CI akışı ve rapor artifactleri
```

Cucumber'da `I click "XPath"` ortak Java adımıyla çalışır. Karate'de doğrudan `click()`, `input()`, `waitFor()` ve `match` kullanılır; Java Step Definition gerekmez. Her senaryoda yeni Chrome oturumu kullanılır ve sonunda kapatılır.

## Doğrulama durumu

17 Ekim 2026 tarihli yerel kontrol: 5 birim testi ve 3 Cucumber dry-run senaryosu geçti. Canlı Trendyol testleri bu kontrolün parçası değildir. CI'nin uzaktaki gerçek sonucu yukarıdaki Actions bağlantısından takip edilir; yerel bir kontrol uzak CI başarısı olarak gösterilmez.

Karate smoke ve CI kanıtı: [doğrulama kaydı](docs/test-evidence.md).

19 Ekim 2026 yerel canlı Chrome regresyonunda Karate TC-001, TC-002, TC-003 ve TC-005 birlikte geçti: 4 test, 0 hata. Bu sonuç henüz bu değişikliğin uzak CI veya Pages sonucu değildir.
