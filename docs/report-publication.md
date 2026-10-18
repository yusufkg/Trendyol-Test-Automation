# GitHub Pages ile rapor yayımlama

CI'nin başarılı standart main koşusu, HTML rapor sitesini hazırlar. Ardından ayrı publish-reports job'u GitHub Pages'e yükler. Bu adım test raporlarının sürekli yayımlanmasıdır; Trendyol uygulamasının production dağıtımı değildir.

## Etkinleştirme

1. Değişiklikleri PR üzerinden main'e alın.
2. Repository Settings → Pages → Build and deployment → Source alanında GitHub Actions seçin.
3. Actions → Maven and Karate CI → Run workflow: main seçin; live_tests kapalı kalsın.
4. verify ve publish-reports job'larının ikisinin de başarılı olduğunu kontrol edin.
5. Deployment job'undaki URL'yi açıp Karate rapor bağlantısı ve ekran görüntüsünün yüklendiğini kontrol edin.

Beklenen proje URL'si https://yusufkg.github.io/Trendyol-Test-Automation/ adresidir. Başarılı deployment görülmeden bu adres çalışıyor kabul edilmez. Bu düzenleme henüz uzakta yayımlanarak doğrulanmamıştır.

Yayımlanan içerik: birim testi özeti, Cucumber dry-run HTML'i ve yerel fixture üzerinde Karate Chrome raporu. Canlı Trendyol koşuları, pull request ve geliştirme dalı koşuları Pages'e yayımlanmaz; raporlar artifact olarak kalır. Pages içeriği herkesçe görülebilir.

Yerel önizleme için önce CI'nin standart rapor komutlarını çalıştırın; ardından scripts/build-report-site.ps1 kullanın. Script gerekli raporlar yoksa veya Karate özeti canlı navigation testlerini içeriyorsa durur. Mevcut çıktı klasörünün üzerine yazmaz; başka bir -Output seçilebilir.

CV ifadesi deployment doğrulanana kadar: GitHub Actions CI ve GitHub Pages rapor yayımlama yapılandırması. Başarılı yayımlama sonrası: GitHub Actions ile CI ve HTML test raporlarının GitHub Pages'e otomatik yayımlanması.
