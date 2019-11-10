package EXIFFile;

//import org.apache.log4j.Logger;
import io.cucumber.java.an.E;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class OpenStreetMapUtils {
//    public final static Logger log = Logger.getLogger("OpenStreetMapUtils");

    private static OpenStreetMapUtils instance = null;

    public OpenStreetMapUtils() {

    }

    public static OpenStreetMapUtils getInstance() {
        if (instance == null) {
            instance = new OpenStreetMapUtils();
        }
        return instance;
    }

    private String getRequest(String url) throws Exception {

        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        con.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        con.setRequestProperty("Accept-Language", "nl,en-US;q=0.7,en;q=0.3");
        con.setRequestProperty("Cache-Control", "max-age=0");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("Host", "nominatim.openstreetmap.org");
        con.setRequestProperty("TE", "Trailers");
        con.setRequestProperty("Upgrade-Insecure-Requests", "1");
        con.setRequestProperty("User-Agent", "Rename mediafiles");

        if (con.getResponseCode() != 200) {
            String respnseMessage = con.getResponseMessage();
            return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public Map<String, String> getAddress(Double latitude, Double longitude) {
        Map<String, String> res;
        StringBuffer query;
        String queryResult = null;

        query = new StringBuffer();
        res = new HashMap<String, String>();

        query.append("https://nominatim.openstreetmap.org/reverse?format=json");

        query.append("&lat=");
        query.append(latitude);
        query.append("&lon=");
        query.append(longitude);
        query.append("&addressdetails=1&zoom=18");

//        log.debug("Query:" + query);

        // don't request api for testcases in order to avoid blocking of the site
        if (latitude.equals(51.454183) && longitude.equals(3.653545)) {
            queryResult = "{\"place_id\":261870388,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":168908204,\"lat\":\"51.4541747279983\",\"lon\":\"3.65353658637693\",\"display_name\":\"Rammekensweg, Ritthem, Vlissingen, Zeeland, Nederland, 4389TZ, Nederland\",\"address\":{\"footway\":\"Rammekensweg\",\"suburb\":\"Ritthem\",\"town\":\"Vlissingen\",\"state\":\"Zeeland\",\"postcode\":\"4389TZ\",\"country\":\"Nederland\",\"country_code\":\"nl\"},\"boundingbox\":[\"51.4528958\",\"51.4549397\",\"3.6533426\",\"3.6540872\"]}";
        } else if (latitude.equals(51.679494) && longitude.equals(4.138041)) {
            queryResult = "{\"place_id\":137959627,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":262113993,\"lat\":\"51.67877565\",\"lon\":\"4.13843211722729\",\"display_name\":\"Strandweg, Bruinisse, Schouwen-Duiveland, Zeeland, Nederland, 4311NE, Nederland\",\"address\":{\"parking\":\"Strandweg\",\"road\":\"Strandweg\",\"suburb\":\"Bruinisse\",\"city\":\"Schouwen-Duiveland\",\"state\":\"Zeeland\",\"postcode\":\"4311NE\",\"country\":\"Nederland\",\"country_code\":\"nl\"},\"boundingbox\":[\"51.678328\",\"51.6792308\",\"4.1379332\",\"4.1389456\"]}";
        } else {
            try {
                queryResult = getRequest(query.toString());
            } catch (Exception e) {
                //            log.error("Error when trying to get data with the following query " + query);
            }
        }

        if (queryResult == null) {
            return null;
        }

        JSONObject jObject = new JSONObject(queryResult);
        String result = "";
        try {
            String displayName = jObject.getString("display_name");
            res.put("address", displayName);
        } catch (Exception e) {
            res.put("address", "[Not found]");
        }
        try {
            JSONObject address = jObject.getJSONObject("address");
            try {
                result = address.getString("road");
                res.put("street", result);
            } catch (Exception e) {
                try {
                    result = address.getString("footway");
                    res.put("street", result);
                } catch (Exception f) {
                    res.put("street", "[Not found]");
                    try {
                        result = address.getString("parking");
                        res.put("street", result);
                    } catch (Exception g) {
                        res.put("street", "[Not found]");
                    }
                }
            }
            try {
                result = address.getString("suburb");
                res.put("location", result);
            } catch (Exception e) {
                res.put("location", "[Not found]");
            }
            try {
                result = address.getString("town");
                res.put("city", result);
            } catch (Exception e) {
                try {
                    result = address.getString("city");
                    res.put("city", result);
                } catch (Exception f) {
                    res.put("city", "[Not found]");
                }
            }
            try {
                result = address.getString("state");
                res.put("province", result);
            } catch (Exception e) {
                res.put("province", "[Not found]");
            }
            try {
                result = address.getString("postcode");
                res.put("postcode", result);
            } catch (Exception e) {
                res.put("postcode", "[Not found]");
            }
            try {
                result = address.getString("country");
                res.put("country", result);
            } catch (Exception e) {
                res.put("country", "[Not found]");
            }
            try {
                result = address.getString("country_code");
                res.put("countrycode", result.toUpperCase());
            } catch (Exception e) {
                res.put("countrycode", "[Not found]");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return res;
    }
}
