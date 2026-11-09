# Playwright TC-001 — 8 Kasım 2026

HomePageIT, Trendyol ana sayfasındaki URL, logo, arama alanı, kategori bağlantıları ve uygulama hata öğelerini kontrol eder. XPath ifadeleri mevcut senaryo kapsamına karşılık gelir.

İki yerel canlı deneme yapıldı. Her ikisi logo görünürlük doğrulamasında başarısız oldu. İkinci denemede kaydedilen HTML 39 byte idi ve yalnızca boş html/head/body öğelerini içeriyordu. Beklenen URL doğrulaması geçti. Ağ, site veya tarayıcı kaynaklı kesin neden belirlenmedi.

Bu TC için canlı başarı kaydı yoktur. Test hatası yükseltilir ve Maven başarısız çıkar.

Komut:

```powershell
mvn -f playwright-ui/pom.xml test '-Dtest=HomePageIT'
```

Açılış HTML ve PNG dosyaları modülün target/playwright-live/ dizininde, Surefire sonucu target/surefire-reports/ içindedir. Bu çıktılar Git'e eklenmez.

HomePageIT açıkça seçildiğinde çalışır. Varsayılan Surefire adlandırması yalnızca SearchSmokeTest sınıfını seçer. Yerel HTML smoke testi bağımsız doğrulanır.

## 9 Kasım incelemesi

Ana sayfa navigasyonunun HTTP yanıtı kontrol edilmeye başlandı. Yeni canlı koşuda HTTP 403 alındı ve test Home page returned HTTP 403 mesajıyla başarısız oldu. Böylece logo doğrulamasına geçmeden erişim reddi görünür hale geldi. Yanıtın hangi güvenlik kuralı nedeniyle verildiği belirlenmedi.

Hata anında navigation.txt içine HTTP durumu ve URL, home.html içine güncel DOM, home.png içine ekran görüntüsü kaydedilir. Kanıt toplama hataları asıl test hatasına suppressed exception olarak eklenir. Başarılı koşuda bu dosyalar üretilmez. Önceki koşu dosyaları target dizininde kalabilir; güncel koşu zamanını kontrol edin veya clean kullanın.

Logo görünürlüğü için assertion bekleme süresi açıkça 30 saniye olarak ayarlandı. Page varsayılan timeout ayarı assertion süresini yönetmediği için önceki koşu 5 saniye bekliyordu.
