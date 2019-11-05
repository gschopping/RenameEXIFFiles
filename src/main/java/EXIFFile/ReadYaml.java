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
    private timeLines timeLines;
    private Object objectYaml;
    private Iterable<Object> objectAll;

    public class timeLine {
        private String startdate;
        private String title;
        private String countrycode;
        private String country;
        private String province;
        private String city;
        private String location;
        private String description;
        private String creator;
        private String website;
        private String copyright;

        public String getStartdate() {
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

        public String getCopyright() {
            return copyright;
        }

        public void setStartdate(String startdate) {
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

        public void setCopyRight(String copyright) {
            this.copyright = copyright;
        }
    }

    public class  DefaultLocation {
        private String countrycode;
        private String country;
        private String province;
        private String city;
        private String creator;
        private String website;
        private String copyright;

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

        public String getCopyright() {
            return copyright;
        }

        public void setCopyRight(String copyright) {
            this.copyright = copyright;
        }
    }

    public  class  timeLines {
//        private DefaultLocation defaultLocation;
//        private Object defaultLocation;
        private List<timeLine> timeline;

        public List<timeLine> getTimeLine() {
            return timeline;
        }

        public void setTimeLine(List<timeLine> timeline) {
            this.timeline = timeline;
        }

//        public Object getDefaultLocation() {
//            return defaultLocation;
//        }

//        public void setDefaultLocation(Object defaultLocation) {
//            this.defaultLocation = defaultLocation;
//        }
    }

    public ReadYaml(String configFile) throws FileNotFoundException {
        InputStream input = new FileInputStream(new File(configFile));
        Yaml yaml = new Yaml(new Constructor(timeLines.class));
        this.timeLines = yaml.load(input);
//        Yaml yaml = new Yaml();
//        this.objectYaml = yaml.load(input);
//        this.objectAll = yaml.loadAll(input);
    }

    public timeLine getTimeLine(int index) {
        return this.timeLines.getTimeLine().get(index);

    }

    public Object getObjectYaml() {
        return this.objectYaml;
    }

    public Iterable<Object> getObjectAllYaml() {
        return this.objectAll;
    }
}
