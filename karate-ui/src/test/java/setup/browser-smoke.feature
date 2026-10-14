Feature: Karate Chrome environment verification

  @SETUP-001
  Scenario: SETUP-001 - I click and enter text using XPath
    Given driver fixtureUrl
    And waitFor("//input[@id='search']")
    When input("//input[@id='search']", 'mouse')
    And click("//button[@id='search-button']")
    Then waitFor("//*[@id='result']")
    And match text("//*[@id='result']") == 'Search: mouse'
    And screenshot()
