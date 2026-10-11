@demo
Feature: Demo login smoke test
  Scenario: Geçerli kullanıcı giriş yapar
    Given demo giriş sayfası açılır
    When geçerli demo hesabıyla giriş yapılır
    Then giriş başarılı mesajı görüntülenir
