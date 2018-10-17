Feature: Check scenario has its context

  Scenario: Third scenario
    Given There is property first=3 in scenario context
    Then The property first in scenario context has value 3
