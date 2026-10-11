# Güncel test planı

| Alan | Kontrol | Beklenen |
|---|---|---|
| Misafir arama | Trendyol / Hepsiburada üzerinde kablosuz mouse ara | Sorguyu içeren sonuç başlığı ve görünür ürün kartları. |
| Fiyat | 12.499,90 TL | 12499.90 |
| Fiyat | 12.499 TL | 12499 |
| Fiyat | Fiyat içermeyen metin | null |
| Demo | Hazır örnek hesabıyla giriş | Başarı mesajı. |
| SQL | Başarılı/başarısız test | Durum, süre ve gerçek hata ayrıntıları kaydedilir. |
| SQL | Kayıt hatası | HTML raporunda hata ve başarısız çalıştırma. |
| Rapor | Senaryo hatası | Hata adımı ve alınabiliyorsa ekran görüntüsü. |

Tek e-commerce.feature ve EcommerceIT runner kullanılır. @trendyol ve @hepsiburada misafir aramasını; @demo ayrı örnek giriş testini seçer. 48 senaryo taslağı docs/guest-test-scenarios altında korunur.

Telefon rehberi ve Samsung S20 alışveriş akışı kaldırıldı. Dry-run tarayıcı işlemlerini doğrulamaz; canlı sitelerin güvenlik engelleri çalıştırmayı etkileyebilir.
