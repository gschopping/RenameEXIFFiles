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
    private List<TimeLine> timeLines;

    public class TimeLine {
        private Date startdate = null;
        private Date enddate = null;
        private String title = "";
        private String countrycode = "";
        private String country = "";
        private String province = "";
        private String city = "";
        private String location = "";
        private String description = "";
        private String author = "";
        private String website = "";
        private String copyright = "";
        private String comment = "";
        private String keys = "";
        private String instructions = "";

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
            if (! this.countrycode.isEmpty()) {
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

        public String getAuthor() {
            return author;
        }

        public String getWebsite() {
            return website;
        }

        public String getCopyright() {
            return copyright;
        }

        public String getComment() {
            return comment;
        }

        public String getKeys() {
            return keys;
        }

        public String getInstructions() {
            return instructions;
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

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public void setCopyRight(String copyright) {
            this.copyright = copyright;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setKeys(String keys) {
            this.keys = keys;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }
    }

    class SortbyDate implements Comparator<TimeLine> {
        @Override
        public int compare(TimeLine o1, TimeLine o2) {
            return o1.getStartdate().compareTo(o2.getStartdate());
        }

    }

    public ReadYaml(String configFile) throws Exception {
//        this.errorMessages = new ArrayList<String>();
        this.timeLines = new ArrayList<TimeLine> ();
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
        TimeLine timeline = new TimeLine();
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
            timeline.setAuthor(value);
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
        if (item.get("comment") != null) {
            value = (String) item.get("comment");
            timeline.setComment(value);
        }
        if (item.get("instructions") != null) {
            value = (String) item.get("instructions");
            timeline.setInstructions(value);
        }
        if (item.get("keys") != null) {
            value = (String) item.get("keys");
            timeline.setKeys(value);
        }
        if (timeline.startdate == null) {
            throw new Exception("startdate is not filled");
        }
        addTimeline(timeline);
    }

    private void addTimeline(TimeLine timeline) throws Exception {
        for (TimeLine element : this.timeLines) {
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

    public List<TimeLine> getTimeLines() {
        Collections.sort(this.timeLines, new SortbyDate());
        setEnddate();
        return this.timeLines;
    }

    public TimeLine getTimeLine(Date date) {
        TimeLine result = null;
        for (TimeLine timeline : this.getTimeLines()) {
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
