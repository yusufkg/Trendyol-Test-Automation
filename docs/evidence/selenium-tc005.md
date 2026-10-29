# Selenium TC-005 doğrulaması

29 Ekim 2026 yerel Chrome 156, Java 17 / Selenium 4.27.0 koşusu.

TC-005, misafir oturumunda kablosuz mouse aramasını gerçekleştirir. Arama sonuç yolu, URL içindeki q parametresi, görünür başlık/ürün kartları ve ilk beş görünür ürün başlığının alaka ölçütü doğrulanır. Ölçüt mouse, fare, faresi, fareler ve fareleri kelimelerini kabul eder; qa.search.relevance sistem özelliğiyle değiştirilebilir. Tüm sonuç kümesi taranmaz.

```powershell
mvn -B -ntp test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-005' '-Dcucumber.plugin=pretty,html:target/tc005-live.html,json:target/tc005-live.json'
```

Sonuç: 1 senaryo, 0 başarısızlık, 0 hata, 0 atlanan; BUILD SUCCESS. Senaryo süresi 21 saniye, Maven toplamı 26,570 saniye. HTML ve JSON dosyaları target altında üretilir; Git'e eklenmez.

İlk koşu, ilgili bir ürünün başlığındaki Faresi kelimesini yalnızca fare kabul eden ölçüt nedeniyle reddetti. Türkçe ekli biçimler kapsandıktan sonra senaryo yeniden çalıştırıldı ve geçti. Chrome CDP sürüm uyarısı görüldü; standart WebDriver adımları tamamlandı.

Bu yerel ve canlı Trendyol testidir; dry-run veya uzak CI sonucu değildir. Site sonuçları zamanla değişebilir. TC-001, TC-002 ve TC-024 bu canlı koşuda yeniden çalıştırılmadı. Karate ve API projeleri bu değişiklik için yeniden çalıştırılmadı.

Ek kontroller: kök projenin 5 birim testi geçti; TC-001, TC-002, TC-005 ve TC-024 için Cucumber dry-run tamamlandı (4 senaryo, 0 hata). Dry-run canlı tarayıcı çalıştırmaz.
