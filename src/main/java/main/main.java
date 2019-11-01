package main;

import EXIFFile.ReadEXIF;
import Imaging.WriteExifMetadataExample;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import java.io.File;
import java.io.IOException;

public class main {

    public static void main(String[] args) throws ImageProcessingException, IOException {
//        File file = new File("src/main/resources/SonyA77.jpg");
//        Metadata metadata = ImageMetadataReader.readMetadata(file);
//
//        for (Directory directory : metadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                System.out.format("[%s] - %s = %s\n",
//                        directory.getName(), tag.getTagName(), tag.getDescription());
//            }
//            if (directory.hasErrors()) {
//                for (String error : directory.getErrors()) {
//                    System.err.format("ERROR: %s\n", error);
//                }
//            }
//        }
        ReadEXIF readEXIF = new ReadEXIF("Z:\\workspace\\RenameEXIFFiles\\src\\main\\resources\\SonyA6300.ARW");
        System.out.format("%s:\t%s\n", "FileType", readEXIF.GetFileType());
        System.out.format("%s:\t%tF  %tT\n", "Create Date/Time", readEXIF.GetCreateDateTime(), readEXIF.GetCreateDateTime());
    }
}
