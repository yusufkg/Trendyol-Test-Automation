# Selenium TC-010 doğrulaması

2 Kasım 2026 yerel Chrome 156.0.8078.74, Java 17 / Selenium 4.27.0 koşusu.

```powershell
mvn -B -ntp test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-010' '-Dcucumber.plugin=pretty,html:target/tc010-live.html,json:target/tc010-live.json'
```

Senaryo önce mouse aramasını doğrular, ardından aynı alanda metni klavye ile değiştirip Enter'a basar. URL'deki q parametresi birebir, yeniden açılan arama alanının değeri ve görünür h1 başlığında yeni sorgu doğrulanır. h1 başlıklarında eski sorgu kalmamalıdır. İlk beş görünür ürün başlığı klavye/klavyesi/klavyeler/klavyeleri veya keyboard ölçütüne uymalıdır. Ölçüt qa.search.replacementRelevance ile değiştirilebilir. Ortak ürünlerin iki aramada da görünmesi tek başına hata değildir; kontrol eski arama durumunun kalmamasıdır.

Son tek-senaryo koşusu: 1 test, 0 başarısızlık, 0 hata, 0 atlanan; BUILD SUCCESS. Senaryo 17,24 saniye, Maven toplamı 22,385 saniye. HTML/JSON target altında tutulur ve Git'e eklenmez.

İlk koşu son durum doğrulamasında zaman aşımına uğradı; değer kontrolünden önce arama alanını yeniden etkinleştirme eklendi. İkinci koşuda sayfa geçişinde body referansı stale oldu. Ortak assertAccessible metin okuması tek JavaScript çağrısına taşındı; güvenlik engeli hata koşulu korunur. Geçici tanılama kodu veya yorum satırı eklenmedi.

Bu yerel canlı testtir; uzak CI veya tüm kataloğun geçtiği anlamına gelmez. Chrome CDP uyarısı görüldü; standart WebDriver işlemleri tamamlandı.

Ek kontroller: 5 birim testi ve 7 aktif Cucumber senaryosunun dry-run kontrolü geçti. İlk TC-003/005/008/010 toplu canlı regresyonunda 3 senaryo geçti; TC-003 kategori doğrulaması zaman aşımına uğradı (toplam 4, 1 error, BUILD FAILURE). TC-010 bu ilk toplu koşuda da geçti. İlk koşu başarılı olarak sunulmaz.

TC-003 tekli tanılama koşusunda doğru URL ve DOM başlığına rağmen görünürlük koşulu zaman aşımına uğradı. Başlık scrollIntoView ile görünür alana getirildi ve örtü temizliği yapıldı; aynı TC-003 yeniden 1/1 geçti. URL/başlık şartları kaldırılmadı. Kategori timeout mesajına URL ve başlık tanılaması eklendi.

Nihai toplu canlı regresyon: TC-003, TC-005, TC-008 ve TC-010 birlikte 4/4 geçti, 0 failure/error/skipped; BUILD SUCCESS. Test süresi 43,53 saniye, Maven toplamı 46,173 saniye. Son çıktılar target/selenium-search-regression.html ve JSON dosyasındadır; aynı isimli önceki raporların üzerine yazılır.

```powershell
mvn -B -ntp test '-Dtest=EcommerceIT' '-Dcucumber.filter.tags=@TC-003 or @TC-005 or @TC-008 or @TC-010' '-Dcucumber.plugin=pretty,html:target/selenium-search-regression.html,json:target/selenium-search-regression.json'
```

TC-001, TC-002 ve TC-024 canlı olarak yeniden çalıştırılmadı. API ve Karate bu değişiklik için yeniden çalıştırılmadı. Yeni geliştirmelerin uzak CI sonucu henüz yoktur.
