package testRunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "features", format = {"json:target/cucumber.json", "html:target/site/cucumber-pretty"}, 
                 glue = "stepDefinitions")

public class TestRunner {
}
