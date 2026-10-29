# Senaryo uygulama durumu

29 Ekim 2026 kaynak denetimi. Durum, ilgili feature dosyasındaki @planned etiketine göre belirlenmiştir; canlı testin her koşuda geçtiği anlamına gelmez. Her TC kimliği bir tasarımdır; Scenario Outline veri satırları ayrı TC sayılmaz.

Selenium: 4 uygulanmış / 44 planlı. Karate: 4 uygulanmış / 44 planlı. Aynı TC iki framework içinde bulunabilir; sayılar toplanarak 8 farklı senaryo denmez.

| Kimlik | Senaryo | Selenium/Cucumber | Karate |
|---|---|---|---|
| TC-001 | I open the Trendyol home page successfully using XPath | Uygulandı | Uygulandı |
| TC-002 | I return to the home page using the logo | Uygulandı | Uygulandı |
| TC-003 | I open the category menu | Planlı | Uygulandı |
| TC-004 | I use browser Back and Forward | Planlı | Planlı |
| TC-005 | I search with a valid keyword | Uygulandı | Uygulandı |
| TC-006 | I search by pressing Enter | Planlı | Planlı |
| TC-007 | I search by clicking the search button | Planlı | Planlı |
| TC-008 | I search for a term with no results | Planlı | Planlı |
| TC-009 | I search with Turkish characters | Planlı | Planlı |
| TC-010 | I replace the search query | Planlı | Planlı |
| TC-011 | I select a main category | Planlı | Planlı |
| TC-012 | I select a subcategory | Planlı | Planlı |
| TC-013 | I return to a parent category using the breadcrumb | Planlı | Planlı |
| TC-014 | I change categories without carrying invalid filters | Planlı | Planlı |
| TC-015 | I apply and remove a brand filter | Planlı | Planlı |
| TC-016 | I apply a minimum price filter | Planlı | Planlı |
| TC-017 | I apply a maximum price filter | Planlı | Planlı |
| TC-018 | I combine brand and price filters | Planlı | Planlı |
| TC-019 | I clear active filters | Planlı | Planlı |
| TC-020 | I sort by ascending price | Planlı | Planlı |
| TC-021 | I sort by descending price | Planlı | Planlı |
| TC-022 | I sort by review rating | Planlı | Planlı |
| TC-023 | I combine sorting with a brand filter | Planlı | Planlı |
| TC-024 | I open the selected product details | Uygulandı | Planlı |
| TC-025 | I change the selected product image | Planlı | Planlı |
| TC-026 | I view the product description and specifications | Planlı | Planlı |
| TC-027 | I change the selected product variant | Planlı | Planlı |
| TC-028 | I view product reviews | Planlı | Planlı |
| TC-029 | I compare list and detail prices | Planlı | Planlı |
| TC-030 | I verify a simple percentage discount | Planlı | Planlı |
| TC-031 | I verify currency and decimal formatting | Planlı | Planlı |
| TC-032 | I verify prices after changing variants | Planlı | Planlı |
| TC-033 | I add a product to the guest cart | Planlı | Planlı |
| TC-034 | I increase the quantity in the guest cart | Planlı | Planlı |
| TC-035 | I remove the test product from the guest cart | Planlı | Planlı |
| TC-036 | I verify the guest cart total | Planlı | Planlı |
| TC-037 | I verify the guest favorites behavior | Planlı | Planlı |
| TC-038 | I verify a protected action redirects to login | Planlı | Planlı |
| TC-039 | I submit an empty search | Planlı | Planlı |
| TC-040 | I search with special characters | Planlı | Planlı |
| TC-041 | I test the maximum search text length | Planlı | Planlı |
| TC-042 | I enter an invalid price range | Planlı | Planlı |
| TC-043 | I search in the current supported Chrome version | Planlı | Planlı |
| TC-044 | I search in a supported alternative browser | Planlı | Planlı |
| TC-045 | I use search and navigation in a mobile viewport | Planlı | Planlı |
| TC-046 | I search using only the keyboard | Planlı | Planlı |
| TC-047 | I verify accessible names for meaningful product images | Planlı | Planlı |
| TC-048 | I measure page loading times in a controlled environment | Planlı | Planlı |

## Kanıt kapsamı

[PR #4 CI koşusu](https://github.com/yusufkg/Trendyol-Test-Automation/actions/runs/39202080626): birim testleri, Cucumber adım eşleşmesi ve yerel fixture üzerinde headless Karate tarayıcı testi başarılıdır. Canlı Selenium/Karate adımları atlanmıştır; Pages yayını PR koşusunda yapılmamıştır.

19 Ekim yerel canlı regresyon: Karate TC-001, TC-002, TC-003 ve TC-005 birlikte geçti. Ayrıntı karate-ui/README.md dosyasındadır. Tarihsel CI/Pages kanıtları yeni geliştirmelerin canlı test sonucu olarak kullanılmaz.
