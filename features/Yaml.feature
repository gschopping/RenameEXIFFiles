Feature: read Yaml file

  Scenario: count timelines
    Given configuration file "start.yml"
    Then number of timelines should be 56

  Scenario: 4th item
    Given configuration file "start.yml"
    When get element 4
    Then copyright should be "Guido Schöpping (2019)"
    And country should be "België"
    And description should be "Wandeling langs de Lienne"
    And startdate should be "2019-03-22 11:00:00"
    And enddate should be "2019-03-24 11:00:00"
