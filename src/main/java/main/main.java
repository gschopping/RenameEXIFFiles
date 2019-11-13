package main;

import EXIFFile.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class main {

    public static void RenameFile(ReadEXIF readEXIF, ReadYaml.TimeLine timeline, File file, String dir) throws IOException, ParseException {
        char postfix=' ';
        // write all tags
        WriteEXIF writeEXIF = new WriteEXIF(file.getPath(), true);
        writeEXIF.setCountryCode(timeline.getCountrycode());
        writeEXIF.setCountry(timeline.getCountry());
        writeEXIF.setProvince(timeline.getProvince());
        writeEXIF.setCity(timeline.getCity());
        writeEXIF.setLocation(timeline.getLocation());
        writeEXIF.setTitle(timeline.getTitle());
        writeEXIF.setDescription(timeline.getDescription());
        writeEXIF.setCopyright(timeline.getCopyright());
        writeEXIF.setAuthor(timeline.getAuthor());
        writeEXIF.setURL(timeline.getWebsite());
        writeEXIF.setComment(timeline.getComment());
        writeEXIF.setSpecialInstructions(timeline.getInstructions());
        String[] keys = timeline.getKeys().split(",");
        writeEXIF.setKeys(keys);
        boolean noError = false;
        String newFileName = "";
        while (! noError) {
            if (postfix == ' ') {
                newFileName = String.format("%s %s.%s",
                        readEXIF.getCreateDateTimeString(),
                        timeline.getTitle(),
                        FilenameUtils.getExtension(file.getName()));

            }
            else {
                newFileName = String.format("%s%c %s.%s",
                        readEXIF.getCreateDateTimeString(),
                        postfix,
                        timeline.getTitle(),
                        FilenameUtils.getExtension(file.getName()));
            }
            System.out.println(String.format("%s =>\t%s\t(%s, %s, %s, %s)", file.getName(), newFileName,
                    timeline.getCountry(), timeline.getCity(), timeline.getLocation(), timeline.getDescription()));
            try {
                writeEXIF.writeFile(dir + "\\results\\" + newFileName, false);
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

    public static void RenameFileWithAddress(Map<String, String> address, String dateString, String len, int counter, File file, String dir) throws IOException, ParseException {
        char postfix=' ';
        // write all tags
        WriteEXIF writeEXIF = new WriteEXIF(file.getPath(), true);
        writeEXIF.setAddress(address);
        boolean noError = false;
        String newFileName = "";
        while (! noError) {
            if (postfix == ' ') {
                newFileName = String.format("%s-%" + len + "d %s.%s",
                        dateString,
                        counter,
                        address.get("location"),
                        FilenameUtils.getExtension(file.getName()));

            }
            else {
                newFileName = String.format("%s%c-%" + len + "d %s.%s",
                        dateString,
                        postfix,
                        counter,
                        address.get("location"),
                        FilenameUtils.getExtension(file.getName()));
            }
            System.out.println(String.format("%s =>\t%s\t(%s, %s, %s)", file.getName(), newFileName,
                    address.get("country"), address.get("city"), address.get("location")));
            try {
                writeEXIF.writeFile(dir + "\\results\\" + newFileName, false);
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


    public static void main(String[] args) {

        String configFile = "X:\\Onbewerkt\\start.yml";
        String startdir = "X:\\Onbewerkt";

// read configuration file
        try {
            ReadYaml readYaml = new ReadYaml(configFile);

// read all files in startdir
            ReadFiles readFiles = new ReadFiles(startdir);
            for (File file : readFiles.getFilesFromDirectory()) {
                ReadEXIF readEXIF = new ReadEXIF(file.getPath());
// timeline is found, now you can set all information in mediafile
// create new name for mediafile
                try {
                    ReadYaml.TimeLine timeline = readYaml.getTimeLine(readEXIF.getCreateDateTime());
                    if (timeline != null) {
                        try {
                            RenameFile(readEXIF, timeline, file, startdir);
                        }
                        catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    else {
                        System.out.println(String.format("%s with %tF %tT has a creationdate before all timelines",
                                file.getName(), readEXIF.getCreateDateTime(), readEXIF.getCreateDateTime()));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

// read timelaps subdirectories
            int counter = 1;
            for (File dir : readFiles.getTimelapsDirectories()) {
                List<File> timelapsFiles = readFiles.getTimelapsFiles(dir);
                // retrieve if possible GPS coordinates
                Map<String, String> address = null;
                String dateString = "";
                if (timelapsFiles.size() > 0) {
                    File file = timelapsFiles.get(timelapsFiles.size()-1);
                    ReadEXIF readEXIF = new ReadEXIF(file.getPath());
                    Double latitude = readEXIF.getGPSLatitude();
                    Double longitude = readEXIF.getGPSLongitude();
                    address = OpenStreetMapUtils.getInstance().getAddress(latitude, longitude);
                    dateString = readEXIF.getCreateDateString();
                }
                for (File file : timelapsFiles) {
                    if (address != null) {
                        RenameFileWithAddress(address, dateString, "3", counter, file, dir.getPath());
                    }

                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
