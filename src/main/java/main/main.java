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
            readYaml = new ReadYaml("Z:\\workspace\\resources\\start_plain.yml");
            System.out.println(readYaml.getTimeLine(0));
//            Iterable<Object> objectIterable = readYaml.getObjectAllYaml();
//            for (Object o : objectIterable) {
//                System.out.println("element type: " + o.getClass());
//                System.out.println(o);
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
