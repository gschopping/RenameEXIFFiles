package EXIFFile;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadYaml {
    private final String regexParser = "line (\\d+), column (\\d+):\n^(\\s*)(.+)$";
    private final String regexDateParser = "^Unparseable date: \"(.+)\"$";
    private List<timeLine> timeLines;
//    private List<String> errorMessages;

    public class timeLine {
        private Date startdate = null;
        private Date enddate = null;
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
            if ((this.country == null) && (this.countrycode != null)) {
                Locale obj = new Locale("", this.countrycode);
                return obj.getDisplayCountry();
            } else {
                return country;
            }
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

        public void setStartdate(String startdate) throws ParseException {
            this.startdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startdate);
        }

        public void setEnddate(Date enddate) {

            this.enddate = enddate;
            ;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCountrycode(String countrycode) throws Exception {
            Locale obj = new Locale("", countrycode);
            try {
                String code = obj.getISO3Country();
            }
            catch (Exception e){
                throw new Exception(String.format("countrycode: %s is not valid", countrycode));
            }
            this.countrycode = obj.getCountry();
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

    public ReadYaml(String configFile) throws Exception {
//        this.errorMessages = new ArrayList<String>();
        this.timeLines = new ArrayList<timeLine> ();
        int lineCount = 0;
        try {
            InputStream input = new FileInputStream(new File(configFile));
            Yaml yaml = new Yaml();
            Map timeLine = yaml.load(input);
            if (timeLine.get("timeline") != null) {
                ArrayList<Map> timelineArray = (ArrayList<Map>) timeLine.get("timeline");
                for (Map timelineItem : timelineArray) {
                    lineCount++;
                    setTimeLine(timelineItem);
                }
            }
        }
        catch (Exception e) {
            String errorType = e.getClass().getName();
            if (errorType.equals("org.yaml.snakeyaml.parser.ParserException")) {
                Pattern pattern = Pattern.compile(regexParser, Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(e.getMessage());
                int line = 0;
                int column = 0;
                String sentence = "";
                if (matcher.find()) {
                    line = Integer.parseInt(matcher.group(1));
                    column = Integer.parseInt(matcher.group(2));
                    sentence = matcher.group(4);
                }
                throw new Exception(String.format("Error on line %d, column %d: %s", line, column, sentence));
            }
            else if (errorType.equals("java.io.FileNotFoundException")) {
                throw new Exception(String.format("%s not found",configFile));
            }
            else if (errorType.equals("java.text.ParseException")) {
                Pattern pattern = Pattern.compile(regexDateParser);
                Matcher matcher = pattern.matcher(e.getMessage());
                String sentence = "";
                if (matcher.find()) {
                    sentence = matcher.group(1);
                }
                throw new Exception(String.format("Error in timeline %d, incorrect dateformat: %s", lineCount, sentence));
            }
            else if (errorType.equals("java.lang.Exception")) {
                throw new Exception(String.format("Error in timeline %d, %s", lineCount, e.getMessage()));
            }
            else if (errorType.equals("org.yaml.snakeyaml.composer.ComposerException")) {
                Pattern pattern = Pattern.compile(regexParser, Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(e.getMessage());
                int line = 0;
                int column = 0;
                String sentence = "";
                if (matcher.find()) {
                    line = Integer.parseInt(matcher.group(1));
                    column = Integer.parseInt(matcher.group(2));
                    sentence = matcher.group(4);
                }
                throw new Exception(String.format("Error on line %d, column %d: undefined alias %s", line, column, sentence));
            }
            else {
                throw new Exception(String.format("Error in timeline %d: %s", lineCount, e.getMessage()));
            }
        }
    }

    private void setTimeLine(Map item) throws Exception {
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
            value = (String) item.get("startdate");
            timeline.setStartdate(value);
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
        if (timeline.startdate == null) {
            throw new Exception("startdate is not filled");
        }
        addTimeline(timeline);
    }

    private void addTimeline(timeLine timeline) throws Exception {
        for (timeLine element : this.timeLines) {
            if (timeline.getStartdate().equals(element.getStartdate())) {
                throw new Exception(String.format("startdate: %tF %tT already exists", timeline.getStartdate(), timeline.getStartdate()));
            }
        }
        this.timeLines.add(timeline);
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

    public timeLine getTimeLine(Date date) {
        timeLine result = null;
        for (timeLine timeline : this.getTimeLines()) {
            if ((date.compareTo(timeline.getStartdate()) >= 0) &&
                    ((timeline.getEnddate() == null) ||
                    (date.compareTo(timeline.getEnddate()) < 0))){
                result = timeline;
                break;
            }
        }
        return result;
    }

}
