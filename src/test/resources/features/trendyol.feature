@trendyol
Feature: Trendyol Samsung S20 alışveriş akışı
  Scenario: En düşük fiyat sıralamasıyla ürün sepete ekleme
    Given Trendyol ana sayfası açılır
    When kullanıcı tarayıcıda hesabına girişini tamamlar
    And "Samsung S20" ürünü aranır
    And ürünler en düşük fiyata sıralanır
    And en düşük fiyatlı uygun ürün sepete eklenir
    Then sepette Samsung S20 ürünü görünür
    Then sepette Samsung S20 ürünü görünür
