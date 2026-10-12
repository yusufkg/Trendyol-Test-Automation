# Güncel test planı

| Alan | Kontrol | Beklenen |
|---|---|---|
| Misafir arama | Trendyol / Hepsiburada üzerinde kablosuz mouse ara | Sorguyu içeren sonuç başlığı ve görünür ürün kartları. |
| Ürün detay | TC-024: mouse aramasından ürün kartına tıkla | Ürün kimliği ve adı eşleşir; görsel ve fiyat görünür. |
| Fiyat | Türkçe fiyat metnini ayrıştır | Sayısal fiyat doğru elde edilir; geçersiz metin reddedilir. |
| Ürün kimliği | Ürün bağlantısını ayrıştır | Kart ve detay eşleştirmesinde kullanılacak kimlik elde edilir. |
| Demo | Ayrı demo sitesinde örnek hesabıyla giriş | Başarı mesajı. |

Tek e-commerce.feature ve EcommerceIT runner kullanılır. Diğer 47 misafir senaryosu @planned etiketli tasarımdır. Çalıştırma komutları README.md içindedir.

Trendyol hesap girişi ve Samsung S20 alışveriş akışı iptal edildi. SQL Server kayıtları ve otomatik HTML/görsel raporlama kaldırıldı. Sonuçlar terminal ve standart Maven test çıktılarından incelenir. Dry-run canlı tarayıcı davranışını doğrulamaz.