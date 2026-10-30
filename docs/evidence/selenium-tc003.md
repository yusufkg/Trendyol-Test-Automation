# Selenium TC-003 doğrulaması

30 Ekim 2026 yerel Chrome 156.0.8078.74 ve Selenium 4.27.0 koşusu.

TC-003 misafir oturumunda kategori menüsünü açar. aria-expanded=true durumu, görünür Elbise bağlantısı, bağlantının /elbise-x-c56 yolu, hedef sayfanın Trendyol alan adı/yolu, Elbise başlığı ve görünür ürün kartları doğrulanır. Veri Elbise kategorisiyle sınırlıdır; tüm kategoriler veya Hepsiburada için başarı iddiası yoktur.

```powershell
mvn -B -ntp test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-003' '-Dcucumber.plugin=pretty,html:target/tc003-live.html,json:target/tc003-live.json'
```

Sonuç: 1 senaryo, 0 başarısızlık, 0 hata, 0 atlanan; BUILD SUCCESS. Senaryo 39,74 saniye, toplam Maven süresi 45,370 saniye. Chrome CDP sürüm uyarısı görüldü; standart WebDriver adımları tamamlandı.

Kaynaklar: CategoryMenuPage.java XPath ve sayfa işlemlerini, NavigationSteps.java Cucumber eşleşmelerini içerir. HTML ve JSON çıktıları target altında üretilir ve Git'e eklenmez.

Bu yerel canlı koşudur; uzak CI veya tüm canlı regresyon sonucu değildir. TC-003 dry-run da geçti. Diğer Selenium, Karate ve API canlı senaryoları bu koşuda çalıştırılmadı.

Ek kontroller: mevcut 5 birim testi ve tüm 5 aktif Cucumber senaryosunun dry-run kontrolü geçti (0 hata). Dry-run canlı tarayıcı çalıştırmaz.
