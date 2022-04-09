Feature: Search Flights in BlazeDemo

  Scenario: Find flights from a city to another
    When I navigate to "https://blazedemo.com/"
    And I choose "Paris" as the departure city and "London" as my destination
    And I click the Find Flights button
    Then I should be shown a page with "Flights from Paris to London"
    #When I click the 2 Choose This Flight button
    #Then I should be shown a page with "Your flight from TLV to SFO has been reserved"