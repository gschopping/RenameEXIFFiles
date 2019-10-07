Feature: Unittest RenameFiles

  Scenario: Read configuration file
    Given I want to rename files
    When file "start.yml" exists
    Then "start.yml" should be of format YAML