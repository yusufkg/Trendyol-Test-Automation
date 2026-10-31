# Selenium TC-008 doğrulaması

31 Ekim 2026 yerel Chrome 156.0.8078.74, Java 17 / Selenium 4.27.0 koşusu.

```powershell
mvn -B -ntp test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-008' '-Dcucumber.plugin=pretty,html:target/tc008-live.html,json:target/tc008-live.json'
```

Veri: qazxvbnmtesturun9473628105. Gerekirse qa.search.noResults ile değiştirilir. URL sorgusu birebir doğrulanır; ürün/sonuç bulunamadı mesajı beklenir. Veri gelecekte ürün döndürürse test başarısız olabilir.

Site, Aradığın ürün bulunamadı. Aşağıdakiler ilgini çekebilir. açıklamasıyla aynı kapsayıcıda öneriler gösterdi. Kartlar varsa bu açıklama görünmeli, her görünür kartta ürün başlığı bulunmalı ve hiçbir başlık sorguyu içermemelidir. Bu UI kontrolü, tüm katalogda ürün bulunmadığını veya sunucudaki toplamın sıfır olduğunu kanıtlamaz. Görünen uygulama hataları ayrıca kontrol edilir.

İlk canlı koşu genel kart yokluğu kontrolünde zaman aşımına uğradı. DOM incelemesi öneri durumunu doğruladı; geçici tanılama kodu kaldırıldı ve senaryo yeniden çalıştırıldı.

Son koşu: 1 senaryo, 0 başarısızlık, 0 hata, 0 atlanan; BUILD SUCCESS. Senaryo 48,78 saniye, Maven toplamı 54,330 saniye. Raporlar target altında tutulur; Git'e eklenmez. Chrome CDP uyarısına rağmen standart WebDriver adımları tamamlandı.

Ek kontroller: 5 birim testi ve 6 aktif Cucumber senaryosunun dry-run kontrolü geçti. Dry-run tarayıcı çalıştırmaz. Diğer canlı UI senaryoları ve API/Karate bu değişiklik için tekrar çalıştırılmadı. Uzak CI sonucu henüz doğrulanmadı.
