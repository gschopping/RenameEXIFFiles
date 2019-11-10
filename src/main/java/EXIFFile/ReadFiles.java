package EXIFFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ReadFiles {
    final String regexMedia = "^(.*\\.MP4|.*\\.ARW|.*\\.JPG|.*\\.DNG|.*\\.M2TS)$";
    private String path;

    public ReadFiles(String path) {
        this.path = path;
    }

    public List<File> ReadFromDirectory() {
        File dir = new File(this.path);
        File [] files = dir.listFiles();
        List<File> result = new ArrayList<File>();

        Arrays.sort( files, (Comparator) (o1, o2) -> {

            if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                return +1;
            } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                return -1;
            } else {
                return 0;
            }
        });

        for (File child : files) {
            if (child.getName().toUpperCase().matches(regexMedia)) {
                result.add(child);
            }
        }
        return result;

    }




}
