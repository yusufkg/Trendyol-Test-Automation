# Misafir kullanıcı Gherkin tanımları — 48 senaryo

**Gherkin tanımları tamamlandı.** TC-001–TC-048 aşağıdaki tek blokta yer alır. Bunlar senaryo tasarımıdır; tamamının Selenium Step Definition kodları yazılmış veya canlı sitede geçtiği anlamına gelmez. Aktif otomasyondaki genel ürün araması `src/test/resources/features/e-commerce.feature` içinde kalır. Bu Markdown dosyası Cucumber runner tarafından çalıştırılmaz.

Test verileri uygulama öncesinde hedef site için hazırlanmalıdır: mevcut kategori/marka, boş sonuç üreten terim, stoklu ürün, varyant/satıcı, geçerli fiyat sınırları ve performans eşiği. İş kuralı gerektiren beklenen davranışlar siteye ait onaylı gereksinimlere bağlanır. Özelliğin mevcut olmaması veya giriş zorunluluğu, uygulama hatası olarak varsayılmaz.

```gherkin
# language: en
@guest @regression
Feature: Guest e-commerce product operations

  Background:
    Given I have configured the target site and scenario test data
    And I am not signed in

  # A. Home page and navigation
  @planned @guest @TC-001 @smoke @navigation
  Scenario: I open the home page successfully
    Given I start a new guest session in a supported browser
    When I open the home page
    Then I verify the home page is ready to use
    And I verify the site logo is visible
    And I verify the search field is visible
    And I verify the main category menu is visible
    And I verify no application error message is displayed

  @planned @guest @TC-002 @navigation
  Scenario: I return to the home page using the logo
    Given I have opened the home page
    And I have opened an existing category page
    When I click the site logo
    Then I verify I am redirected to the home page URL
    And I verify the main navigation controls are visible

  @planned @guest @TC-003 @navigation
  Scenario: I open the category menu
    Given I have opened the home page
    And I have selected a menu with subcategories in the test data
    When I open the category menu using its supported interaction
    Then I verify the subcategory options are visible
    When I click an existing subcategory
    Then I verify the selected subcategory page is displayed

  @planned @guest @TC-004 @navigation
  Scenario: I use browser Back and Forward
    Given I have opened an existing category from the home page
    And I have opened a product detail in the same tab
    When I click the browser Back button
    Then I verify the previous category page is displayed
    When I click the browser Forward button
    Then I verify the same product detail page is displayed
    And I verify no unexpected redirect or application error occurs

  # B. Product search
  @planned @guest @TC-005 @smoke @search
  Scenario: I search with a valid keyword
    Given I have opened the home page
    And I verify the search field is visible and enabled
    When I click the search field
    And I enter "kablosuz mouse" into the search field
    And I press Enter
    Then I verify the search results page is displayed
    And I verify the search query is "kablosuz mouse"
    And I verify product cards are visible
    And I verify the visible products satisfy the configured relevance criteria

  @planned @guest @TC-006 @search
  Scenario: I search by pressing Enter
    Given I have opened the home page
    When I click the search field
    And I enter "klavye" into the search field
    And I press Enter
    Then I verify search results for "klavye" are displayed
    And I verify product cards are visible

  @planned @guest @TC-007 @search @conditional
  Scenario: I search by clicking the search button
    Given I have opened the home page
    And I have verified the site supports a separate search button
    When I click the search field
    And I enter "kulaklık" into the search field
    And I click the search button
    Then I verify search results for "kulaklık" are displayed
    And I verify product cards are visible

  @planned @guest @TC-008 @search @negative
  Scenario: I search for a term with no results
    Given I have opened the home page
    And I have a test term verified to return no results
    When I click the search field
    And I enter the no-results test term into the search field
    And I press Enter
    Then I verify the no-results message is displayed
    And I verify no product cards for the query are listed
    And I verify no application error occurs

  @planned @guest @TC-009 @search @turkish
  Scenario Outline: I search with Turkish characters
    Given I have opened the home page
    When I click the search field
    And I enter "<term>" into the search field
    And I press Enter
    Then I verify the search query is "<term>"
    And I verify Turkish characters are displayed correctly
    And I verify the search result state is displayed

    Examples:
      | term        |
      | çanta       |
      | gözlük      |
      | şarj cihazı |

  @planned @guest @TC-010 @search
  Scenario: I replace the search query
    Given I have searched for "mouse"
    When I click the search field
    And I replace the search text with "klavye"
    And I press Enter
    Then I verify the search query is "klavye"
    And I verify the visible products satisfy the new query relevance criteria
    And I verify the previous query result state is no longer shown

  # C. Categories
  @planned @guest @TC-011 @smoke @category
  Scenario: I select a main category
    Given I have opened the home page
    And I have selected an existing main category in the test data
    When I click the configured main category
    Then I verify the page title and URL match the selected category
    And I verify the category product list is displayed

  @planned @guest @TC-012 @category
  Scenario: I select a subcategory
    Given I have configured a main category and its subcategory
    And I have opened the main category page
    When I click the configured subcategory
    Then I verify the subcategory product list is displayed
    And I verify the breadcrumb matches the selected category path

  @planned @guest @TC-013 @category @navigation
  Scenario: I return to a parent category using the breadcrumb
    Given I have opened a subcategory page with breadcrumb navigation
    When I click the parent category breadcrumb
    Then I verify the correct parent category page is displayed
    And I verify the parent category product list is displayed

  @planned @guest @TC-014 @category @filter
  Scenario: I change categories without carrying invalid filters
    Given I have applied a category-specific filter in the first category
    And I have configured the required filter carryover behavior
    When I click a different category
    Then I verify the new category products are displayed
    And I verify the previous category-specific filter follows the configured rule

  # D. Filtering
  @planned @guest @TC-015 @filter
  Scenario: I apply and remove a brand filter
    Given I have searched for "kulaklık"
    And I have selected an available brand in the test data
    When I click the selected brand filter
    Then I verify the brand filter is active
    And I verify the visible product brands match the selected brand
    When I click the selected brand filter again to remove it
    Then I verify the brand filter is inactive
    And I verify the product results are updated

  @planned @guest @TC-016 @filter @price
  Scenario: I apply a minimum price filter
    Given I have opened results with price filters
    And I have configured a valid minimum price
    When I enter the configured minimum price
    And I click the price filter Apply button
    Then I verify the minimum price filter is active
    And I verify comparable visible prices are not below the minimum

  @planned @guest @TC-017 @filter @price
  Scenario: I apply a maximum price filter
    Given I have opened results with price filters
    And I have configured a valid maximum price
    When I enter the configured maximum price
    And I click the price filter Apply button
    Then I verify the maximum price filter is active
    And I verify comparable visible prices do not exceed the maximum

  @planned @guest @TC-018 @filter @price
  Scenario: I combine brand and price filters
    Given I have configured an available brand and a valid price range
    And I have opened suitable product results
    When I click the configured brand filter
    And I enter the configured price range
    And I click the price filter Apply button
    Then I verify both filters are active
    And I verify visible products satisfy both brand and price conditions

  @planned @guest @TC-019 @filter
  Scenario: I clear active filters
    Given I have applied multiple filters
    And I have recorded the unfiltered result state
    When I click the Clear filters control
    Then I verify my applied filters are removed
    And I verify default query filters follow the required rules
    And I verify results are updated for the unfiltered query

  # E. Sorting
  @planned @guest @TC-020 @sorting @price
  Scenario: I sort by ascending price
    Given I have searched for "klavye"
    And I have configured the comparable price rule
    When I click the sorting menu
    And I click the lowest-price sorting option
    Then I verify ascending price sorting is active
    And I verify comparable prices on the updated first page can be read as numbers
    And I verify each price is not lower than the previous price

  @planned @guest @TC-021 @sorting @price
  Scenario: I sort by descending price
    Given I have searched for "klavye"
    And I have configured the comparable price rule
    When I click the sorting menu
    And I click the highest-price sorting option
    Then I verify descending price sorting is active
    And I verify each comparable first-page price is not higher than the previous price

  @planned @guest @TC-022 @sorting @conditional
  Scenario: I sort by review rating
    Given I have opened product results
    And I have verified the site supports sorting by review rating
    And I have configured rating order and tie-breaking rules
    When I click the sorting menu
    And I click the review-rating sorting option
    Then I verify review-rating sorting is active
    And I verify the visible ratings follow the configured sorting rules

  @planned @guest @TC-023 @sorting @filter
  Scenario: I combine sorting with a brand filter
    Given I have applied an available brand filter to product results
    When I click the sorting menu
    And I click the lowest-price sorting option
    Then I verify the brand filter remains active
    And I verify visible products match the selected brand
    And I verify comparable prices are in ascending order

  # F. Product details
  @planned @guest @TC-024 @smoke @product
  Scenario: I open the selected product details
    Given I have searched for "mouse"
    And I have recorded a suitable product card identity name and price
    When I click that product card
    And I switch to its product tab if a new tab opens
    Then I verify the selected product detail page is displayed
    And I verify the product identity and name match the selected card
    And I verify the product image and price are visible

  @planned @guest @TC-025 @product @images
  Scenario: I change the selected product image
    Given I have opened a product detail page with at least two images
    When I click the second product image thumbnail
    Then I verify the selected image is displayed as the main image
    And I verify no image loading error is shown

  @planned @guest @TC-026 @product
  Scenario: I view the product description and specifications
    Given I have opened a product with configured description and specification data
    When I click the description and specifications sections
    Then I verify these sections are visible
    And I verify their contents match the selected product test data

  @planned @guest @TC-027 @product @variant
  Scenario: I change the selected product variant
    Given I have opened a product detail page with multiple selectable variants
    When I click a different color size or capacity variant
    Then I verify the selected variant is visibly active
    And I verify product information is updated for that variant

  @planned @guest @TC-028 @product @reviews
  Scenario: I view product reviews
    Given I have opened a product with configured review data
    When I click the reviews section
    Then I verify the rating and review count match the configured data
    And I verify review components are visible when review data exists
    And I verify the configured empty state when no review data exists

  # G. Price and discount checks
  @planned @guest @TC-029 @price @product
  Scenario: I compare list and detail prices
    Given I have configured the same product variant seller and campaign conditions
    And I have recorded the comparable product list price
    When I click the same product card to open its details
    Then I verify the detail price matches the list price within the configured tolerance
    And I verify the currency and price conditions are the same

  @planned @guest @TC-030 @price @discount
  Scenario: I verify a simple percentage discount
    Given I have configured a product with a simple percentage discount
    And I know its old price new price and rounding rule
    When I view the product price and discount fields
    Then I verify the old price is greater than zero
    And I verify the new price does not exceed the old price
    And I verify the displayed discount matches the calculated percentage and rounding rule

  @planned @guest @TC-031 @price @currency
  Scenario: I verify currency and decimal formatting
    Given I have configured the required site currency and number format
    When I view product prices
    Then I verify the correct currency is displayed
    And I verify the decimal and thousands separators match the required format
    And I verify the price text can be parsed as a numeric value

  @planned @guest @TC-032 @price @variant
  Scenario: I verify prices after changing variants
    Given I have configured two differently priced variants from the same seller
    And I have opened the first variant details
    When I click the second variant
    Then I verify the price changes to the expected second-variant price
    When I click the first variant again
    Then I verify its expected price is displayed again

  # H. Guest cart operations, only when supported without login
  @planned @guest @TC-033 @cart @conditional
  Scenario: I add a product to the guest cart
    Given I have verified guest cart operations are supported
    And I have prepared a clean guest cart belonging only to this test
    And I have configured an in-stock product and its required variant
    And I have opened that product detail page
    When I click the required variant
    And I click the Add to cart button
    Then I verify the product was added successfully
    And I verify the same product variant and quantity are in the cart
    And I verify I am not required to sign in

  @planned @guest @TC-034 @cart @conditional
  Scenario: I increase the quantity in the guest cart
    Given I have verified guest cart operations are supported
    And I have one in-stock product in the test cart
    And I can add a second unit without changing the price conditions
    When I click the Increase quantity control once
    Then I verify the cart quantity is two
    And I verify the line subtotal follows the quantity and current price rule

  @planned @guest @TC-035 @cart @conditional
  Scenario: I remove the test product from the guest cart
    Given I have verified guest cart operations are supported
    And I have only this test product in the test cart
    When I click the Remove product control
    Then I verify the product is no longer in the cart
    And I verify the configured empty cart state is displayed
    And I verify the total follows the empty cart rule

  @planned @guest @TC-036 @cart @price @conditional
  Scenario: I verify the guest cart total
    Given I have verified guest cart operations are supported
    And I have configured at least two in-stock products and their quantities
    And I know the discounts delivery charge and rounding rules
    When I add the configured products and quantities to the test cart
    Then I verify the cart lines show the correct products and quantities
    And I verify the total includes product amounts discounts and delivery charge correctly

  # I. Favorites and authentication redirects
  @planned @guest @TC-037 @favorites @authentication
  Scenario: I verify the guest favorites behavior
    Given I am viewing a product card with a favorites control
    And I have configured the required guest favorites behavior
    When I click the favorite button
    Then I verify the required guest favorites behavior occurs
    And I verify the login page is displayed when a login redirect is expected
    And I do not sign in

  @planned @guest @TC-038 @authentication
  Scenario: I verify a protected action redirects to login
    Given I have configured an action verified to require authentication
    When I start the protected action as a guest
    Then I verify the login page or configured authentication notice is displayed
    And I verify the protected action is not completed
    And I do not sign in

  # J. Negative and boundary checks
  @planned @guest @TC-039 @search @negative
  Scenario: I submit an empty search
    Given I have opened the home page
    And I have configured the required empty-search behavior
    When I click the search field
    And I clear the search field
    And I press Enter
    Then I verify the required empty-search behavior occurs
    And I verify no unexpected application error occurs

  @planned @guest @TC-040 @search @negative
  Scenario: I search with special characters
    Given I have opened the home page
    And I have configured the required special-character search behavior
    When I click the search field
    And I enter "!@#$%" into the search field
    And I press Enter
    Then I verify the query is handled safely
    And I verify the configured results or information message is displayed
    And I verify no unexpected application error occurs

  @planned @guest @TC-041 @search @negative @boundary
  Scenario Outline: I test the maximum search text length
    Given I have configured the maximum search length and overflow behavior
    And I have opened the home page
    When I click the search field
    And I enter prepared search text for "<lengthCase>"
    And I press Enter
    Then I verify "<expectedBehavior>" occurs
    And I verify no unexpected application error occurs

    Examples:
      | lengthCase     | expectedBehavior                        |
      | maximum limit  | valid boundary input is handled         |
      | maximum plus 1 | overflow follows the configured rule    |

  @planned @guest @TC-042 @filter @negative
  Scenario: I enter an invalid price range
    Given I have opened results with price filters
    And I have configured the required invalid-range behavior
    When I enter a minimum price greater than the maximum price
    And I click the price filter Apply button
    Then I verify the invalid range is rejected or handled by the configured rule
    And I verify the visible filter state matches that rule
    And I verify no unexpected application error occurs

  # K. Browser and responsive checks
  @planned @guest @TC-043 @browser @chrome
  Scenario: I search in the current supported Chrome version
    Given I start a new guest session in the current supported Chrome version
    And I have opened the home page
    When I click the search field
    And I enter "kablosuz mouse" into the search field
    And I press Enter
    Then I verify the search query is passed correctly
    And I verify the search results page is usable
    And I verify product cards are visible

  @planned @guest @TC-044 @browser @cross_browser
  Scenario Outline: I search in a supported alternative browser
    Given I have verified the site supports "<browser>"
    And I start a new guest session in the current supported "<browser>" version
    And I have opened the home page
    When I click the search field
    And I enter "kablosuz mouse" into the search field
    And I press Enter
    Then I verify the search query is passed correctly
    And I verify the basic search behavior is consistent across browsers
    And I verify product cards are visible

    Examples:
      | browser |
      | Firefox |
      | Edge    |

  @planned @guest @TC-045 @responsive
  Scenario: I use search and navigation in a mobile viewport
    Given I have configured a supported mobile viewport
    And I start a guest browser session at that viewport size
    And I have opened the home page
    When I click the mobile category menu
    Then I verify its options are visible and usable
    When I close the mobile category menu
    And I click the search field
    And I enter "kablosuz mouse" into the search field
    And I press Enter
    Then I verify the search field and product cards are usable in the mobile viewport
    And I verify no overlap or overflow prevents basic interactions

  # L. Accessibility and performance
  @planned @guest @TC-046 @accessibility @keyboard
  Scenario: I search using only the keyboard
    Given I have opened the home page
    When I press Tab to move through the main navigation controls
    Then I verify the keyboard focus is visible
    And I verify I can reach the search field using the keyboard
    When I enter "mouse" into the focused search field
    And I press Enter
    Then I verify the search results are displayed
    And I verify no keyboard trap occurs

  @planned @guest @TC-047 @accessibility @images
  Scenario: I verify accessible names for meaningful product images
    Given I have opened a product list or product detail page
    And I have identified meaningful images in the test data
    When I inspect their accessible names
    Then I verify each meaningful image has an appropriate non-empty accessible name
    And I verify decorative images are not treated as meaningful product images

  @planned @guest @TC-048 @performance
  Scenario: I measure page loading times in a controlled environment
    Given I have configured the test environment network conditions and measurement start and end points
    And I have configured loading thresholds for the home page and search results
    When I measure the time until the home page and "kablosuz mouse" results are usable
    Then I verify the home page time does not exceed its threshold
    And I verify the search results time does not exceed its threshold
    And I record the measured times in the test report
```

## Uygulama ve sonuç değerlendirme notları

- **48 ayrı TC tanımı vardır.** TC-009, TC-041 ve TC-044 veri tekrarları için `Scenario Outline` kullanır; örnek satırları 48 sayısına yeni TC kimlikleri eklemez.
- `@conditional` senaryoların özellik desteği önce doğrulanmalıdır. Giriş zorunlu misafir sepet işlemleri `BLOCKED` / oturum gereksinimi olarak kaydedilir; giriş otomasyonu eklenmez.
- TC-037 ve TC-038'de giriş yönlendirmesi beklenen sonuç ise doğru yönlendirme **PASS** olur. Her giriş ekranı otomatik olarak engel sayılmaz.
- Test verisi veya onaylı iş kuralı bulunmadan boş sonuç, indirim, sınır değer veya performans beklentisi uydurulmaz. Kampanya, varyant, satıcı, sponsorlu ürün ve teslimat koşulları karşılaştırmalarda tanımlanmalıdır.
- Sepet testleri ayrı misafir oturumunda yalnızca testin eklediği ürünlerle yürütülür; test sonunda kendi verileri temizlenir. Sipariş veya ödeme yapılmaz.
- Ürün detayı yeni sekmede açılırsa sekme kimlikleri kaydedilir; geri dönüş testi kaynak sekmeye dönerek doğrulanır. TC-004 aynı sekmedeki gerçek geri/ileri geçmişini sınar.
- TC-048, kontrollü bir süre kontrolüdür; kapsamlı yük veya performans testi değildir.

## Doğrulama

11 Ekim 2026: Projedeki Gherkin 28.0.0 ayrıştırıcısıyla sözdizimi doğrulandı. 48 senaryo tanımı, örnek tabloları açıldığında 52 senaryo örneği ve 0 ayrıştırma hatası bulundu. TC-001–TC-048 kimlikleri eksiksiz ve tekildir. Bu doğrulama Selenium adımlarını veya canlı site davranışını çalıştırmaz.

48 tanım tek src/test/resources/features/e-commerce.feature dosyasına da aktarılmıştır. @planned etiketli bölüm henüz Selenium adımları uygulanmadığı için varsayılan çalıştırmadan hariç tutulur.



Güncel uygulama durumu: Selenium 6 uygulanmış / 42 planlı, Karate 4 uygulanmış / 44 planlı. HTML raporları ve GitHub Pages rapor yayını mevcuttur; SQL entegrasyonu güncel sürümde yoktur. Bu belge test tasarımını anlatır; kanıt ve framework bazlı durum için docs/scenario-status.md esas alınır.
