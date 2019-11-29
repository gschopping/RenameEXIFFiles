package nl.schoepping.EXIFFile;

import org.apache.commons.lang.time.DateUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReadEXIF {
    private String exiftool;
    private String mediaFile;
    private EXIFInfo exifInfo;

    public static class EXIFInfo {
        private Date CreationDate = null;
        private Double Latitude = 0.0;
        private Double Longitude = 0.0;

        Date getCreationDate() {
            return CreationDate;
        }

        void setCreationDate(Date creationDate) {
            CreationDate = creationDate;
        }

        Double getLatitude() {
            return Latitude;
        }

        void setLatitude(Double latitude) {
            Latitude = latitude;
        }

        Double getLongitude() {
            return Longitude;
        }

        void setLongitude(Double longitude) {
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

    private EXIFInfo getEXIFInfo() throws IOException, ParseException {
        EXIFInfo result = new EXIFInfo();
        Map<String, String> map = new HashMap<>();
        BufferedReader reader;
        String[] cmdString = new String[] { exiftool,
                "-s1",
                "-DateTimeOriginal", "-CreateDate", "-FileModifyDate", "-GPSDateTime",
                "-TimeZone",
                "-FileType",
                "-c", "%.6f", "-GPSLatitude", "-GPSLongitude",
                getSpaceReplacedFileName() };
        Process process = Runtime.getRuntime().exec(cmdString);
        if (process == null) {
            throw new IOException("Can't execute " + Arrays.toString(cmdString));
        }
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        while (line != null) {
            String regexEXIF = "^(\\S+)(\\s*): (.+)$";
            Pattern pattern = Pattern.compile(regexEXIF);
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                map.put(matcher.group(1),matcher.group(3));
            }
            line = reader.readLine();
        }
        reader.close();

        // Read creationdate
        String dateString = null;
        String timeZone = "+00:00";
        switch (map.get("FileType")) {
            case "JPEG":
                dateString = map.get("DateTimeOriginal");
                break;
            case "ARW":
                dateString = map.get("DateTimeOriginal");
                break;
            case "DNG":
                dateString = map.get("DateTimeOriginal");
                break;
            case "MP4":
                dateString = map.get("CreateDate");
                timeZone = map.get("TimeZone");
                if (timeZone == null) {
                    timeZone = "+00:00";
                }
                break;
            case "M2TS":
                dateString = map.get("DateTimeOriginal");
                break;
            case "AVI":
                dateString = map.get("FileModifyDate");
                break;
        }
        // if dateString is still empty then try another tag
        if (dateString == null) {
            dateString = map.get("GPSDateTime");
            if (dateString == null) {
                dateString = map.get("FileModifyDate");
            }
        }

        if (dateString != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            Date date = simpleDateFormat.parse(dateString);
            String regexTimeZone = "^([+-])(\\d{2}):(\\d{2})$";
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
            double latitude;
            double longitude;
            String regexGPS = "^([\\d.]+) ([NESW])$";
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

    public String getCreateDateTimeString() throws Exception {
        if (this.getCreateDateTime() == null) {
            throw new Exception("No creation date is found");
        }
        return new SimpleDateFormat("yyyyMMdd-HHmmss").format(this.getCreateDateTime());
    }

    String getCreateDateString() throws Exception {
        if (this.getCreateDateTime() == null) {
            throw new Exception("No creation date is found");
        }
        return new SimpleDateFormat("yyyyMMdd").format(this.getCreateDateTime());
    }



}
