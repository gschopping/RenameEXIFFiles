package main;

import EXIFFile.RenameFiles;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class main {

    static Logger log = Logger.getLogger("RenameEXIFFiles");

    public static void main(String[] args) {

        String configFile = "X:\\Onbewerkt\\start.yml";
        String startdir = "X:\\Onbewerkt";
        String log4jConfigFile = "C:\\Program Files\\apache-maven-3.6.2\\bin\\log4j.properties";

        PropertyConfigurator.configure(log4jConfigFile);
        log.info(String.format("Configuration file:\t%s", configFile));
        log.info(String.format("Start dicrectory:\t%s", startdir));


        try {
            RenameFiles renameFiles = new RenameFiles(log, startdir, configFile);
            renameFiles.RenameRootFiles();
            renameFiles.RenameTimelapsFiles(true);
            renameFiles.RenameTimelapsFiles(false);
        }
        catch (Exception e) {
            log.error(e);
//            System.out.println(e.getMessage());
        }

    }

}
