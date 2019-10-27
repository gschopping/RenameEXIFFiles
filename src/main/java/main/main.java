package main;

import Imaging.WriteExifMetadataExample;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import java.io.File;
import java.io.IOException;

public class main {

    public static void main(String[] args) throws ImageWriteException, ImageReadException, IOException {
        System.out.println("=============== JPG ================================");
        new WriteExifMetadataExample().changeExifMetadata(new File("src/main/resources/SonyA77.jpg"), new File("src/main/resources/results/SonyA77.jpg"));
        System.out.println("=============== ARW ================================");
        new WriteExifMetadataExample().changeExifMetadata(new File("src/main/resources/SonyA77.ARW"), new File("src/main/resources/results/SonyA77.ARW"));
    }
}
