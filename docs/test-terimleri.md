# Yazılım test terimleri

| Terim | Açıklama | Bu projeden örnek |
|---|---|---|
| Statik test | Kodu çalıştırmadan gereksinimleri veya kodu inceleme. | SQL sorgularının ve senaryoların gözden geçirilmesi. |
| Dinamik test | Yazılımı çalıştırıp davranışını doğrulama. | Ürün araması yapıp sonucu kontrol etme. |
| Birim testi (unit) | Küçük bir fonksiyonu bağımlılıklarından ayrı sınama. | Türkçe fiyat metninin sayıya çevrilmesi. |
| Entegrasyon testi | Bileşenlerin birlikte çalışmasını doğrulama. | JDBC ile SQL test sonucu kaydı. |
| Sistem testi | Tam sistemin gereksinimlerini sınama. | E-ticaret aramasının çalışması. |
| Uçtan uca test (E2E) | Kullanıcının baştan sona iş akışını doğrulama. | Ana sayfa → arama → sonuç doğrulama. |
| Kabul testi (UAT) | İş ihtiyacının karşılandığını kullanıcı açısından doğrulama. | Aranan ürüne ilişkin sonuçların görünmesi. |
| Fonksiyonel test | Yazılımın ne yaptığını kontrol etme. | Arama başlığında sorgunun görünmesi. |
| Fonksiyonel olmayan test | Hız, güvenlik, kullanılabilirlik gibi kalite özelliklerini sınama. | Listeleme süresi veya erişim izinleri. |
| Pozitif test | Geçerli girdinin beklenen başarılı sonucu vermesi. | Geçerli ürün adıyla sonuç alınması. |
| Negatif test | Geçersiz girdinin kontrollü reddedilmesi. | Boş arama girdisinin ele alınması (planlanan). |
| Regresyon testi | Değişikliklerin mevcut davranışı bozmadığını kontrol etme. | Locator değişikliğinden sonra testleri tekrar çalıştırma. |
| Yeniden test (retest) | Belirli bir hatanın düzeldiğini aynı koşullarda kontrol etme. | Arama kutusu hatasını tekrar sınama. |
| Smoke test | Temel özelliklerin çalıştığını hızlıca kontrol etme. | Uygulamanın ve tarayıcının açılması. |
| Sanity test | Küçük bir değişiklikle ilgili işlevleri dar kapsamda sınama. | Yalnızca fiyat ayrıştırmasını kontrol etme. |
| Beyaz kutu | İç kod yapısını bilerek test tasarlama. | Fiyat ayrıştırıcısının dallarını sınama. |
| Siyah kutu | İç yapıya bakmadan girdiler ve çıktılar üzerinden test tasarlama. | Gherkin alışveriş senaryosu. |
| Gri kutu | İç yapı hakkında kısmi bilgiyle test tasarlama. | UI işlemini SQL kayıtlarıyla karşılaştırma. |
| Sınır değer analizi | Geçerli/geçersiz aralık sınırlarını sınama. | Arama metni uzunluk sınırlarını sınama (planlanan). |
| Eşdeğer sınıf | Benzer davranış beklenen girdileri gruplama. | Geçerli, boş ve çok uzun arama metinleri (planlanan). |
| Performans / yük / stres | Yanıt süresini; beklenen yükü; sınırların üzerindeki yükü sınama. | SQL listelemesinin çok kayıtla ölçülmesi. Bu projede uygulanmadı. |
| Güvenlik testi | Yetkilendirme ve veri koruma davranışını sınama. | Yetkisiz DB erişiminin reddi. Bu projede kapsamlı uygulanmadı. |
| BDD / Gherkin | Davranışı Given–When–Then biçiminde örneklerle tanımlama yaklaşımı ve dili. | Ortak e-commerce.feature dosyası. Test seviyesi değildir. |
| Assertion | Gerçek sonuç ile beklenen sonucu karşılaştıran kontrol. | Arama başlığı ve ürün kartlarının görünürlüğü. |
| Fixture / teardown | Test verisini hazırlama / test sonrası temizleme. | Senaryo sonunda test tarayıcısının kapatılması. |
| Test raporu / hata logu | Sonuçların özeti / başarısızlığın teknik ayrıntısı. | HTML raporu / SQL ErrorMessage alanı. |

Beklenen hata ile test başarısızlığı farklıdır: geçersiz fiyat aralığının reddedilmesini bekleyen negatif test, hata doğru şekilde oluşursa **PASSED** olur. Beklenen hata oluşmazsa **FAILED** olur. Canlı sitedeki ağ, stok ve DOM değişimleri test sonucunu etkileyebilir; birim testlerinin geçmesi E2E sonucunu kanıtlamaz.
