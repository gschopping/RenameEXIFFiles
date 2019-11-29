package nl.schoepping.EXIFFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class ReadFiles {
    private final String regexMedia = "^(.*\\.MP4|.*\\.ARW|.*\\.JPG|.*\\.DNG|.*\\.M2TS|.*\\.AVI)$";
    private final String regexTimelaps = "^(Timelaps\\d+)$";
    private final String regexGPS = "^(GPS\\d+)$";
    private final String regexTimelapsFile = "^(.*\\.ARW|.*\\.JPG|.*\\.DNG)$";
    private String path;

    public class EXIFFile {
        private File file;
        private File path;
        private Double latitude = 0.0;
        private Double longitude = 0.0;
        private Date creationDate;

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public File getPath() {
            return path;
        }

        void setPath(File path) {
            this.path = path;
        }

        public Double getLatitude() {
            return latitude;
        }

        void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        Date getCreationDate() {
            return creationDate;
        }

        void setCreationDate(Date creationDate) {
            this.creationDate = creationDate;
        }
    }

    public ReadFiles(String path) {
        this.path = path;
    }



    public List<File> getFilesFromDirectory() {
        File dir = new File(this.path);
        File [] files = dir.listFiles();
        List<File> result = new ArrayList<>();

        if (files != null) {
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
        }

        return result;
    }

    public List<File> getTimelapsDirectories() {
        File dir = new File(this.path);
        File[] files = dir.listFiles(File::isDirectory);
        List<File> result = new ArrayList<>();

        if (files != null) {
            for (File child : files) {
                if (child.getName().matches(regexTimelaps)) {
                    result.add(child);
                }
            }
        }
        return result;
    }

    List<File> getGPSDirectories() {
        File dir = new File(this.path);
        File[] files = dir.listFiles(File::isDirectory);
        List<File> result = new ArrayList<>();

        if (files != null) {
            for (File child : files) {
                if (child.getName().matches(regexGPS)) {
                    result.add(child);
                }
            }
        }
        return result;
    }

    List<File> getTimelapsFiles(File dirTimelaps) {
        File dir = new File(dirTimelaps.getPath());
        File[] files = dir.listFiles();
        List<File> result = new ArrayList<>();

        if (files != null) {
            Arrays.sort(files, (Comparator) (o1, o2) -> {

                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return +1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
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
        }
        return result;
    }

    public List<EXIFFile> getFiles() throws IOException, ParseException {
        List<EXIFFile> result = new ArrayList<>();

        Arrays.sort( result.toArray(), (Comparator) (o1, o2) -> ((EXIFFile)o1).getCreationDate().compareTo(((EXIFFile)o2).getCreationDate()));


        // read Root files
        File dir = new File(this.path);
        File [] files = dir.listFiles();

        if (files != null) {
            for (File child : files) {
                if (child.getName().toUpperCase().matches(regexMedia)) {
                    EXIFFile exifFile = new EXIFFile();
                    exifFile.setFile(child);
                    exifFile.setPath(dir);
                    ReadEXIF readEXIF = new ReadEXIF(child.getPath());
                    exifFile.setCreationDate(readEXIF.getCreateDateTime());
                    exifFile.setLatitude(readEXIF.getGPSLatitude());
                    exifFile.setLongitude(readEXIF.getGPSLongitude());
                    result.add(exifFile);
                }
            }
        }

        // read Timelapsand GPS files
        File[] dirs = dir.listFiles(File::isDirectory);

        if (dirs != null) {
            for (File child : dirs) {
                if (child.getName().matches(regexTimelaps) || child.getName().matches(regexGPS)) {
                    File subdir = new File(child.getPath());
                    File[] subfiles = subdir.listFiles();
                    if (subfiles != null) {
                        for (File subchild : subfiles) {
                            if (subchild.getName().toUpperCase().matches(regexTimelapsFile)) {
                                EXIFFile exifFile = new EXIFFile();
                                exifFile.setFile(subchild);
                                exifFile.setPath(subdir);
                                ReadEXIF readEXIF = new ReadEXIF(subchild.getPath());
                                exifFile.setCreationDate(readEXIF.getCreateDateTime());
                                exifFile.setLatitude(readEXIF.getGPSLatitude());
                                exifFile.setLongitude(readEXIF.getGPSLongitude());
                                result.add(exifFile);
                            }
                        }
                    }
                }
            }
        }

        return result;
    }




}
