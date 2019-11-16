package main;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Log4jExample {

    static Logger log = Logger.getLogger(Log4jExample.class.getName());

    public static void main(String[] args) {
//        BasicConfigurator.configure();
        String log4jConfigFile = "C:\\Program Files\\apache-maven-3.6.2\\bin\\log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);
        log.debug("Hello this is a debug message");
        log.info("Hello this is an info message");
    }

}
