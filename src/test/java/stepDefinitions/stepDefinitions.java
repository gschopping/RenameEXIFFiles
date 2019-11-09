package stepDefinitions;

import EXIFFile.OpenStreetMapUtils;
import EXIFFile.ReadEXIF;
import EXIFFile.ReadYaml;
import EXIFFile.WriteEXIF;
import com.drew.imaging.ImageProcessingException;
import io.cucumber.java.en.*;
import junit.framework.AssertionFailedError;
import org.junit.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import static org.hamcrest.Matchers.matchesPattern;

public class stepDefinitions {
    String mediaFile;
    ReadEXIF readEXIF;
    Map<String, String> address;
    Double latitude;
    Double longitude;
    ReadYaml readYaml;
    ReadYaml.timeLine element;

    @Given("File {string}")
    public void file(String mediaFile) throws ImageProcessingException, IOException {
        this.mediaFile = mediaFile;
        this.readEXIF = new ReadEXIF("Z:\\workspace\\resources\\" + this.mediaFile);
    }

    @Then("the creationdate is {string}")
    public void theCreationdateIs(String creationDate) throws IOException, ParseException {
        String compareDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.readEXIF.GetCreateDateTime());
        Assert.assertEquals(creationDate, compareDate);
    }

    @Given("file to write {string}")
    public void fileToWrite(String mediaFile) throws IOException {
        this.mediaFile = mediaFile;
        // copy file from original destination in order to get clean exif values
        Path source = Paths.get("Z:\\workspace\\resources\\" + mediaFile);
        Path destination = Paths.get("Z:\\workspace\\resources\\results\\" + mediaFile);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    @When("write Author {string}")
    public void writeAuthor(String author) throws IOException {
        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\results\\" + this.mediaFile);
        writeEXIF.SetAuthor(author);
        writeEXIF.WriteFile();
    }

    @Then("tag {string} should contain {string}")
    public void tagShouldContain(String tag, String author) throws IOException {
        ReadEXIF readEXIF = new ReadEXIF("Z:\\workspace\\resources\\results\\" + this.mediaFile);
        String result = readEXIF.GetTag(tag);
        Assert.assertEquals(author, result);

    }

    @When("write Title {string}")
    public void writeTitle(String title) throws IOException {
        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\results\\" + this.mediaFile);
        writeEXIF.SetTitle(title);
        writeEXIF.WriteFile();
    }

    @When("write Keys {string}")
    public void writeKeys(String keys) throws IOException {
        String[] results = keys.split(",");
        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\results\\" + this.mediaFile);
        writeEXIF.SetKeys(results);
        writeEXIF.WriteFile();
    }

    @When("write Country {string}")
    public void writeCountry(String country) throws IOException {
        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\results\\" + this.mediaFile);
        writeEXIF.SetCountry(country);
        writeEXIF.WriteFile();
    }

    @When("write City {string}")
    public void writeCity(String city) throws IOException {
        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\results\\" + this.mediaFile);
        writeEXIF.SetCity(city);
        writeEXIF.WriteFile();
    }

    @Then("latitude should be {string}")
    public void latitudeShouldBe(String latitude) throws IOException {
        Assert.assertEquals(latitude, this.latitude.toString());
    }

    @And("longitude should be {string}")
    public void longitudeShouldBe(String longitude) throws IOException {
        Assert.assertEquals(longitude, this.longitude.toString());
    }

    @And("street should be {string}")
    public void streetShouldBe(String street) throws IOException {
        if (address != null) {
            Assert.assertEquals(street, address.get("street"));
        } else {
            Assert.fail();
        }
    }

    @And("location should be {string}")
    public void locationShouldBe(String location) {
        if (address != null) {
            Assert.assertEquals(location, address.get("location"));
        } else {
            Assert.fail();
        }
    }

    @And("city should be {string}")
    public void cityShouldBe(String city) {
        if (address != null) {
            Assert.assertEquals(city, address.get("city"));
        } else {
            Assert.fail();
        }
    }

    @When("read GPS tags")
    public void readGPSTags() throws IOException {
        this.longitude = readEXIF.GetGPSLongitude();
        this.latitude = readEXIF.GetGPSLatitude();
        this.address = OpenStreetMapUtils.getInstance().getAddress(this.latitude, this.longitude);
    }

    @When("read GPS tags and write address information")
    public void readGPSTagsAndWriteAddressInformation() throws IOException {
        this.longitude = readEXIF.GetGPSLongitude();
        this.latitude = readEXIF.GetGPSLatitude();
        this.address = OpenStreetMapUtils.getInstance().getAddress(this.latitude, this.longitude);
        if (this.address != null) {
            // copy file from original destination in order to get clean exif values
            Path source = Paths.get("Z:\\workspace\\resources\\" + mediaFile);
            Path destination = Paths.get("Z:\\workspace\\resources\\results\\" + mediaFile);
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\results\\" + this.mediaFile);
            writeEXIF.SetCountryCode(this.address.get("countrycode"));
            writeEXIF.SetCountry(this.address.get("country"));
            writeEXIF.SetCity(this.address.get("city"));
            writeEXIF.SetProvince(this.address.get("province"));
            writeEXIF.SetLocation(this.address.get("location"));
            writeEXIF.WriteFile();
        }
    }

    @Given("configuration file {string}")
    public void configurationFile(String configFile) throws FileNotFoundException, ParseException {
        this.readYaml = new ReadYaml("Z:\\workspace\\resources\\" + configFile);
    }

    @Then("number of timelines should be {int}")
    public void numberOfTimelinesShouldBe(int count) {
        Assert.assertEquals(count, this.readYaml.getTimeLines().size());
    }

    @When("get element {int}")
    public void getElement(int index) {
        this.element = this.readYaml.getTimeLines().get(index-1);
    }

    @Then("copyright should be {string}")
    public void copyrightShouldBe(String copyright) {
        Assert.assertEquals(copyright, element.getCopyright());
    }

    @And("country should be {string}")
    public void countryShouldBe(String country) {
        Assert.assertEquals(country, element.getCountry());
    }

    @And("startdate should be {string}")
    public void startdateShouldBe(String date) {
        String compareDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.element.getStartdate());
//        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date compareDate = isoFormat.parse(date);
        Assert.assertEquals(date, compareDate);
    }

    @And("enddate should be {string}")
    public void enddateShouldBe(String date) {
        String compareDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.element.getEnddate());
        Assert.assertEquals(date, compareDate);
    }

    @And("description should be {string}")
    public void descriptionShouldBe(String description) {
        Assert.assertEquals(description, this.element.getDescription());
    }

    @Then("an error {string} should be shown")
    public void anErrorShouldBeShown(String errorMessage) {
        Assert.assertEquals(errorMessage, this.readYaml.getErrorMessages().get(0));
        Assert.assertThat(this.readYaml.getErrorMessages().get(0), matchesPattern(errorMessage));
    }
}
