Feature: RenameFiles

  Scenario: rename files in root directory
    Given directory "Z:\workspace\resources"
    And configuration file "Z:\workspace\resources\start.yml"
    When rename all files
    Then in subdirectory "Z:\workspace\resources\results" 4 files will be found