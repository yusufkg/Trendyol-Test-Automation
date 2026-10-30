# Güncel test planı

Framework bazlı kaynak durum tablosu: [48 senaryo durumu](scenario-status.md).

| Alan | Selenium | Karate | Beklenen |
|---|---|---|---|
| TC-001 ana sayfa | Uygulandı | Uygulandı | Sayfa ve temel element kontrolleri |
| TC-002 navigasyon | Uygulandı | Uygulandı | Logoyla ana sayfaya dönüş |
| TC-003 kategori menüsü | Uygulandı | Uygulandı | Görünür alt kategoriden doğru listeye geçiş |
| TC-005 arama | Uygulandı | Uygulandı | URL sorgusu, başlık, ürün kartları ve örnek uygunluk kontrolü |
| TC-024 ürün detay | Uygulandı | Planlı | Kimlik/ad eşleşmesi, yüklenmiş görsel ve pozitif fiyat |
| Fiyat/ürün yardımcıları | 5 birim testi | Ayrı kapsam | Tarayıcı açmadan yardımcı kod doğrulaması |

Selenium 5 uygulanmış / 43 planlı; Karate 4 uygulanmış / 44 planlı. Diğer senaryoların test tasarımları hazırdır; uygulanmış sayılmaz. Çalıştırma komutları ana README.md dosyasındadır.

CI: birim testleri, Cucumber dry-run ve yerel HTML fixture üzerinde headless Karate testi. Canlı Trendyol adımları ayrıca elle seçilir. Güncel PR koşusunda canlı adımlar çalışmamıştır. HTML raporları ve GitHub Pages rapor yayını vardır; SQL logger güncel kaynakta bulunmaz.

Trendyol login ve Samsung S20 alışveriş akışı kapsamda değildir. Bağımsız API projesinde 5 senaryo, DataProvider ile 10 çalıştırma ve TestNG smoke/regression XML suite seçimi uygulandı. API CI job ve artifact mevcuttur; güncel kapsamın kanıtları api-tests/README.md ve roadmap.md dosyalarında izlenir.
