Feature: Check for Covid statistics in the web app

  Scenario: Check that homepage loads properly
    When I navigate to "http://localhost:3000/"
    Then I should be shown a page with "Statistics"
    And I should see some continents
    And There should be a list with most cases

  Scenario: Check Cache statistics
    When I navigate to "http://localhost:3000/"
    And I click the "Cache Statistics" button
    Then I should be shown a page with "Cache Statistics"
    And I should see a cache named "Daily Cases"

  Scenario: Check World's evolution
    When I navigate to "http://localhost:3000/"
    And I click the "Covid Evolution" button
    Then I should be shown a page with "World"
    And I should see a date in "Statistics for"

  Scenario: Check a Country's statistics
    When I navigate to "http://localhost:3000/"
    And I select "USA" in the dropdown
    Then I should be shown a page with "USA"
    And I should see a number in "Population"

  Scenario: Check a Country's statistics for a day
    When I navigate to "http://localhost:3000/country/usa"
    And I select "2022-04-20" in the datepicker
    Then I should be shown a page with "USA"
    And I should see "Statistics for 2022-04-20"

  Scenario: Check a Country's history
    When I navigate to "http://localhost:3000/country/germany"
    Then I should be shown a page with "Germany"
    And I should be shown a line chart


  Scenario: Check an invalid's Country's statistics
    When I navigate to "http://localhost:3000/country/iDontExist"
    Then I should be shown a page with "find statistics for country"
