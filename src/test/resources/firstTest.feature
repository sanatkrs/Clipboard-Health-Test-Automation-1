Feature: Add item to the cart
  Scenario: User should be able to add item into the cart
    Given User is on cart page
    When User clicks on the add item button
    Then Item should be displayed into the list
