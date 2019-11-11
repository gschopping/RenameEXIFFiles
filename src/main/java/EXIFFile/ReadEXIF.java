package EXIFFile;

import org.apache.commons.lang.time.DateUtils;

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
    private String mediaFile;
    private String fileType;

    public ReadEXIF(String filePath) throws IOException {
        this.mediaFile = filePath;
        this.fileType = GetFileType();
    }

    private String GetFileType() throws IOException {
        String fileType;
        Process process = Runtime.getRuntime().exec("exiftool.bat -s3 -File:FileType " + this.mediaFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        fileType = reader.readLine();
        reader.close();
        return fileType;
    }

    public String GetTag(String tag) throws IOException {
        String result = "";
        BufferedReader reader;
        String line;
        Process process = Runtime.getRuntime().exec("exiftool.bat -charset IPTC=UTF8 -s3 -" + tag + " " + this.mediaFile);
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        line = reader.readLine();
        if (line != null) {
            result = line;
        }
        reader.close();
        return result;
    }

    private String GetTimeZone() throws IOException {
        String timezone = "+00:00";
        if (this.fileType.equals("MP4")) {
            BufferedReader reader;
            String line;
            Process process = Runtime.getRuntime().exec("exiftool.bat -s3 -QuickTime:TimeZone " + this.mediaFile);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = reader.readLine();
            if ((line != null) && line.matches(regexTimeZone)) {
                timezone = line;
            }
            reader.close();
        }
        return timezone;
    }

    public Date GetCreateDateTime() throws IOException, ParseException {
        Date date = null;
        Process process = null;
        BufferedReader reader;
        String line;
        if (this.fileType.equals("JPEG")) {
            process = Runtime.getRuntime().exec("exiftool.bat -s3 -EXIF:DateTimeOriginal " + this.mediaFile);
        } else if (this.fileType.equals("ARW")) {
            process = Runtime.getRuntime().exec("exiftool.bat -s3 -EXIF:DateTimeOriginal " + this.mediaFile);
        } else if (this.fileType.equals("DNG")) {
            process = Runtime.getRuntime().exec("exiftool.bat -s3 -EXIF:DateTimeOriginal " + this.mediaFile);
        } else if (this.fileType.equals("MP4")) {
            process = Runtime.getRuntime().exec("exiftool.bat -s3 -QuickTime:CreateDate " + this.mediaFile);
        } else if (this.fileType.equals("M2TS")) {
            process = Runtime.getRuntime().exec("exiftool.bat -s3 -H264:DateTimeOriginal " + this.mediaFile);
        }
        if (process != null) {
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            line = reader.readLine();
            reader.close();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            date = simpleDateFormat.parse(line);
            String timeZone = this.GetTimeZone();
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

    public Double GetGPSLatitude() throws IOException {
        double result = 0.0;
        BufferedReader reader;
        String line;
        Process process = Runtime.getRuntime().exec("exiftool.bat -c \"%.6f\" -Composite:GPSLatitude -s3 " + this.mediaFile);
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        line = reader.readLine();
        if (line != null) {
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

    public Double GetGPSLongitude() throws IOException {
        double result = 0.0;
        BufferedReader reader;
        String line;
        Process process = Runtime.getRuntime().exec("exiftool.bat -c \"%.6f\" -Composite:GPSLongitude -s3 " + this.mediaFile);
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        line = reader.readLine();
        if (line != null) {
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

    public String getCreateDateTimeString() throws IOException, ParseException {
        return new SimpleDateFormat("yyyyMMdd-HHmmss").format(this.GetCreateDateTime());

    }



}
