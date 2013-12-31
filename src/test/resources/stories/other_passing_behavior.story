Scenario: A scenario that works with parameters

Given I have an implemented JBehave scenario with one param
And the scenario with two uno and dos parameters also works
When user open 'http:\\www.google.com'
Then user should see 'Google' in title