package nl.schoepping.EXIFFile;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReadEXIF {
    final private String regexTimeZone = "^([+-])(\\d{2}):(\\d{2})$";
    final private String regexGPS = "^([\\d.]+) ([NESW])$";
    final private String regexEXIF = "^(\\S+)(\\s*): (.+)$";
    private String exiftool = "exiftool";
    private Logger logger = null;
    private String mediaFile;
    private String fileType;
    private EXIFInfo exifInfo = null;

    public class EXIFInfo {
        private Date CreationDate = null;
        private Double Latitude = 0.0;
        private Double Longitude = 0.0;

        public Date getCreationDate() {
            return CreationDate;
        }

        public void setCreationDate(Date creationDate) {
            CreationDate = creationDate;
        }

        public Double getLatitude() {
            return Latitude;
        }

        public void setLatitude(Double latitude) {
            Latitude = latitude;
        }

        public Double getLongitude() {
            return Longitude;
        }

        public void setLongitude(Double longitude) {
            Longitude = longitude;
        }
    }

    public ReadEXIF(String filePath) throws IOException, ParseException {
        this.mediaFile = filePath;
        if (System.getProperty("os.name").contains("Windows")) {
            exiftool = "exiftool.bat";
        } else {
            exiftool = "exiftool";
        }
        this.exifInfo = this.getEXIFInfo();
    }

    private String getSpaceReplacedFileName() {
        return replaceSpaces(this.mediaFile);
    }

    private String replaceSpaces(String fileName) {
        String result = fileName;
        if (System.getProperty("os.name").contains("Windows")) {
            result = "\"" +  fileName + "\"";
        }
        return result;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private EXIFInfo getEXIFInfo() throws IOException, ParseException {
        EXIFInfo result = new EXIFInfo();
        Map<String, String> map = new HashMap<String, String>();
        Process process = null;
        BufferedReader reader;
        String[] cmdString = new String[] { exiftool,
                "-s1",
                "-DateTimeOriginal", "-CreateDate", "-FileModifyDate",
                "-TimeZone",
                "-FileType",
                "-c", "%.6f", "-GPSLatitude", "-GPSLongitude",
                getSpaceReplacedFileName() };
        process = Runtime.getRuntime().exec(cmdString);
        if (process != null) {
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                Pattern pattern = Pattern.compile(regexEXIF);
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    map.put(matcher.group(1),matcher.group(3));
                }
                line = reader.readLine();
            }
            reader.close();

            // Read creationdate
            String dateString = "";
            String timeZone = "+00:00";
            if (map.get("FileType").equals("JPEG")) {
                dateString = map.get("DateTimeOriginal");
            } else if (map.get("FileType").equals("ARW")) {
                dateString = map.get("DateTimeOriginal");
            } else if (map.get("FileType").equals("DNG")) {
                dateString = map.get("DateTimeOriginal");
            } else if (map.get("FileType").equals("MP4")) {
                dateString = map.get("CreateDate");
                timeZone = map.get("TimeZone");
                if (timeZone == null) {
                    timeZone = "+00:00";
                }
            } else if (map.get("FileType").equals("M2TS")) {
                dateString = map.get("DateTimeOriginal");
            } else if (map.get("FileType").equals("AVI")) {
                dateString = map.get("FileModifyDate");
            }
            if ((dateString != null) && (! dateString.isEmpty())) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                Date date = simpleDateFormat.parse(dateString);
                Pattern pattern = Pattern.compile(regexTimeZone);
                Matcher matcher = pattern.matcher(timeZone);
                int hours = 0;
                int minutes = 0;
                if (matcher.matches()) {
                    String sign = matcher.group(1);
                    hours = Integer.parseInt(matcher.group(2));
                    if (sign.equals("-")) {
                        hours = -1 * hours;
                    }
                    minutes = Integer.parseInt(matcher.group(3));
                }
                date = DateUtils.addHours(date, hours);
                result.setCreationDate(DateUtils.addMinutes(date, minutes));
            }

            // read GPS data

            String latitudeString = map.get("GPSLatitude");
            String longitudeString = map.get("GPSLongitude");
            if ((latitudeString != null) && (longitudeString != null)) {
                double latitude = 0.0;
                double longitude = 0.0;
                Pattern pattern = Pattern.compile(regexGPS);
                Matcher matcher = pattern.matcher(latitudeString);
                if (matcher.matches()) {
                    latitude = Double.parseDouble(matcher.group(1));
                    if (matcher.group(2).equals("S")) {
                        latitude = latitude * -1;
                    }
                    result.setLatitude(latitude);
                }
                matcher = pattern.matcher(longitudeString);
                if (matcher.matches()) {
                    longitude = Double.parseDouble(matcher.group(1));
                    if (matcher.group(2).equals("S")) {
                        longitude = longitude * -1;
                    }
                    result.setLongitude(longitude);
                }
            }
        }
        return result;

    }

    public String getTag(String tag) throws IOException {
        String result = "";
        BufferedReader reader;
        String line;
        String[] cmdString = new String[] { exiftool,  "-charset", "IPTC=UTF8", "-s3", "-" + tag, getSpaceReplacedFileName() };
        Process process = Runtime.getRuntime().exec(cmdString);
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        line = reader.readLine();
        if (line != null) {
            result = line;
        }
        reader.close();
        return result;
    }

    public Date getCreateDateTime() {
        return this.exifInfo.getCreationDate();
    }

    public Double getGPSLatitude() {
        return this.exifInfo.getLatitude();
    }

    public Double getGPSLongitude() {
        return this.exifInfo.getLongitude();
    }

    public String getCreateDateTimeString() throws IOException, ParseException, InterruptedException {
        return new SimpleDateFormat("yyyyMMdd-HHmmss").format(this.getCreateDateTime());
    }

    public String getCreateDateString() throws IOException, ParseException, InterruptedException {
        return new SimpleDateFormat("yyyyMMdd").format(this.getCreateDateTime());
    }



}
