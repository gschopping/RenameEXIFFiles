package main;

import EXIFFile.ReadAddress;
import EXIFFile.ReadYaml;
import org.checkerframework.checker.units.qual.Time;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class main {

    public class Customer {
        private String firstName;
//        private String lastName;
//        private int age;
//        private List<Contact> contactDetails;
//        private Address homeAddress;
        // getters and setters


        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

//        public String getLastName() {
//            return lastName;
//        }
//
//        public void setLastName(String lastName) {
//            this.lastName = lastName;
//        }
//
//        public int getAge() {
//            return age;
//        }
//
//        public void setAge(int age) {
//            this.age = age;
//        }
//
//        public List<Contact> getContactDetails() {
//            return contactDetails;
//        }
//
//        public void setContactDetails(List<Contact> contactDetails) {
//            this.contactDetails = contactDetails;
//        }
//
//        public Address getHomeAddress() {
//            return homeAddress;
//        }
//
//        public void setHomeAddress(Address homeAddress) {
//            this.homeAddress = homeAddress;
//        }

    }

    public static void main(String[] args) throws IOException {
        final String regexMedia = "^(.*\\.MP4|.*\\.ARW|.*\\.JPG|.*\\.DNG|.*\\.M2TS)$";
//        final String regexMedia = "(.*)";
        String path = "Z:\\workspace\\resources";

//        Files.find(Paths.get(path),
//                Integer.MAX_VALUE,
//                (filePath, fileAttr) -> fileAttr.isRegularFile())
//                .forEach(System.out::println);

        File dir = new File(path);
        File [] files = dir.listFiles();

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
                System.out.println(child.getName());
            }
        }


//        ReadYaml readYaml = new ReadYaml("Z:\\workspace\\resources\\invalid_variable.yml");
//        for (String errorMessage : readYaml.getErrorMessages()) {
//            System.out.println(errorMessage);
//        }
//        for (ReadYaml.timeLine timeLine : readYaml.getTimeLines()) {
//            System.out.println(timeLine.getStartdate() +  "  ==> " + timeLine.getEnddate());
//            System.out.println(timeLine.getDescription());
//            System.out.println(timeLine.getCountry());
//            System.out.println(timeLine.getCountrycode());
//        }

    }

}
