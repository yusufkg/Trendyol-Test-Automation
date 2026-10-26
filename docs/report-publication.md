# GitHub Pages ile rapor yayımlama

CI'nin başarılı standart main koşusu, HTML rapor sitesini hazırlar. Ardından ayrı publish-reports job'u GitHub Pages'e yükler. Bu adım test raporlarının sürekli yayımlanmasıdır; Trendyol uygulamasının production dağıtımı değildir.

## Etkinleştirme

Private depolarda Pages hazırlama ve yayımlama adımları atlanır; testler ve test-reports artifact yüklemesi devam eder. Bu durum job özetinde açıklanır. Public depolarda aşağıdaki Pages ayarı ayrıca yapılmalıdır. Bu koruma, Pages desteklemeyen private depolarda Not Found hatasını önler.

Public depolarda yayımlama job'u önce Pages API erişimini kontrol eder. HTTP 404 durumunda yapılandırma ve deployment adımları atlanır; neden ve etkinleştirme yolu job özetine yazılır. Bu durum başarılı bir deployment anlamına gelmez. HTTP 403, 500 ve diğer beklenmeyen yanıtlar hata olarak korunur. Site etkinleştirildikten sonra job yeniden çalıştırılabilir.

1. Değişiklikleri PR üzerinden main'e alın.
2. Repository Settings → Pages → Build and deployment → Source alanında GitHub Actions seçin.
3. Actions → Maven and Karate CI → Run workflow: main seçin; live_tests kapalı kalsın.
4. verify ve publish-reports job'larının ikisinin de başarılı olduğunu kontrol edin.
5. Deployment job'undaki URL'yi açıp Karate rapor bağlantısı ve ekran görüntüsünün yüklendiğini kontrol edin.

Proje URL'si https://yusufkg.github.io/Trendyol-Test-Automation/ adresidir. `67192df` commit'i için [18 Ekim 2026 tarihli koşuda](https://github.com/yusufkg/Trendyol-Test-Automation/actions/runs/39100021848) verify ve publish-reports job'ları başarıyla tamamlandı; yayımlanan rapor sayfası da kontrol edildi. Daha sonraki değişiklikler için ilgili commit'in deployment sonucu ayrıca kontrol edilmelidir.

Yayımlanan içerik: birim testi özeti, Cucumber dry-run HTML'i ve yerel fixture üzerinde Karate Chrome raporu. Canlı Trendyol koşuları, pull request ve geliştirme dalı koşuları Pages'e yayımlanmaz; raporlar artifact olarak kalır. Pages içeriği herkesçe görülebilir.

Yerel önizleme için önce CI'nin standart rapor komutlarını çalıştırın; ardından scripts/build-report-site.ps1 kullanın. Script gerekli raporlar yoksa veya Karate özeti canlı navigation testlerini içeriyorsa durur. Mevcut çıktı klasörünün üzerine yazmaz; başka bir -Output seçilebilir.

CV ifadesi: GitHub Actions ile CI ve HTML test raporlarının GitHub Pages'e otomatik yayımlanması. Bu ifade, uygulamanın production dağıtımı anlamına gelmez.
