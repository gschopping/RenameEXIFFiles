Feature: Read Files

  Scenario: Read all media files from given directory
    Given directory "Z:\workspace\resources"
    When read all media files
    Then the number of files should be 10
    And the first file should be "SonyA77.m2ts"
    And the last file should be "SonyA77.ARW"

  Scenario: Read subdirectories with Timelaps files
    Given directory "Z:\workspace\resources"
    When read Timelaps subdirectories
    Then the number of directories should be 2