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
