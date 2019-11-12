package main;

import EXIFFile.ReadEXIF;
import EXIFFile.ReadFiles;
import EXIFFile.ReadYaml;
import EXIFFile.WriteEXIF;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class main {


    public static void main(String[] args) throws IOException {

        char postfix=' ';

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
                    ReadYaml.timeLine timeline = readYaml.getTimeLine(readEXIF.GetCreateDateTime());
                    if (timeline != null) {
                        // write all tags
                        writeEXIF.SetCountryCode(timeline.getCountrycode());
                        writeEXIF.SetCountry(timeline.getCountry());
                        writeEXIF.SetProvince(timeline.getProvince());
                        writeEXIF.SetCity(timeline.getCity());
                        writeEXIF.SetLocation(timeline.getLocation());
                        writeEXIF.SetTitle(timeline.getTitle());
                        writeEXIF.SetDescription(timeline.getDescription());
                        writeEXIF.SetCopyright(timeline.getCopyright());
                        writeEXIF.SetAuthor(timeline.getCreator());
                        writeEXIF.SetURL(timeline.getWebsite());
                        writeEXIF.SetComment(timeline.getComment());
                        writeEXIF.SetSpecialInstructions(timeline.getInstructions());
                        String[] keys = timeline.getKeys().split(",");
                        writeEXIF.SetKeys(keys);
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
                                writeEXIF.WriteFile(startdir + "\\results\\" + newFileName, false);
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
                    else {
                        System.out.println(String.format("%s with %tF %tT has a creationdate before all timelines",
                                file.getName(), readEXIF.GetCreateDateTime(), readEXIF.GetCreateDateTime()));
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
