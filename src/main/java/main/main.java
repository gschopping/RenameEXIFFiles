package main;

import EXIFFile.ReadYaml;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class main {
    static String address = "Eiffeltower";

    public static void main(String[] args) {
        ReadYaml readYaml = null;
        try {
            readYaml = new ReadYaml("Z:\\workspace\\resources\\start.yml");
            System.out.println(readYaml.getTimeLocation(0));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
