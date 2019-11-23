package nl.schoepping.EXIFFile;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReadEXIF {
    final private String regexTimeZone = "^([+-])(\\d{2}):(\\d{2})$";
    final private String regexGPS = "^([\\d.]+) ([NESW])$";
    private String exiftool = "exiftool";
    private Logger logger = null;
    private String mediaFile;
    private String fileType;

    public ReadEXIF(String filePath) throws IOException {
        this.mediaFile = filePath;
        this.fileType = getFileType();
        if (System.getProperty("os.name").contains("Windows")) {
            exiftool = "exiftool.bat";
        } else {
            exiftool = "exiftool";
        }
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

    private String getFileType() throws IOException {
        String line;
        String[] cmdString = new String[] { exiftool, "-s3", "-File:FileType", getSpaceReplacedFileName() };
        Process process = Runtime.getRuntime().exec(cmdString);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        line = reader.readLine();
        reader.close();
        return line;
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

    private String getTimeZone() throws IOException {
        String timezone = "+00:00";
        if (this.fileType.equals("MP4")) {
            BufferedReader reader;
            String line;
            String[] cmdString = new String[] { exiftool, "-s3", "-QuickTime:TimeZone", getSpaceReplacedFileName() };
            Process process = Runtime.getRuntime().exec(cmdString);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = reader.readLine();
            if ((line != null) && line.matches(regexTimeZone)) {
                timezone = line;
            }
            reader.close();
        }
        return timezone;
    }

    public Date getCreateDateTime() throws IOException, ParseException {
        if (logger != null) {
            logger.debug("ReadEXIF: getCreateDateTime");
        }
        Date date = null;
        Process process = null;
        BufferedReader reader;
        String line;
        String tag = "";
        if (this.fileType.equals("JPEG")) {
            tag = "-EXIF:DateTimeOriginal";
        } else if (this.fileType.equals("ARW")) {
            tag = "-EXIF:DateTimeOriginal";
        } else if (this.fileType.equals("DNG")) {
            tag = "-EXIF:DateTimeOriginal";
        } else if (this.fileType.equals("MP4")) {
            tag = "-QuickTime:CreateDate";
        } else if (this.fileType.equals("M2TS")) {
            tag = "-H264:DateTimeOriginal";
        } else if (this.fileType.equals("AVI")) {
            tag = "-File:FileModifyDate";
        }
        if (! tag.isEmpty()) {
            String[] cmdString = new String[] { exiftool, "-s3", tag, getSpaceReplacedFileName() };
            process = Runtime.getRuntime().exec(cmdString);
        }
        if (logger != null) {
            logger.debug("ReadEXIF: getCreateDateTime (after DateTime) " + this.fileType);
        }
        if (process != null) {
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = reader.readLine();
            reader.close();
            if (logger != null) {
                logger.debug("ReadEXIF: getCreateDateTime " + line);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            date = simpleDateFormat.parse(line);
            String timeZone = this.getTimeZone();
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
            date = DateUtils.addMinutes(date, minutes);
        }
        return date;
    }

    public Double getGPSLatitude() throws IOException {
        double result = 0.0;
        BufferedReader reader;
        String line;
        String[] cmdString = new String[] { exiftool, "-c", "%.6f", "-GPSLatitude", "-s3", getSpaceReplacedFileName() };
        Process process = Runtime.getRuntime().exec(cmdString);
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        line = reader.readLine();
        if (line != null) {
            if (logger != null) {
                logger.debug("ReadEXIF: getGPSLatitude " + line);
            }
            Pattern pattern = Pattern.compile(regexGPS);
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                result = Double.parseDouble(matcher.group(1));
                if (matcher.group(2).equals("S")) {
                    result = result * -1;
                }
            }
        }
        reader.close();
        return result;
    }

    public Double getGPSLongitude() throws IOException {
        double result = 0.0;
        BufferedReader reader;
        String line;
        String[] cmdString = new String[] { exiftool, "-c", "%.6f", "-GPSLongitude", "-s3", getSpaceReplacedFileName() };
        Process process = Runtime.getRuntime().exec(cmdString);
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        line = reader.readLine();
        if (line != null) {
            if (logger != null) {
                logger.debug("ReadEXIF: getGPSLongitude " + line);
            }
            Pattern pattern = Pattern.compile(regexGPS);
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                result = Double.parseDouble(matcher.group(1));
                if (matcher.group(2).equals("W")) {
                    result = result * -1;
                }
            }
        }
        reader.close();
        return result;
    }

    public String getCreateDateTimeString() throws IOException, ParseException, InterruptedException {
        return new SimpleDateFormat("yyyyMMdd-HHmmss").format(this.getCreateDateTime());
//        return new SimpleDateFormat("yyyyMMddHHmmss").format(this.getCreateDateTime());
    }

    public String getCreateDateString() throws IOException, ParseException, InterruptedException {
        return new SimpleDateFormat("yyyyMMdd").format(this.getCreateDateTime());
    }



}
