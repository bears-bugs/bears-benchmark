Feature: Check scenario has its context

  Scenario: Second scenario
    Given There is property first=2 in scenario context
    Then The property first in scenario context has value 2
