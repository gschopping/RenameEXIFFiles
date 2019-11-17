package EXIFFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Date;
import java.util.List;

public class RenameFiles {
    private Logger logger;
    private String startDirectory;
    private String configFile;

    public RenameFiles(Logger logger, String startDirectory, String configFile) {
        this.logger = logger;
        this.startDirectory = startDirectory;
        this.configFile = configFile;
    }

    private void RenameFile(OpenStreetMapUtils.Address address, ReadYaml.TimeLine timeline, String dateString, String directory, int counter, File file, boolean fromTimeline) throws Exception {
        char postfix=' ';
        String title = "";
        String country = "";
        String city = "";
        String location = "";
        // write all tags
        WriteEXIF writeEXIF = new WriteEXIF(file.getPath(), true);
        if ((address != null) && address.getIsSet()) {
            writeEXIF.setAddress(address);
            title = address.getLocation();
            country = address.getCountry();
            city = address.getCity();
            location = address.getLocation();
        }
        else {
            writeEXIF.setCountryCode(timeline.getCountrycode());
            writeEXIF.setCountry(timeline.getCountry());
            writeEXIF.setProvince(timeline.getProvince());
            writeEXIF.setCity(timeline.getCity());
            writeEXIF.setLocation(timeline.getLocation());
            title = timeline.getTitle();
            country = timeline.getCountry();
            city = timeline.getCity();
            location = timeline.getLocation();
        }
        writeEXIF.setCopyright(timeline.getCopyright());
        writeEXIF.setAuthor(timeline.getAuthor());
        writeEXIF.setURL(timeline.getWebsite());
        if (fromTimeline) {
            writeEXIF.setTitle(timeline.getTitle());
            writeEXIF.setDescription(timeline.getDescription());
            writeEXIF.setComment(timeline.getComment());
            writeEXIF.setSpecialInstructions(timeline.getInstructions());
            String[] keys = timeline.getKeys().split(",");
            writeEXIF.setKeys(keys);
        }
        else {
            writeEXIF.setTitle(title);
        }
        boolean noError = false;
        String newFileName = "";
        while (! noError) {
            if (postfix == ' ') {
                if (counter > 0) {
                    newFileName = String.format("%s-%04d %s.%s",
                            dateString,
                            counter,
                            title,
                            FilenameUtils.getExtension(file.getName()));
                }
                else {
                    newFileName = String.format("%s %s.%s",
                            dateString,
                            title,
                            FilenameUtils.getExtension(file.getName()));
                }
            }
            else {
                if (counter > 0) {
                    newFileName = String.format("%s%c-%04d %s.%s",
                            dateString,
                            postfix,
                            counter,
                            title,
                            FilenameUtils.getExtension(file.getName()));
                }
                else {
                    newFileName = String.format("%s%c %s.%s",
                            dateString,
                            postfix,
                            title,
                            FilenameUtils.getExtension(file.getName()));
                }
            }
            if (logger != null) {
                logger.info(String.format("%s =>\t%s\t(%s, %s, %s)", file.getName(), newFileName,
                        country, city, location));
            }
            System.out.println(String.format("%s =>\t%s\t(%s, %s, %s)", file.getName(), newFileName,
                    country, city, location));
            try {
                writeEXIF.writeFile(directory + "\\results\\" + newFileName, false);
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

    private void RenameTimelapsFile(OpenStreetMapUtils.Address address, ReadYaml.TimeLine timeline, String dateString, String subdir, int counter, File file) throws Exception {
        char postfix=' ';
        String title = "";
        String country = "";
        String city = "";
        String location = "";
        // write all tags
        WriteEXIF writeEXIF = new WriteEXIF(file.getPath(), true);
        if ((address != null) && address.getIsSet()) {
            writeEXIF.setAddress(address);
            writeEXIF.setTitle(title);
            title = address.getLocation();
            country = address.getCountry();
            city = address.getCity();
            location = address.getLocation();
        }
        else {
            writeEXIF.setCountryCode(timeline.getCountrycode());
            writeEXIF.setCountry(timeline.getCountry());
            writeEXIF.setProvince(timeline.getProvince());
            writeEXIF.setCity(timeline.getCity());
            writeEXIF.setLocation(timeline.getLocation());
            title = timeline.getTitle();
            country = timeline.getCountry();
            city = timeline.getCity();
            location = timeline.getLocation();
        }
        writeEXIF.setTitle(title);
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
                        title,
                        FilenameUtils.getExtension(file.getName()));

            }
            else {
                newFileName = String.format("%s%c-%04d %s.%s",
                        dateString,
                        postfix,
                        counter,
                        title,
                        FilenameUtils.getExtension(file.getName()));
            }
            if (logger != null) {
                this.logger.info(String.format("%s =>\t%s\t(%s, %s, %s)", file.getName(), newFileName,
                        country, city, location));
            }
            System.out.println(String.format("%s =>\t%s\t(%s, %s, %s)", file.getName(), newFileName,
                    country, city, location));
            try {
                writeEXIF.writeFile(subdir + "\\results\\" + newFileName, false);
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
                String dateString = readEXIF.getCreateDateTimeString();
                Double latitude = readEXIF.getGPSLatitude();
                Double longitude = readEXIF.getGPSLongitude();
                OpenStreetMapUtils.Address address = OpenStreetMapUtils.getInstance().getAddress(latitude, longitude);
                RenameFile(address, timeline, dateString, this.startDirectory, 0, file, true);
            } else {
                if (logger != null) {
                    this.logger.info(String.format("%s with %tF %tT has a creationdate before all timelines",
                            file.getName(), readEXIF.getCreateDateTime(), readEXIF.getCreateDateTime()));
                }
                System.out.println(String.format("%s with %tF %tT has a creationdate before all timelines",
                        file.getName(), readEXIF.getCreateDateTime(), readEXIF.getCreateDateTime()));
            }
        }
    }

    public void RenameTimelapsFiles(boolean counting) throws Exception {
        ReadYaml readYaml = new ReadYaml(this.configFile);
        // read timelaps subdirectories
        ReadFiles readFiles = new ReadFiles(this.startDirectory);
        int counter = 1;
        String referenceDate = "";
        List<File> dirs;
        if (counting) {
            dirs = readFiles.getTimelapsDirectories();
        }
        else {
            dirs = readFiles.getGPSDirectories();
        }
        for (File dir : dirs) {
            List<File> timelapsFiles = readFiles.getTimelapsFiles(dir);
            if (timelapsFiles.size() > 0) {
                // take the last file where GPS data is to be expected and fix the date
                File file = timelapsFiles.get(timelapsFiles.size() - 1);
                ReadEXIF readEXIF = new ReadEXIF(file.getPath());
                // start counter for each new date, keep all filenames unique
                if (referenceDate.isEmpty()) {
                    counter = 1;
                }
                else {
                    if (! referenceDate.equals(readEXIF.getCreateDateString())) {
                        counter = 1;
                    }
                }
                referenceDate = readEXIF.getCreateDateString();
                // read from Yaml file in order to retrieve some default information
                ReadYaml.TimeLine timeline = readYaml.getTimeLine(readEXIF.getCreateDateTime());
                if (timeline != null) {
                    // retrieve if possible GPS coordinates
                    Double latitude = readEXIF.getGPSLatitude();
                    Double longitude = readEXIF.getGPSLongitude();
                    OpenStreetMapUtils.Address address = OpenStreetMapUtils.getInstance().getAddress(latitude, longitude);
                    for (File timelapsFile : timelapsFiles) {
                        if (! counting) {
                            // not counting means no counter will be added instead the time will be used
                            readEXIF = new ReadEXIF(timelapsFile.getPath());
                            referenceDate = readEXIF.getCreateDateTimeString();
                            counter = 0;
                        }
                        RenameFile(address, timeline, referenceDate, dir.getPath(), counter, timelapsFile, false);
//                        RenameTimelapsFile(address, timeline, referenceDate, dir.getPath(), counter, timelapsFile);
                        counter++;
                    }
                } else {
                    if (logger != null) {
                        this.logger.info(String.format("%s with %tF %tT has a creationdate before all timelines",
                                file.getName(), readEXIF.getCreateDateTime(), readEXIF.getCreateDateTime()));
                    }
                    System.out.println(String.format("%s with %tF %tT has a creationdate before all timelines",
                            file.getName(), readEXIF.getCreateDateTime(), readEXIF.getCreateDateTime()));
                }
            } else {
                if (logger != null) {
                    this.logger.info(String.format("No files in %s", dir.getPath()));
                }
                System.out.println(String.format("No files in %s", dir.getPath()));
            }
        }
    }
}
