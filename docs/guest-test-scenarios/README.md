# Giriş yapmadan e-ticaret test senaryoları

Kullanıcının paylaştığı 48 test tasarımı, 12 gruba ayrılmıştır. Her dosyada senaryo kimlikleri, adımlar ve beklenen sonuçlar korunmuştur. Bunlar planlanan senaryolardır; Selenium işlemlerinin uygulanmış veya canlı sitede başarıyla doğrulanmış olduğu anlamına gelmez.

| Dosya | Senaryolar | Sayı |
|---|---|---:|
| [Ana sayfa ve navigasyon](01-ana-sayfa-navigasyon.md) | TC-001–004 | 4 |
| [Ürün arama](02-urun-arama.md) | TC-005–010 | 6 |
| [Kategori](03-kategori.md) | TC-011–014 | 4 |
| [Filtreleme](04-filtreleme.md) | TC-015–019 | 5 |
| [Sıralama](05-siralama.md) | TC-020–023 | 4 |
| [Ürün detay](06-urun-detay.md) | TC-024–028 | 5 |
| [Fiyat ve indirim](07-fiyat-indirim.md) | TC-029–032 | 4 |
| [Misafir sepet](08-misafir-sepet.md) | TC-033–036 | 4 |
| [Favoriler ve oturum](09-favoriler-oturum.md) | TC-037–038 | 2 |
| [Negatif ve sınır değer](10-negatif-sinir-deger.md) | TC-039–042 | 4 |
| [Tarayıcı ve responsive](11-tarayici-responsive.md) | TC-043–045 | 3 |
| [Erişilebilirlik ve performans](12-erisilebilirlik-performans.md) | TC-046–048 | 3 |
| **Toplam** | **TC-001–048** | **48** |

[Kaynak plan](kaynak-plan.md) paylaşılan metnin tamamını içerir: kapsam, mimari, geliştirme sırası ve sonuç sınıflandırma önerileri.

[Tam Gherkin tanımları](examples/guest-gherkin.md) TC-001–TC-048 için 48 senaryo tanımını içerir. Gherkin dokümanı tamamlandı; tüm tanımların Selenium Step Definition kodları henüz uygulanmadı. Tek e-commerce.feature artık 48 tanımı @planned bölümünde de içerir; runner varsayılan olarak yalnızca uygulanmış senaryoları çalıştırır.

İlk uygulama aşaması için kaynak planın önerdiği altı bağımsız akış: ana sayfa, ürün arama, marka filtresi, artan fiyat sıralaması, ürün detayı ve liste/detay fiyat karşılaştırması.




Güncel uygulama durumu: Selenium 4 uygulanmış / 44 planlı, Karate 4 uygulanmış / 44 planlı. HTML raporları ve GitHub Pages rapor yayını mevcuttur; SQL entegrasyonu güncel sürümde yoktur. Bu belge test tasarımını anlatır; kanıt ve framework bazlı durum için docs/scenario-status.md esas alınır.
