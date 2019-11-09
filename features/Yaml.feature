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

  Scenario: faulty yaml file
    Given configuration file "not_a_yaml.yml"
    Then an error "Error on line 11, column 5: -startdate: \"2019-04-13 10:00:00\"" should be shown
    
  Scenario: incorrect date format
    Given configuration file "incorrect_date.yml"
    Then an error "Error in timeline 1, incorrect dateformat: 2019-04 10:00:00" should be shown
    
  Scenario: 2 timelines with same startdate
    Given configuration file "same_date.yml"
    Then an error "Error in timeline 2, startdate: 2019-04-05 10:00:00 already exists" should be shown
    
  Scenario: incorrect countrycode is used
    Given configuration file "invalid_countrycode.yml"
    Then an error "Error in timeline 3, countrycode: BX is not valid" should be shown

  Scenario: invalid variable is used
    Given configuration file "invalid_variable.yml"
    Then an error "Error on line 13, column 11: undefined alias <<: *default" should be shown