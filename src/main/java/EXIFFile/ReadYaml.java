package EXIFFile;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadYaml {
    private List<timeLine> timeLines;

    public class timeLine {
        private Date startdate;
        private Date enddate;
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

        public Date getStartdate() {
            return startdate;
        }

        public Date getEnddate() { return enddate; }

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

        public void setStartdate(Date startdate) {
            this.startdate = startdate;
        }

        public void setEnddate(Date enddate) {
            this.enddate = enddate;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCountrycode(String countrycode) {
            Locale obj = new Locale("", countrycode);
            if (obj != null) {
                this.countrycode = obj.getCountry();
            } else {
                this.countrycode = "";
            }
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

    class SortbyDate implements Comparator<timeLine> {
        @Override
        public int compare(timeLine o1, timeLine o2) {
            return o1.getStartdate().compareTo(o2.getStartdate());
        }

    }

    public ReadYaml(String configFile) throws FileNotFoundException, ParseException {
        this.timeLines = new ArrayList<timeLine> ();
        InputStream input = new FileInputStream(new File(configFile));
        Yaml yaml = new Yaml();
        Map timeLine = yaml.load(input);
        if (timeLine.get("timeline") != null) {
            ArrayList<Map> timelineArray = (ArrayList<Map>) timeLine.get("timeline");
            for (Map timelineItem : timelineArray) {
                setTimeLine(timelineItem);
            }
        }
    }

    private void setTimeLine(Map item) throws ParseException {
        timeLine timeline = new timeLine();
        String value = "";
        if (item.get("countrycode") != null) {
            value = (String) item.get("countrycode");
            timeline.setCountrycode(value);
        }
        if (item.get("country") != null) {
            value = (String) item.get("country");
            timeline.setCountry(value);
        }
        if (item.get("province") != null) {
            value = (String) item.get("province");
            timeline.setProvince(value);
        }
        if (item.get("city") != null) {
            value = (String) item.get("city");
            timeline.setCity(value);
        }
        if (item.get("creator") != null) {
            value = (String) item.get("creator");
            timeline.setCreator(value);
        }
        if (item.get("website") != null) {
            value = (String) item.get("website");
            timeline.setWebsite(value);
        }
        if (item.get("copyright") != null) {
            value = (String) item.get("copyright");
            timeline.setCopyRight(value);
        }
        if (item.get("startdate") != null) {
            Date date = (Date) item.get("startdate");
            timeline.setStartdate(date);
        }
        if (item.get("title") != null) {
            value = (String) item.get("title");
            timeline.setTitle(value);
        }
        if (item.get("location") != null) {
            value = (String) item.get("location");
            timeline.setLocation(value);
        }
        if (item.get("description") != null) {
            value = (String) item.get("description");
            timeline.setDescription(value);
        }
        timeLines.add(timeline);
    }

    private void setEnddate() {
        Date date = null;
        for (int count = this.timeLines.size() - 1; count >= 0; count--) {
            if (count < this.timeLines.size() - 1) {
                this.timeLines.get(count).setEnddate(date);
            }
            date = this.timeLines.get(count).getStartdate();
        }
    }

    public List<timeLine> getTimeLines() {
        Collections.sort(this.timeLines, new SortbyDate());
        setEnddate();
        return this.timeLines;
    }

}
