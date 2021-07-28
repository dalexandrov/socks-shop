Feature: BuySocks

  Scenario: Buy one pair of socks
    Given a shopping cart with one pair of socks
    When a user makes a checkout
    Then checkout performed and submitted for delivery