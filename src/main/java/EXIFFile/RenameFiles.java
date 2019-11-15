package EXIFFile;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class RenameFiles {
    private String startDirectory;
    private String configFile;

    public RenameFiles(String startDirectory, String configFile) {
        this.startDirectory = startDirectory;
        this.configFile = configFile;
    }

    private void RenameFile(ReadEXIF readEXIF, ReadYaml.TimeLine timeline, File file) throws Exception {
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
                writeEXIF.writeFile(this.startDirectory + "\\results\\" + newFileName, false);
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
                    throw new Exception(e.getMessage());
                }
            }
        }

    }

    private void RenameFileWithAddress(Map<String, String> address, ReadYaml.TimeLine timeline, String dateString, int counter, File file) throws Exception {
        char postfix=' ';
        // write all tags
        WriteEXIF writeEXIF = new WriteEXIF(file.getPath(), true);
        writeEXIF.setAddress(address);
        writeEXIF.setAuthor(timeline.getAuthor());
        writeEXIF.setCopyright(timeline.getCopyright());
//        writeEXIF.setComment(timeline.getComment());
//        writeEXIF.setDescription(timeline.getDescription());
//        writeEXIF.setSpecialInstructions(timeline.getInstructions());
        boolean noError = false;
        String newFileName = "";
        while (! noError) {
            if (postfix == ' ') {
                newFileName = String.format("%s-%04d %s.%s",
                        dateString,
                        counter,
                        address.get("location"),
                        FilenameUtils.getExtension(file.getName()));

            }
            else {
                newFileName = String.format("%s%c-%04d %s.%s",
                        dateString,
                        postfix,
                        counter,
                        address.get("location"),
                        FilenameUtils.getExtension(file.getName()));
            }
            System.out.println(String.format("%s =>\t%s\t(%s, %s, %s)", file.getName(), newFileName,
                    address.get("country"), address.get("city"), address.get("location")));
            try {
                writeEXIF.writeFile(this.startDirectory + "\\results\\" + newFileName, false);
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
                    throw new Exception(e.getMessage());
                }
            }
        }

    }

    public void RenameRootFiles() throws Exception {
        ReadYaml readYaml = new ReadYaml(configFile);
        ReadFiles readFiles = new ReadFiles(this.startDirectory);
        for (File file : readFiles.getFilesFromDirectory()) {
            ReadEXIF readEXIF = new ReadEXIF(file.getPath());
// timeline is found, now you can set all information in mediafile
// create new name for mediafile
            ReadYaml.TimeLine timeline = readYaml.getTimeLine(readEXIF.getCreateDateTime());
            if (timeline != null) {
                RenameFile(readEXIF, timeline, file);
            } else {
                System.out.println(String.format("%s with %tF %tT has a creationdate before all timelines",
                        file.getName(), readEXIF.getCreateDateTime(), readEXIF.getCreateDateTime()));
            }
        }
    }

    public void RenameTimelapsFiles() throws Exception {
        ReadYaml readYaml = new ReadYaml(this.configFile);
        // read timelaps subdirectories
        ReadFiles readFiles = new ReadFiles(this.startDirectory);
        int counter = 1;
        for (File dir : readFiles.getTimelapsDirectories()) {
            List<File> timelapsFiles = readFiles.getTimelapsFiles(dir);
            Map<String, String> address;
            String dateString = "";
            if (timelapsFiles.size() > 0) {
                File file = timelapsFiles.get(timelapsFiles.size() - 1);
                ReadEXIF readEXIF = new ReadEXIF(file.getPath());
                // read from Yaml file in order to retrieve some default information
                ReadYaml.TimeLine timeline = readYaml.getTimeLine(readEXIF.getCreateDateTime());
                if (timeline != null) {
                    // retrieve if possible GPS coordinates
                    Double latitude = readEXIF.getGPSLatitude();
                    Double longitude = readEXIF.getGPSLongitude();
                    address = OpenStreetMapUtils.getInstance().getAddress(latitude, longitude);
                    dateString = readEXIF.getCreateDateString();
                    for (File timelapsFile : timelapsFiles) {
                        if (address != null) {
                            RenameFileWithAddress(address, timeline, dateString, counter, timelapsFile);
                        }
                    }
                } else {
                    System.out.println(String.format("%s with %tF %tT has a creationdate before all timelines",
                            file.getName(), readEXIF.getCreateDateTime(), readEXIF.getCreateDateTime()));
                }
            } else {
                System.out.println(String.format("No files in %s", dir.getPath()));
            }
        }
    }
}
