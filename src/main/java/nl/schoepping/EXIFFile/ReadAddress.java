package nl.schoepping.EXIFFile;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadAddress {
    private Customer customer;

    public class Contact {
        private String type;
        private int number;
        // getters and setters

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    public class Address {
        private String line;
        private String city;
        private String state;
        private Integer zip;
        // getters and setters

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Integer getZip() {
            return zip;
        }

        public void setZip(Integer zip) {
            this.zip = zip;
        }
    }

    public class Customer {
        private String firstName;
        private String lastName;
        private int age;
//        private List<Contact> contactDetails;
//        private Address homeAddress;
        // getters and setters


        public Customer(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

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

    public ReadAddress(String configFile) throws IOException {
        Yaml yaml = new Yaml(new Constructor(Customer.class));
//        InputStream input = this.getClass().getClassLoader().getResourceAsStream(configFile);
//        InputStream input = new FileInputStream(new File(configFile));
        InputStream input = Files.newInputStream(Path.of(configFile));
        this.customer = yaml.loadAs(input, Customer.class);

//        final YAMLMapper mapper = new YAMLMapper(new YAMLFactory());
//        mapper.findAndRegisterModules();
//        final File file = new File(configFile);
//        this.customer = mapper.readValue(file, Customer.class);
    }

    public Customer getCustomer() {
         return this.customer;
    }

}
