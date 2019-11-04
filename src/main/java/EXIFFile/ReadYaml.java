package EXIFFile;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReadYaml {
    private TimeLine objYaml;

    public class TimeLocation {
        private Date startdate;
        private String title;
        private String countrycode;
        private String country;
        private String province;
        private String city;
        private String location;
        private String description;
        private String creator;
        private String website;
        private String copyRight;

        public Date getStartdate() {
            return startdate;
        }

        public String getTitle() {
            return title;
        }

        public String getCountrycode() {
            return countrycode;
        }

        public String getCountry() {
            return country;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getLocation() {
            return location;
        }

        public String getDescription() {
            return description;
        }

        public String getCreator() {
            return creator;
        }

        public String getWebsite() {
            return website;
        }

        public String getCopyRight() {
            return copyRight;
        }

        public void setStartdate(Date startdate) {
            this.startdate = startdate;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCountrycode(String countrycode) {
            this.countrycode = countrycode;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public void setCopyRight(String copyRight) {
            this.copyRight = copyRight;
        }
    }

    public class  DefaultLocation {
        private String countrycode;
        private String country;
        private String province;
        private String city;
        private String creator;
        private String website;
        private String copyRight;

        public String getCountrycode() {
            return countrycode;
        }

        public void setCountrycode(String countrycode) {
            this.countrycode = countrycode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getCopyRight() {
            return copyRight;
        }

        public void setCopyRight(String copyRight) {
            this.copyRight = copyRight;
        }
    }

    public  class  TimeLine {
        private DefaultLocation defaultLocation;
        private List<TimeLocation> timeLocation;

        public List<TimeLocation> getTimeLocation() {
            return timeLocation;
        }

        public void setTimeLocation(List<TimeLocation> timeLocation) {
            this.timeLocation = timeLocation;
        }

        public DefaultLocation getDefaultLocation() {
            return defaultLocation;
        }

        public void setDefaultLocation(DefaultLocation defaultLocation) {
            this.defaultLocation = defaultLocation;
        }
    }

    public ReadYaml(String configFile) throws FileNotFoundException {
        InputStream input = new FileInputStream(new File(configFile));
        Yaml yaml = new Yaml(new Constructor(TimeLine.class));
        this.objYaml = yaml.load(input);
    }

    public TimeLocation getTimeLocation(int index) {
        return this.objYaml.getTimeLocation().get(index);

    }
}
