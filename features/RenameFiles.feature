Feature: RenameFiles

  Scenario: rename files in root directory
    Given directory "Z:\workspace\Mediafiles"
    And configuration file "Z:\workspace\Mediafiles\start.yml"
    When rename all files
    Then in subdirectory "Z:\workspace\Mediafiles\results" 9 files will be found