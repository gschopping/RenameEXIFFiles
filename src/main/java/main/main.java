package main;

import EXIFFile.ReadEXIF;
import EXIFFile.ReadFiles;
import EXIFFile.ReadYaml;
import EXIFFile.WriteEXIF;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class main {

    public static void RenameFile(ReadEXIF readEXIF, WriteEXIF writeEXIF, ReadYaml.TimeLine timeline, File file, String dir) throws IOException, ParseException {
        char postfix=' ';
        // write all tags
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
                WriteEXIF writeEXIF = new WriteEXIF(file.getPath(), true);
// timeline is found, now you can set all information in mediafile
// create new name for mediafile
                try {
                    ReadYaml.TimeLine timeline = readYaml.getTimeLine(readEXIF.getCreateDateTime());
                    if (timeline != null) {
                        try {
                            RenameFile(readEXIF, writeEXIF, timeline, file, startdir);
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
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
