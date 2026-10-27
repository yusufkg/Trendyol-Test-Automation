# Aşamalı geliştirme planı

## Aşama 1 — Mevcut kanıtları tutarlı tut

- [x] Kaynaktaki 48 TC kimliğini ve @planned etiketlerini say; Selenium 3/45, Karate 4/44 durumunu belgelerle eşitle.
- [x] Eski Gauge/Docker kullanımını kontrol et ve güncel akıştan ayrımını açıkla.
- [x] Mevcut geliştirme commit'inin GitHub Actions sonuçlarını gerçek job adımlarından doğrula.

Kaynak durum tablosu: scenario-status.md. 9334d0a commit'inin PR koşusu #11 başarılıdır. Bu koşuda canlı Trendyol testleri çalışmadı. Birim testi, dry-run ve yerel Chrome kanıtları ayrı değerlendirilir.

Güncel Maven projeleri Gauge kullanmaz. src/WebSteps.java, specs, env/default ve manifest.json eski Gauge yapısındandır; ana Maven kaynak/test yolları dışında veya kullanılmayan yapılandırmalardır. Dockerfile da eski Gauge ortamına aittir; mevcut CI Dockerfile'ı kullanmaz. Docker çalışması doğrulanmış gibi gösterilmez. Bu denetimde dosyalar silinmedi; ayrı bir temizlik değişikliğinde taşınabilir.

## Aşama 2 — Bağımsız API otomasyonu

- [x] api-tests/pom.xml ve modül kaynaklarını oluştur.
- [x] RestAssured ile kontrollü yerel API kur.
- [x] GET/POST/negatif kapsamlı en az beş kontrat testi yaz.
- [x] API testlerinin CI job ve artifact yapılandırmasını uzak başarılı koşuyla doğrula.

Detaylar: ../api-tests/README.md. API-001 GET liste, API-002 GET ürün detay ve API-003 POST oluşturma/GET kayıt kontrolü uygulandı. API-004 negatif fiyat reddi ve kayıt oluşmaması kontrolü de eklendi. API-005 bulunamayan ürün testi de tamamlandı. 24 Ekim 2026 yerel Maven koşusunda beşi birlikte geçti (5 test, 0 hata). 26 Ekim 2026: bağımsız api-tests job ve api-test-reports artifact yapılandırması eklendi. Aynı Maven komutu yerelde 5 test geçti. 27 Ekim kontrolünde CI koşusu 40276213865 için API job ve artifact yüklemesi başarılı olarak doğrulandı. Aynı koşuda Pages başarısızdır; tüm workflow başarılı olarak sunulmaz.

## Aşama 3 — TestNG

API projesinde @Test, @BeforeMethod, @AfterMethod ve @DataProvider uygulandı. API-004 altı veriyle çalışır; 27 Ekim yerel koşusunda toplam 10 çalıştırma geçti. Bu yeni kapsamın uzak CI doğrulaması bekliyor. Grup seçimi henüz eklenmedi.

- [x] Ayrı API projesinde @Test, @BeforeMethod, @AfterMethod ve @DataProvider kullan.
- [ ] smoke/regression gruplarını ve testng.xml seçimini doğrula.

Mevcut JUnit/Selenium/Karate projeleri TestNG'ye dönüştürülmez.

## Aşama 4 — Kapsam ve portföy

- [ ] Seçilen framework'te 3–5 yeni kararlı UI senaryosu tamamla.
- [ ] Yeni başarılı koşular için README kanıtlarını güncelle.
- [ ] Yalnızca tamamlanmış çalışmaları CV ve LinkedIn'e ekle.

Bir PR tek bir somut çıktıya odaklanır. Dört haftalık takvim hedeftir; kesin teslim tarihi değildir. Uygulanmamış framework veya senaryo portföyde tamamlanmış olarak gösterilmez.
