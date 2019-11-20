Feature: RenameFiles

  Scenario: rename files in root directory
    Given directory "Z:\workspace\Mediafiles"
    And configuration file "Z:\workspace\Mediafiles\start.yml"
    When rename all files
    Then in subdirectory "Z:\workspace\Mediafiles\results" 27 files will be found

  Scenario: rename files in Timelaps directory
    Given directory "Z:\workspace\Mediafiles"
    And configuration file "Z:\workspace\Mediafiles\start.yml"
    When rename all timelaps files in subdir "Timelaps1"
    Then in subdirectory "Z:\workspace\Mediafiles\Timelaps1\results" 7 files will be found

  Scenario: rename files over multiple Timelaps directories
    Given directory "Z:\workspace\Mediafiles"
    And configuration file "Z:\workspace\Mediafiles\start.yml"
    When rename all timelaps files
    Then in subdirectory "Z:\workspace\Mediafiles\Timelaps1\results" first file will be "20191109-0001 Vrouwenpolder.ARW"
    And in subdirectory "Z:\workspace\Mediafiles\Timelaps2\results" first file will be "20191109-0008 Vrouwenpolder.ARW"
    And in subdirectory "Z:\workspace\Mediafiles\Timelaps3\results" first file will be "20190316-0001 Bruinisse.ARW"
