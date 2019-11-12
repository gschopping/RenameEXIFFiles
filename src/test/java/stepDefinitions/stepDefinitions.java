package stepDefinitions;

import EXIFFile.*;
import com.drew.imaging.ImageProcessingException;
import io.cucumber.java.en.*;
import junit.framework.AssertionFailedError;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static org.hamcrest.Matchers.matchesPattern;

public class stepDefinitions {
    private String mediaFile;
    private String copyFile;
    private ReadEXIF readEXIF;
    private Map<String, String> address;
    private Double latitude;
    private Double longitude;
    private ReadYaml readYaml;
    private ReadYaml.timeLine element;
    private ReadFiles readFiles;
    private List<File> files;
    private String errorMessage;
    private String title;

    // CreateDate feature =========================================================

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

    // WriteTags feature =========================================================

    @Given("file to read {string} and write {string}")
    public void fileToReadAndWrite(String readFile, String writeFile) {
        this.mediaFile = readFile;
        this.copyFile = writeFile;
    }


    @When("write Author {string}")
    public void writeAuthor(String author) throws IOException {
        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\" + this.mediaFile, false);
        writeEXIF.SetAuthor(author);
        writeEXIF.WriteFile("Z:\\workspace\\resources\\" + this.copyFile, true);
    }

    @Then("tag {string} should contain {string}")
    public void tagShouldContain(String tag, String value) throws IOException {
        ReadEXIF readEXIF = new ReadEXIF("Z:\\workspace\\resources\\" + this.copyFile);
        String result = readEXIF.GetTag(tag);
        Assert.assertEquals(value, result);

    }

    @When("write Title {string}")
    public void writeTitle(String title) throws IOException {
        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\" + this.mediaFile, false);
        writeEXIF.SetTitle(title);
        writeEXIF.WriteFile("Z:\\workspace\\resources\\" + this.copyFile, true);
    }

    @When("write Keys {string}")
    public void writeKeys(String keys) throws IOException {
        String[] results = keys.split(",");
        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\" + this.mediaFile, false);
        writeEXIF.SetKeys(results);
        writeEXIF.WriteFile("Z:\\workspace\\resources\\" + this.copyFile, true);
    }

    @When("write Country {string}")
    public void writeCountry(String country) throws IOException {
        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\" + this.mediaFile, false);
        writeEXIF.SetCountry(country);
        writeEXIF.WriteFile("Z:\\workspace\\resources\\" + this.copyFile, true);
    }

    @When("write City {string}")
    public void writeCity(String city) throws IOException {
        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\" + this.mediaFile, false);
        writeEXIF.SetCity(city);
        writeEXIF.WriteFile("Z:\\workspace\\resources\\" + this.copyFile, true);
    }

    @When("write Title {string} but not delete existing file")
    public void writeTitleButNotDeleteExistingFile(String title) throws IOException, ParseException {
        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\" + this.mediaFile, false);
        writeEXIF.SetTitle(title);
        char postfix = ' ';
        boolean noError = false;
        ReadEXIF readEXIF = new ReadEXIF("Z:\\workspace\\resources\\" + this.mediaFile);
        while (! noError) {
            if (postfix == ' ') {
                this.copyFile = String.format("%s %s.%s",
                        readEXIF.getCreateDateTimeString(),
                        title,
                        FilenameUtils.getExtension(this.mediaFile));
            }
            else {
                this.copyFile = String.format("%s%c %s.%s",
                        readEXIF.getCreateDateTimeString(),
                        postfix,
                        title,
                        FilenameUtils.getExtension(this.mediaFile));
            }
            try {
                writeEXIF.WriteFile("Z:\\workspace\\resources\\results\\" + this.copyFile, false);
                noError = true;
            } catch (Exception e) {
                if (e.getMessage().matches("^(.* already exists)$")) {
                    if (postfix == ' ') {
                        postfix = 'a';
                    } else {
                        postfix += 1;
                    }
                }
                else {
                    noError = true;
                }
            }
        }
    }

    @Then("new file should be {string}")
    public void newFileShouldBe(String newFile) {
        File file = new File("Z:\\workspace\\resources\\results\\" + this.copyFile);
        if (! file.delete()) {
            Assert.fail();
        }
        Assert.assertEquals(newFile, this.copyFile);
    }

    // GPS Tags feature =========================================================

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

    @When("read GPS tags and write address information to file {string}")
    public void readGPSTagsAndWriteAddressInformationToFile(String writeFile) throws IOException {
        this.longitude = readEXIF.GetGPSLongitude();
        this.latitude = readEXIF.GetGPSLatitude();
        this.copyFile = writeFile;
        this.address = OpenStreetMapUtils.getInstance().getAddress(this.latitude, this.longitude);
        if (this.address != null) {
            WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\" + this.mediaFile, false);
            writeEXIF.SetCountryCode(this.address.get("countrycode"));
            writeEXIF.SetCountry(this.address.get("country"));
            writeEXIF.SetCity(this.address.get("city"));
            writeEXIF.SetProvince(this.address.get("province"));
            writeEXIF.SetLocation(this.address.get("location"));
            writeEXIF.WriteFile("Z:\\workspace\\resources\\" + writeFile, true);
        }
    }

    // Read Yaml feature =========================================================

    @Given("configuration file {string}")
    public void configurationFile(String configFile) throws FileNotFoundException, ParseException {
        try {
            this.readYaml = new ReadYaml("Z:\\workspace\\resources\\" + configFile);
        }
        catch (Exception e) {
            this.errorMessage = e.getMessage();
        }
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

    @When("date and time is {string}")
    public void dateAndTimeIs(String dateString) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        this.element = this.readYaml.getTimeLine(date);
        if (this.element == null) {
            this.errorMessage = String.format("%s is outside range timelines", dateString);
        }
    }

    @Then("an error {string} should be shown")
    public void anErrorShouldBeShown(String errorMessage) {
        Assert.assertEquals(errorMessage, this.errorMessage);
    }

    // Directory feature =========================================================

    @Given("directory {string}")
    public void directory(String directory) {
        this.readFiles = new ReadFiles(directory);
    }

    @When("read all media files")
    public void readAllMediaFiles() {
        this.files = this.readFiles.getFilesFromDirectory();
    }

    @Then("the number of files should be {int}")
    public void theNumberOfFilesShouldBe(int count) {
        Assert.assertEquals(count, this.files.size());
    }

    @And("the first file should be {string}")
    public void theFirstFileShouldBe(String fileName) {
        Assert.assertEquals(fileName, this.files.get(0).getName());
    }

    @And("the last file should be {string}")
    public void theLastFileShouldBe(String fileName) {
        Assert.assertEquals(fileName, this.files.get(this.files.size()-1).getName());
    }

    @When("read Timelaps subdirectories")
    public void readTimelapsSubdirectories() {
        this.files = this.readFiles.getTimelapsDirectories();
    }

    @Then("the number of directories should be {int}")
    public void theNumberOfDirectoriesShouldBe(int count) {
        Assert.assertEquals(count, this.files.size());
    }

}
