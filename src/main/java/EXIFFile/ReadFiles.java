package EXIFFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ReadFiles {
    private final String regexMedia = "^(.*\\.MP4|.*\\.ARW|.*\\.JPG|.*\\.DNG|.*\\.M2TS|.*\\.AVI)$";
    private final String regexTimelaps = "^(Timelaps\\d+)$";
    private final String regexGPS = "^(GPS\\d+)$";
    private final String regexTimelapsFile = "^(.*\\.ARW|.*\\.JPG|.*\\.DNG)$";
    private String path;

    public ReadFiles(String path) {
        this.path = path;
    }

    public List<File> getFilesFromDirectory() {
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

    public List<File> getTimelapsDirectories() {
        File dir = new File(this.path);
        File[] files = dir.listFiles(File::isDirectory);
        List<File> result = new ArrayList<File>();

        for (File child : files) {
            if (child.getName().matches(regexTimelaps)) {
                result.add(child);
            }
        }
        return result;
    }

    public List<File> getGPSDirectories() {
        File dir = new File(this.path);
        File[] files = dir.listFiles(File::isDirectory);
        List<File> result = new ArrayList<File>();

        for (File child : files) {
            if (child.getName().matches(regexGPS)) {
                result.add(child);
            }
        }
        return result;
    }

    public List<File> getTimelapsFiles(File dirTimelaps) {
        File dir = new File(dirTimelaps.getPath());
        File[] files = dir.listFiles();
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
            if (child.getName().toUpperCase().matches(regexTimelapsFile)) {
                result.add(child);
            }
        }
        return result;
    }




}
