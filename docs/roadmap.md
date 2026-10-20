# Aşamalı geliştirme planı

## Aşama 1 — Mevcut kanıtları tutarlı tut

- [x] Kaynaktaki 48 TC kimliğini ve @planned etiketlerini say; Selenium 3/45, Karate 4/44 durumunu belgelerle eşitle.
- [x] Eski Gauge/Docker kullanımını kontrol et ve güncel akıştan ayrımını açıkla.
- [x] Mevcut geliştirme commit'inin GitHub Actions sonuçlarını gerçek job adımlarından doğrula.

Kaynak durum tablosu: scenario-status.md. 9334d0a commit'inin PR koşusu #11 başarılıdır. Bu koşuda canlı Trendyol testleri çalışmadı. Birim testi, dry-run ve yerel Chrome kanıtları ayrı değerlendirilir.

Güncel Maven projeleri Gauge kullanmaz. src/WebSteps.java, specs, env/default ve manifest.json eski Gauge yapısındandır; ana Maven kaynak/test yolları dışında veya kullanılmayan yapılandırmalardır. Dockerfile da eski Gauge ortamına aittir; mevcut CI Dockerfile'ı kullanmaz. Docker çalışması doğrulanmış gibi gösterilmez. Bu denetimde dosyalar silinmedi; ayrı bir temizlik değişikliğinde taşınabilir.

## Aşama 2 — Bağımsız API otomasyonu

- [ ] api-tests/pom.xml ve modül kaynaklarını oluştur.
- [ ] RestAssured ile kontrollü yerel API kur.
- [ ] GET/POST/negatif kapsamlı en az beş kontrat testi yaz.
- [ ] API testlerini CI'a bağla ve artifact üret.

Tasarım: ../api-tests/README.md. Şu an yalnızca tasarım belgesi vardır; API testi veya çalıştırılabilir Maven modülü henüz yoktur.

## Aşama 3 — TestNG

- [ ] Ayrı API projesinde @Test, @BeforeMethod, @AfterMethod ve @DataProvider kullan.
- [ ] smoke/regression gruplarını ve testng.xml seçimini doğrula.

Mevcut JUnit/Selenium/Karate projeleri TestNG'ye dönüştürülmez.

## Aşama 4 — Kapsam ve portföy

- [ ] Seçilen framework'te 3–5 yeni kararlı UI senaryosu tamamla.
- [ ] Yeni başarılı koşular için README kanıtlarını güncelle.
- [ ] Yalnızca tamamlanmış çalışmaları CV ve LinkedIn'e ekle.

Bir PR tek bir somut çıktıya odaklanır. Dört haftalık takvim hedeftir; kesin teslim tarihi değildir. Uygulanmamış framework veya senaryo portföyde tamamlanmış olarak gösterilmez.
