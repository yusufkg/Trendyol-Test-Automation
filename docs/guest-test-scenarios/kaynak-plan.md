# E-Ticaret Web Sitesi – Login Olmadan Detaylı Test Otomasyon Senaryoları

Hedef: Java 17, Selenium WebDriver, Cucumber (BDD), JUnit ve Maven kullanarak, kullanıcı girişi gerektirmeyen kapsamlı bir e-ticaret test otomasyon projesi geliştirmek.

Bu planı `Trendyol-Test-Automation` projen için kullanabilirsin. Senaryolar; ürün arama, kategori navigasyonu, filtreleme, sıralama, ürün detayları, fiyat doğrulama, favoriler, sepet ve negatif testleri kapsayacak.

Önemli bir ayrım var: Login gerektirmeyen bir test ile login olmadan çalıştırılabilen bir test aynı şey değildir. Örneğin ürün arama çoğunlukla misafir kullanıcıyla yapılabilirken favorilere ekleme veya sepette ürün saklama bazı sitelerde oturum gerektirebilir. Bu nedenle giriş gerektiren işlemleri zorlamak yerine, misafir kullanıcıya sunulan davranışı doğrulayacağız.

## 1. Otomasyon kapsamı

Test grupları

# 12

Planlanan test senaryosu

# 48

Otomasyon yaklaşımı

BDD + Page Object Model

Kullanıcı oturumu

Misafir (Guest)

| Test grubu                                | Senaryo sayısı | Öncelik |
| ----------------------------------------- | -------------- | ------- |
| Ana sayfa ve navigasyon                   | 4              | Yüksek  |
| Ürün arama                                | 6              | Kritik  |
| Kategori ve alt kategori                  | 4              | Yüksek  |
| Ürün filtreleme                           | 5              | Kritik  |
| Ürün sıralama                             | 4              | Kritik  |
| Ürün detay sayfası                        | 5              | Kritik  |
| Fiyat ve indirim doğrulama                | 4              | Kritik  |
| Misafir sepet işlemleri                   | 4              | Koşullu |
| Favoriler ve oturum yönlendirmesi         | 2              | Orta    |
| Negatif ve sınır değer testleri           | 4              | Yüksek  |
| Tarayıcı ve responsive testleri           | 3              | Orta    |
| Erişilebilirlik ve performans kontrolleri | 3              | Orta    |
| Toplam                                    | 48             |         |

Bu senaryoları gerçek bir sitede uygulamadan önce hangi işlemlerin girişsiz desteklendiğini doğrulamak gerekir.

## 2. Ayrıntılı test senaryoları

Aşağıdaki 48 senaryo, genel e-ticaret iş kuralları temel alınarak hazırlanmış bir test tasarımıdır. Trendyol'un güncel arayüzündeki her işlevin girişsiz kullanılabildiği doğrulanmış değildir. Her senaryoda önce işlevin mevcut olduğunu ve beklenen davranışın ürün gereksinimleriyle uyumlu olduğunu kontrol etmelisin.

### A. Ana sayfa ve navigasyon

TC-001 – Ana sayfanın başarıyla açılması

Ön koşul: Chrome açık, kullanıcı giriş yapmamış.

1. Tarayıcıyı başlat.
2. E-ticaret sitesinin ana sayfasına git.
3. Sayfanın yüklenmesini bekle.
4. Site logosunun görünürlüğünü kontrol et.
5. Arama alanının görünürlüğünü kontrol et.
6. Ana kategori menüsünü kontrol et.
7. Görünür hata mesajı bulunmadığını doğrula.

Beklenen: Ana sayfa kullanılabilir durumda açılmalı; temel navigasyon bileşenleri görünür olmalı.

TC-002 – Logo üzerinden ana sayfaya dönüş

1. Ana sayfayı aç.
2. Herhangi bir kategoriye gir.
3. Site logosuna tıkla.
4. Yönlendirilen adresi kontrol et.
5. Ana sayfanın temel bileşenlerini doğrula.

Beklenen: Kullanıcı ana sayfaya dönebilmeli.

TC-003 – Üst kategori menüsünün açılması

1. Ana sayfayı aç.
2. Bir kategori menüsünün üzerine gel veya tıkla.
3. Alt kategori menüsü varsa açıldığını doğrula.
4. Bir alt kategori seç.
5. Açılan sayfanın seçilen kategoriyle ilişkili olduğunu kontrol et.

Beklenen: Menü ve yönlendirmeler doğru çalışmalı.

TC-004 – Tarayıcı geri/ileri navigasyonu

1. Ana sayfayı aç.
2. Bir kategoriye git.
3. Bir ürün detayına git.
4. Tarayıcının geri butonunu kullan.
5. Önceki sayfanın açıldığını doğrula.
6. İleri butonuyla ürün detayına dön.

Beklenen: Navigasyon sırasında yanlış sayfaya yönlendirme veya beklenmeyen hata oluşmamalı.

### B. Ürün arama

TC-005 – Geçerli anahtar kelimeyle ürün arama

1. Ana sayfayı aç.
2. Arama alanının görünür ve etkin olmasını bekle.
3. Arama alanına `kablosuz mouse` yaz.
4. Enter tuşuna bas.
5. Arama sonuç sayfasının yüklenmesini bekle.
6. Arama sorgusunun doğru aktarıldığını kontrol et.
7. Ürün kartlarının görünürlüğünü doğrula.
8. Ürünlerin aranan kavramla ilişkili olduğunu kontrol et.

Beklenen: Arama sonuçları gösterilmeli ve sorguyla ilişkili ürünler listelenmeli.

TC-006 – Enter tuşuyla arama: Arama terimini yaz, Enter'a bas ve sonuç sayfasının açıldığını doğrula.

TC-007 – Arama butonuyla arama: Arama butonu mevcutsa terimi yaz, butona tıkla ve sonuçları doğrula.

TC-008 – Sonuç bulunamayan arama: Kontrollü test verisiyle sonuç üretmediği bilinen bir terim ara. Uygun boş sonuç mesajının gösterildiğini doğrula.

TC-009 – Türkçe karakterle arama: `çanta`, `gözlük`, `şarj cihazı` terimlerini ayrı ayrı ara. Türkçe karakterlerin bozulmadığını ve sorguların doğru aktarıldığını kontrol et.

TC-010 – Arama teriminin değiştirilmesi: Önce `mouse`, sonra `klavye` ara. İkinci aramada önceki sorguya ait sonuçların kalmadığını doğrula.

### C. Kategori testleri

TC-011 – Ana kategori seçimi: Ana menüden bir kategori seç, sayfanın doğru kategoriye ait olduğunu doğrula.

TC-012 – Alt kategori seçimi: Ana kategori → alt kategori yolunu izle. Breadcrumb ve ürün listesinin seçilen alt kategoriyle uyumunu kontrol et.

TC-013 – Breadcrumb navigasyonu: Alt kategoriden üst kategoriye breadcrumb aracılığıyla dön ve yönlendirmeyi doğrula.

TC-014 – Kategori değişikliği: İki farklı kategori arasında geçiş yap. Eski kategoriye ait filtrelerin yeni kategoriye yanlışlıkla taşınmadığını doğrula.

### D. Filtreleme testleri

TC-015 – Marka filtresi

1. Ürün araması yap.
2. Sonuçların yüklenmesini bekle.
3. Marka filtre bölümünü bul.
4. Listelenen markalardan birini seç.
5. Filtrenin uygulandığını doğrula.
6. Görüntülenen ürünlerin marka bilgilerini kontrol et.
7. Filtreyi kaldır.
8. Sonuçların güncellendiğini doğrula.

Beklenen: Seçilen markayla uyumlu ürünler listelenmeli; filtre kaldırıldığında kısıtlama kalkmalı.

TC-016 – Minimum fiyat filtresi: Alt fiyat sınırı belirle, sonuçlarda görünen fiyatların seçilen alt sınırdan düşük olmadığını doğrula.

TC-017 – Maksimum fiyat filtresi: Üst fiyat sınırı belirle, sonuçların sınırı aşmadığını doğrula.

TC-018 – Birden fazla filtre: Marka ve fiyat filtrelerini birlikte uygula. Görüntülenen ürünlerin iki koşulu da karşıladığını doğrula.

TC-019 – Filtre temizleme: Birden fazla filtre uygula, temizleme işlemini gerçekleştir ve aktif filtrelerin kaldırıldığını doğrula.

Not: Fiyat ve marka doğrulamasında ürün kartlarında sunulan değerleri esas al. Fiyat karşılaştırmalarında kampanya, varyant ve teslimat seçeneklerine ilişkin iş kurallarını ayrıca değerlendir.

### E. Ürün sıralama

TC-020 – Artan fiyata göre sıralama

1. Arama yap.
2. Ürün listesinin yüklenmesini bekle.
3. Sıralama menüsünü aç.
4. `En düşük fiyat` veya eşdeğer seçeneği seç.
5. Sonuçların güncellenmesini bekle.
6. İlk sayfada görünen ürün fiyatlarını al.
7. Fiyatları sayısal değerlere dönüştür.
8. Her fiyatın bir önceki fiyattan küçük olmadığını doğrula.

Beklenen: Görünen sonuçlar, sitenin tanımladığı fiyat sıralama kuralına uygun olmalı.

TC-021 – Azalan fiyata göre sıralama: En yüksek fiyat seçeneğini uygula ve fiyatların azalan düzende olduğunu doğrula.

TC-022 – Değerlendirme puanına göre sıralama: Böyle bir seçenek mevcutsa seç ve ürün puanlarının sıralama kuralına uygunluğunu doğrula.

TC-023 – Sıralama ile filtrenin birlikte çalışması: Önce marka filtresi, ardından artan fiyat sıralaması uygula. İki koşulun birlikte korunduğunu doğrula.

### F. Ürün detay sayfası

TC-024 – Ürün detayına yönlendirme

1. Arama sonuçlarını aç.
2. İlk uygun ürün kartını seç.
3. Ürün adını ve fiyatını kaydet.
4. Ürün detayını aç.
5. Gerekirse yeni sekmeye geç.
6. Detay sayfasının yüklenmesini bekle.
7. Ürün adının seçilen ürünle eşleştiğini doğrula.
8. Ürün görseli ve fiyat alanını kontrol et.

Beklenen: Doğru ürünün detay sayfası açılmalı.

TC-025 – Ürün görselleri: Ürün galerisindeki görseller arasında geçiş yap. Seçilen görselin ana görsel alanında gösterildiğini doğrula.

TC-026 – Ürün açıklaması: Ürün açıklaması ve özellikler bölümünün mevcut ürün verisiyle tutarlı olduğunu doğrula.

TC-027 – Ürün varyantı: Beden, renk veya kapasite seçeneği olan bir üründe varyant değiştir. Seçimin görünür biçimde güncellendiğini doğrula.

TC-028 – Ürün değerlendirmeleri: Yorum bölümüne git. Mevcut değerlendirme sayısı, puan ve yorum bileşenlerinin tutarlı gösterildiğini kontrol et.

### G. Fiyat ve indirim doğrulama

TC-029 – Liste ve detay fiyat karşılaştırması: Ürün kartındaki fiyatı kaydet, detay sayfasını aç ve aynı varyant/satıcı için fiyatları karşılaştır.

TC-030 – İndirim oranı doğrulama: İndirimin basit yüzdesel hesapla belirlendiği test verisinde eski fiyat, yeni fiyat ve gösterilen indirim oranını karşılaştır.

TC-031 – Para birimi gösterimi: Ürün fiyatlarının geçerli para birimiyle, doğru ondalık biçimde gösterildiğini doğrula.

TC-032 – Varyant değişiminde fiyat: Farklı fiyatlı iki varyant seç ve fiyatın seçime uygun güncellendiğini kontrol et.

### H. Misafir sepet işlemleri (site destekliyorsa)

TC-033 – Sepete ürün ekleme: Ürün detayına git, gerekli varyantı seç, sepete ekle ve sepet içeriğini doğrula.

TC-034 – Sepette adet artırma: Ürün miktarını artır, ara toplamın yeni miktara uygun hesaplandığını doğrula.

TC-035 – Sepetten ürün çıkarma: Eklenen ürünü kaldır ve sepet içeriğinin güncellendiğini kontrol et.

TC-036 – Sepet toplamı: Birden fazla ürün ekle; ürün fiyatı, adet, indirim ve varsa teslimat bedeline göre toplamı kontrol et.

Bu işlemler giriş gerektiriyorsa testi Blocked / Authentication Required olarak işaretle; giriş otomasyonu ekleme.

### I. Favoriler ve oturum yönlendirmesi

TC-037 – Girişsiz favori işlemi: Ürün kartındaki favori butonuna bas. Ürünün gereksinimlerde tanımlanan misafir davranışını (örneğin giriş yönlendirmesi) gösterdiğini doğrula.

TC-038 – Korumalı işlemin yönlendirilmesi: Oturum gerektiren bir işlevi başlat. Giriş ekranı veya bilgilendirme gösterildiğini doğrula; giriş yapma.

### J. Negatif ve sınır değer testleri

TC-039 – Boş arama: Arama alanını boş bırak ve Enter'a bas. Gereksinimde tanımlanan davranışın gerçekleştiğini kontrol et.

TC-040 – Özel karakter araması: `!@#$%` gibi karakterlerle arama yap. Uygulamanın hata vermeden güvenli bir sonuç veya bilgilendirme sunmasını doğrula.

TC-041 – Çok uzun arama metni: Belirlenen maksimum uzunluk sınırındaki ve sınırın üzerindeki girdileri dene. Uygulamanın gereksinime uygun davranmasını kontrol et.

TC-042 – Geçersiz fiyat aralığı: Minimum fiyat maksimumdan büyük olacak şekilde giriş yap. Uygulamanın geçersiz aralığı engellediğini veya uygun biçimde ele aldığını doğrula.

### K. Tarayıcı ve responsive testleri

TC-043 – Chrome testi: Temel arama akışını güncel Chrome'da çalıştır.

TC-044 – Firefox/Edge testi: Aynı akışı desteklenen başka bir tarayıcıda çalıştır; temel fonksiyonların tutarlılığını kontrol et.

TC-045 – Mobil viewport testi: Desteklenen mobil ekran genişliğinde arama, menü ve ürün kartlarının kullanılabilirliğini doğrula.

### L. Erişilebilirlik ve performans

TC-046 – Klavye navigasyonu: Tab ve Enter tuşlarıyla arama alanına ve temel navigasyon öğelerine ulaşılabildiğini kontrol et.

TC-047 – Görsellerin erişilebilirliği: Anlam taşıyan ürün görsellerinin erişilebilir isimlerini ve uygun alternatif metinlerini kontrol et.

TC-048 – Sayfa yüklenme süresi: Kontrol edilen test ortamında, önceden belirlenmiş performans eşiğine göre ana sayfa ve arama sonuçlarının yüklenmesini ölç. Tek bir Selenium ölçümünü kapsamlı performans testi olarak değerlendirme.

## 3. Cucumber `.feature` dosyası örneği

Bu örnek, test senaryolarının çalıştırılabilir BDD tanımlarına nasıl dönüştürüleceğini gösterir. Step Definition kodları ve gerçek siteye uygun locator'lar ayrıca yazılmalıdır.

```

# language: tr
@guest @regression
Özellik: E-ticaret misafir kullanıcı ürün işlemleri
  Kullanıcı giriş yapmadan ürünleri arayabilmeli,
  filtreleyebilmeli, sıralayabilmeli ve inceleyebilmelidir.

  Arka Plan:
    Diyelim ki kullanıcı e-ticaret ana sayfasını açar
    Ve kullanıcı oturum açmamıştır
    Ve ürün arama alanı görünür durumdadır

  @smoke @search
  Senaryo: Geçerli anahtar kelimeyle ürün arama
    Eğer ki kullanıcı "kablosuz mouse" araması yaparsa
    O zaman arama sonuç sayfası açılmalıdır
    Ve arama sonuçları sorguyla ilişkili olmalıdır

  @search @negative
  Senaryo: Sonuç bulunamayan ürün araması
    Eğer ki kullanıcı sonuç üretmediği bilinen test terimini ararsa
    O zaman sonuç bulunamadı durumu gösterilmelidir

  @filter
  Senaryo: Marka filtresi uygulama
    Diyelim ki kullanıcı "kulaklık" araması yapmıştır
    Eğer ki kullanıcı mevcut bir marka filtresi seçerse
    O zaman görüntülenen ürünler seçilen markaya uygun olmalıdır

  @sorting
  Senaryo: Ürünleri artan fiyata göre sıralama
    Diyelim ki kullanıcı "klavye" araması yapmıştır
    Eğer ki kullanıcı artan fiyat sıralamasını seçerse
    O zaman ürün fiyatları artan sırada olmalıdır

  @product
  Senaryo: Ürün detayını görüntüleme
    Diyelim ki kullanıcı "mouse" araması yapmıştır
    Eğer ki kullanıcı bir ürün kartını açarsa
    O zaman seçilen ürünün detay sayfası gösterilmelidir
    Ve ürünün adı ve fiyatı görünür olmalıdır

  @navigation
  Senaryo: Ürün detayından arama sonuçlarına dönüş
    Diyelim ki kullanıcı "mouse" araması yapmıştır
    Ve kullanıcı bir ürün detayını açmıştır
    Eğer ki kullanıcı önceki sayfaya dönerse
    O zaman arama sonuç sayfası gösterilmelidir

```

Cucumber'ın resmi Gherkin dokümantasyonu, senaryoların `Given–When–Then` mantığında yazılmasını ve tekrar eden veri kombinasyonları için `Scenario Outline` kullanılmasını öneriyor.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://cucumber.io\&sz=32)

Cucumber

+1



## 4. Proje mimarisi

Mevcut `Trendyol-Test-Automation` projen için önerdiğim yapı:

Trendyol-Test-Automation/

src/test/java/com/yusuf/qa/ ├── pages/ │   ├── HomePage.java │   ├── SearchResultsPage.java │   ├── CategoryPage.java │   ├── ProductDetailPage.java │   └── CartPage.java ├── steps/ │   ├── SearchSteps.java │   ├── FilterSteps.java │   ├── SortingSteps.java │   └── ProductSteps.java ├── runners/ │   └── TrendyolIT.java └── support/     ├── Hooks.java     └── TestContext.java  src/test/resources/features/ ├── search.feature ├── filter.feature ├── sorting.feature ├── product-detail.feature └── navigation.feature

Bu yapı öneridir; mevcut dosyalarını doğrudan silip yeniden oluşturmanı gerektirmez.

Selenium'un resmi Page Object Model yaklaşımında locator ve sayfa etkileşimleri Page sınıflarında tutulur, doğrulamalar ise test/step katmanında yapılır. Bu ayrım, arayüz değiştiğinde bakım maliyetini azaltır.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://www.selenium.dev\&sz=32)

Selenium



## 5. Otomasyonu hangi sırayla geliştirmelisin?

1\. Aşama – Smoke testleri

TC-001, TC-005, TC-011 ve TC-024. Önce ana sayfa, arama, kategori ve ürün detayının bağımsız çalışmasını sağla.

2\. Aşama – Filtreleme ve sıralama

TC-015–023. Fiyatları `BigDecimal` ile karşılaştır; `Thread.sleep()` yerine `WebDriverWait` ve uygun koşulları kullan.

3\. Aşama – Ürün ve fiyat doğrulamaları

TC-025–032. Ürün adı, varyant, fiyat, indirim ve görsel kontrollerini ekle.

4\. Aşama – Negatif ve regresyon testleri

TC-039–048. Sınır değerleri, tarayıcı farklılıklarını, klavye erişimini ve regresyon kontrollerini uygula.

5\. Aşama – Raporlama ve CI/CD

JUnit/Cucumber raporları, başarısız test ekran görüntüleri, SQL kayıtları ve Jenkins/GitHub Actions entegrasyonunu tamamla.

Selenium dokümantasyonu, dinamik içeriklerde uygun bekleme stratejilerinin kullanılmasını özellikle vurgular; bu yaklaşım kararsız (flaky) testleri azaltmaya yardımcı olur.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://www.selenium.dev\&sz=32)

Selenium



## 6. Test sonuçlarını nasıl sınıflandırmalısın?

| Durum   | Kullanım                                                                |
| ------- | ----------------------------------------------------------------------- |
| PASS    | Beklenen davranış doğrulandı                                            |
| FAIL    | İşlev çalıştı ancak beklenen sonuç gerçekleşmedi                        |
| BLOCKED | Cloudflare, erişim veya oturum gereksinimi nedeniyle test tamamlanamadı |
| SKIPPED | Test bilinçli olarak çalıştırılmadı                                     |

JUnit ve Cucumber'ın standart raporları bu durumların tamamını kendiliğinden ayrı kategoriler olarak sunmayabilir. Özellikle `BLOCKED` için özel raporlama mantığı gerekebilir.

Benim önerim: İlk aşamada 48 senaryonun tamamını kodlamaya çalışma. Önce ana sayfa → ürün arama → marka filtresi → fiyat sıralama → ürün detayı → fiyat karşılaştırması akışını altı bağımsız otomasyon senaryosu olarak geliştir.

Böylece login kullanmadan, gerçek doğrulamalar içeren ve GitHub'da sergileyebileceğin sürdürülebilir bir Selenium/Cucumber test projesi oluşturabilirsin.

Araştırmada kullanılan temel kaynaklar: [Selenium – Page Object Models](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/), [Selenium – Waiting Strategies](https://www.selenium.dev/documentation/webdriver/waits/) ve [Cucumber – Gherkin Reference](https://cucumber.io/docs/gherkin/reference/).