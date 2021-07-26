Feature: AllSocks

  Scenario: Get all socks
    Given a request to get all socks
    When a user wants to get all socks
    Then the output is response with socks