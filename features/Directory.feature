Feature: Read Files

  Scenario: Read all media files from given directory
    Given directory "Z:\workspace\resources"
    When read all media files
    Then the number of files should be 10
    And the first file should be "OnePlus2.mp4"
    And the last file should be "SonyA6300.MP4"