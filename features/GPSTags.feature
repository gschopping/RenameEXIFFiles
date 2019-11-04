Feature: Read GPS tags and get address

  Scenario: Read GPS from Sony RAW file
    Given File "SonyA77.m2ts"
    When read GPS tags
    Then latitude should be "51.454183"
    And longitude should be "3.653545"
    And street should be "Rammekensweg"
    And location should be "Ritthem"
    And city should be "Vlissingen"

  Scenario: Read GPS from Sony RAW file and writes found address information
    Given File "SonyA77.ARW"
    When read GPS tags and write address information
    Then tag "XMP:City" should contain "Schouwen-Duiveland"
    And tag "XMP:CountryCode" should contain "NL"
    And tag "IPTC:Country-PrimaryLocationCode" should contain "NLD"
    And tag "XMP:Country" should contain "Nederland"
    And tag "IPTC:Country-PrimaryLocationName" should contain "Nederland"
    And tag "XMP:State" should contain "Zeeland"
    And tag "IPTC:Province-State" should contain "Zeeland"
    And tag "IPTC:ObjectName" should contain "Bruinisse"
    And tag "IPTC:Sub-location" should contain "Bruinisse"
