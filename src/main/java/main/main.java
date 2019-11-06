package main;

import EXIFFile.ReadAddress;
import EXIFFile.ReadYaml;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

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
//        Yaml yaml = new Yaml();
//        String document = "\n- Hesperiidae\n- Papilionidae\n- Apatelodidae\n- Epiplemidae";
//        List<String> list = (List<String>) yaml.load(document);
//        System.out.println(list.get(0));

        String document = "firstName: \"John\"\n" +
                "lastName: \"Doe\"\n" +
                "age: 31\n";


        Constructor constructor = new Constructor(Customer.class);
//        TypeDescription customerDescription = new TypeDescription(Customer.class);
        Yaml yaml = new Yaml();
        InputStream input = Files.newInputStream(Path.of("Z:\\workspace\\resources\\address.yaml"));
        Map customer = yaml.load(input);
        System.out.println(customer.get("homeAddress"));
    }

}
