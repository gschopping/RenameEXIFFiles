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
                    ReadYaml.timeLine timeline = readYaml.getTimeLine(readEXIF.GetCreateDateTime());
                    if (timeline != null) {
                        String newFileName = String.format("%s %s.%s",
                                readEXIF.getCreateDateTimeString(),
                                timeline.getTitle(),
                                FilenameUtils.getExtension(file.getName()));
                        System.out.println(String.format("%s =>\t%s\t(%s, %s, %s, %s)", file.getName(), newFileName,
                                timeline.getCountry(), timeline.getCity(), timeline.getLocation(), timeline.getDescription()));
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
