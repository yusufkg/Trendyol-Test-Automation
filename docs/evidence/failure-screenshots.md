# Selenium hata ekran görüntüsü — 3 Kasım 2026

Başarısız Cucumber senaryosunda Hooks, tarayıcı kapanmadan önce PNG görüntüsünü failure-screenshot adı ve image/png türüyle rapora ekler. Başarılı senaryoda görüntü alınmaz. ScenarioCleanup ekran görüntüsü, attachment ve tarayıcı kapatmayı yönetir; Hooks finally bloğu ThreadLocal oturumunu temizler.

Görüntü alma veya rapora ekleme başarısızsa hata sınıfı scenario.log ile kaydedilir ve quit yine denenir. Zaten başarısız senaryoda quit hatası asıl hatayı değiştirmez; başarılı senaryonun quit hatası ise yükseltilir. Tarayıcı tamamen kapanmışsa görüntü alınacağı garanti edilmez.

## Doğrulama

- ScenarioCleanupTest: 6 test geçti. Capture/attach/quit sırası, başarılı senaryoda capture yapılmaması, capture/attachment hatasında cleanup ve quit hata politikası test edildi. Bu testler kontrollü WebDriver çiftleri kullanır.
- Tüm kök birim testleri: 11 geçti (önceki 5 ve yeni 6), 0 hata.
- Gerçek Chrome 156 üzerinde boş sayfada kasıtlı geçersiz XPath ile tek tanılama senaryosu çalıştırıldı. Beklendiği gibi InvalidSelectorException ve BUILD FAILURE oluştu. Bu bir uygulama kusuru veya başarılı UI senaryosu olarak sunulmaz.
- target/failure-evidence.json içindeki After hook ekinde bir image/png, failure-screenshot kaydı doğrulandı. Base64 çözüldü: 12.799 byte ve geçerli PNG imzası.
- target/failure-evidence.html içinde aynı ek doğrulandı.
- Ardından 7 aktif senaryonun dry-run kontrolü 7/7 geçti.

Tanılama feature dosyası ve HTML/JSON çıktıları yalnızca ignored target dizinindedir; 48 senaryoluk kataloğa eklenmedi. Canlı Trendyol senaryoları, API ve Karate bu değişiklik için yeniden çalıştırılmadı. Yeni kodun uzak CI sonucu henüz doğrulanmadı.

## Kullanım

```powershell
mvn test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-010' '-Dcucumber.plugin=pretty,html:target/selenium-live.html,json:target/selenium-live.json'
```

Senaryo başarısızsa HTML raporundaki After hook altında failure-screenshot ekini açın. JSON dosyası PNG'yi base64 olarak taşır. CI'nin isteğe bağlı canlı Selenium koşusunda bu dosyalar test-reports artifact içinde tutulur; canlı raporlar Pages'e yayımlanmaz.
