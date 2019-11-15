package main;

import EXIFFile.RenameFiles;

public class main {

    public static void main(String[] args) {

        String configFile = "X:\\Onbewerkt\\start.yml";
        String startdir = "X:\\Onbewerkt";

        try {
            RenameFiles renameFiles = new RenameFiles(startdir, configFile);
            renameFiles.RenameRootFiles();
            renameFiles.RenameTimelapsFiles();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
