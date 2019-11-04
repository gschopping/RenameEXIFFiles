package testRunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "features",  plugin = {"pretty", "json:target/cucumber.json", "html:target/site/cucumber-pretty"}, strict = false,
                 glue = "stepDefinitions")
public class TestRunner {
}
