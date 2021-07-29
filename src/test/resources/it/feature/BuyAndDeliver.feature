Feature: BuySocks

  Scenario: Buy one pair of socks
    Given a user makes a checkout
    When the checkout is performed
    Then submitted to delivery