Feature: Unittest CreateDate

  Scenario: Read date from Sony RAW file
    Given File "SonyA77.ARW"
    Then the creationdate is "2019-03-16 18:09:06"

  Scenario: Read date from JPEG file
    Given File "SonyA6300.JPG"
    Then the creationdate is "2019-08-30 22:47:31"

  Scenario: Read date from DNG file
    Given File "DJIOsmoPlus.DNG"
    Then the creationdate is "2019-08-11 18:36:31"

  Scenario: Read date from MP4 file
    Given File "OnePlus2.mp4"
    Then the creationdate is "2016-03-27 13:18:50"

  Scenario: Read date from MP4 file
    Given File "SonyA6300.MP4"
    Then the creationdate is "2019-06-28 12:39:40"

  Scenario: Read date from M2TS file
    Given File "SonyA77.m2ts"
    Then the creationdate is "2016-08-07 19:00:56"



