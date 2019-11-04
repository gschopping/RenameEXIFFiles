package main;

import EXIFFile.OpenStreetMapUtils;
import EXIFFile.ReadEXIF;
import EXIFFile.WriteEXIF;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class main {
    static String address = "Eiffeltower";

    public static void main(String[] args) throws IOException {
//        Map<String, Double> coords;
//        coords = OpenStreetMapUtils.getInstance().getCoordinates(address);
//        if (coords != null) {
//            System.out.println("latitude :" + coords.get("lat"));
//            System.out.println("longitude:" + coords.get("lon"));
//        }

//        ReadEXIF readEXIF = new ReadEXIF("Z:\\workspace\\resources\\results\\SonyA77.ARW");
//        Double latitude = readEXIF.GetGPSLatitude();
//        System.out.println(latitude);
//        Double longitude = readEXIF.GetGPSLongitude();
//        System.out.println(longitude);
//
//        Map<String, String> address;
//        address = OpenStreetMapUtils.getInstance().getAddress(latitude, longitude);
//        if (address != null) {
//            System.out.println("address :\t" + address.get("address"));
//            System.out.println("street :\t" + address.get("street"));
//            System.out.println("location :\t" + address.get("location"));
//            System.out.println("postcode :\t" + address.get("postcode"));
//            System.out.println("city :\t" + address.get("city"));
//            System.out.println("province :\t" + address.get("province"));
//            System.out.println("country :\t" + address.get("country"));
//            System.out.println("countrycode :\t" + address.get("countrycode"));
//        }

        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {

            Locale obj = new Locale("", countryCode);

            System.out.println("Country Code = " + obj.getISO3Country()
                    + ", Country Name = " + obj.getDisplayCountry());

        }

        System.out.println("Done");


//        WriteEXIF writeEXIF = new WriteEXIF("Z:\\workspace\\resources\\results\\SonyA77.ARW");
//        writeEXIF.SetAuthor("Воронеж");
//        writeEXIF.SetCountry("Israël");
//        writeEXIF.WriteFile();
    }


}
