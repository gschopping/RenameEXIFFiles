package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.Assert;

public class stepDefinitions {
    @Given("I want to rename files")
    public void iWantToRenameFiles() {
    }
    @When("file {string} exists")
    public void fileExists(String configFile) {
        Assert.assertTrue(configFile.equals("start.yml"));
    }

    @Then("{string} should be of format YAML")
    public void shouldBeOfFormatYAML(String configFile) {
        Assert.assertTrue(configFile.equals("start.yml"));
    }

}
