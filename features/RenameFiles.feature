Feature: RenameFiles

  Scenario: rename files in root directory
    Given directory "Z:\workspace\Mediafiles"
    And configuration file "Z:\workspace\Mediafiles\start.yml"
    When rename all files
    Then in subdirectory "Z:\workspace\Mediafiles\results" 22 files will be found

  Scenario: rename files in Timelaps directory
    Given directory "Z:\workspace\Mediafiles"
    And configuration file "Z:\workspace\Mediafiles\start.yml"
    When rename all timelaps files in subdir "Timelaps1"
    Then in subdirectory "Z:\workspace\Mediafiles\Timelaps1\results" 7 files will be found