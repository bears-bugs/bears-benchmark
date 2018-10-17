Feature: Check scenario has its context

  Scenario: First scenario
    Given There is property first=1 in scenario context
    Then The property first in scenario context has value 1
