package nl.schoepping.RenameEXIFFiles;

import nl.schoepping.EXIFFile.RenameFiles;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RenameEXIFFiles {

    private static Logger log = Logger.getLogger("RenameEXIFFiles");

    public static void main(String[] args) {

        if (args.length > 2) {
            String configFile = args[0];
            String startdir = args[1];
            String log4jConfigFile = args[2];

            PropertyConfigurator.configure(log4jConfigFile);
            log.info(String.format("Configuration file:\t%s", configFile));
            log.info(String.format("Start directory:\t%s", startdir));
            log.info(String.format("Homedir:\t%s", System.getProperty("user.dir")));



            try {
                RenameFiles renameFiles = new RenameFiles(log, startdir, configFile);
                renameFiles.RenameRootFiles();
                renameFiles.RenameTimelapsFiles(true);
                renameFiles.RenameTimelapsFiles(false);
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
        else
            System.out.println("Missing arguments");

    }


}
