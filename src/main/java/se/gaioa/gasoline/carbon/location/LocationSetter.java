package se.gaioa.gasoline.carbon.location;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import se.gaioa.gasoline.carbon.SearchFrame;

public class LocationSetter {

    public static final File SER = new File("C:\\Users\\Gustav\\Downloads\\location cache.ser");
    private static final String API_KEY = "AIzaSyBIMlFN-DHSrjQgWYOpHNQdWNiJHP7xONk";

    private static JSONObject query(String q) throws MalformedURLException, IOException {
        return new JSONObject(new String(new URL(
                "https://maps.googleapis.com/maps/api/geocode/json?address="
                + q.replace(' ', '+') + "&key=" + API_KEY)
                .openConnection().getInputStream().readAllBytes(), StandardCharsets.UTF_8));
    }

    public static void initCache() {
        if (SER.exists()) {
            Object[] locations = (Object[]) SearchFrame.loadSer(SER);
            City.CACHE = (City[]) locations[0];
            School.CACHE = (School[]) locations[1];
        }
    }

    public static void main(String[] args) throws IOException {
//        example();
        City[] cities = (City[]) processList(0);
        School[] schools = (School[]) processList(1);
        City[][] both = {cities, schools};
        SearchFrame.saveSer(both, SER);
    }

    private static City[] processList(int type) throws IOException {
        ArrayList<City> output = new ArrayList<>();
        BufferedReader I = new BufferedReader(new InputStreamReader(new FileInputStream(
                "C:\\Users\\Gustav\\Downloads\\" + (type == 0 ? "city" : "edu") + ".txt"), "UTF-8"));
        while (true) {
            String query = I.readLine();
            if (query == null) {
                break;
            }
            JSONObject response = query(URLEncoder.encode(query, StandardCharsets.UTF_8));
            if (!response.getString("status").equals("OK")) {
                System.err.println(query + " - Query got response status '" + response.getString("status") + "'");
            } else {
                String city = null;
                String subcountry = "";
                String country = null;
                double latitude;
                double longitude;
                JSONArray address = response.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                for (int i = 0; i < address.length(); i++) {
                    JSONObject p = address.getJSONObject(i);
                    if (p.getJSONArray("types").getString(0).equals("country")) {
                        country = p.getString("long_name");
                    }
                    if (p.getJSONArray("types").get(0).toString().equals("administrative_area_level_1")) {
                        subcountry = p.getString("long_name");
                    }
                    if (p.getJSONArray("types").getString(0).equals("locality") || p.getJSONArray("types").getString(0).equals("postal_town")) {
                        if (city != null) {
                            throw new Error(query + " - Duplicate city name!");
                        }
                        city = p.getString("long_name");
                    }
                }
                if (country == null) {
                    throw new IllegalArgumentException(query + " - No country in result!");
                }
                if (city == null) {
                    throw new IllegalArgumentException(query + " - No city in result!");
                }
                JSONObject coords = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                latitude = coords.getDouble("lat");
                longitude = coords.getDouble("lng");
                String ID = response.getJSONArray("results").getJSONObject(0).getString("place_id");
                boolean foundLocation = false;
                boolean foundSynonym = false;
                for (City lC : output) {
                    if (lC.ID.equals(ID)) {
                        foundLocation = true;
                        for (String synonym : lC.synonyms) {
                            if (synonym.equals(query)) {
                                foundSynonym = true;
                            }
                        }
                        if (!foundSynonym) {
                            lC.addSynonym(query);
                        }
                    }
                }
                if (!foundLocation) {
                    City lC;
                    switch (type) {
                        case 0:
                            lC = new City(ID, city, subcountry, country, latitude, longitude);
                            if (!city.equals(query)) {
                                lC.addSynonym(query);
                            }
                            break;
                        case 1:
                            lC = new School(ID, query, city, subcountry, country, latitude, longitude);
                            break;
                        default:
                            throw new AssertionError();
                    }
                    output.add(lC);
                }
            }
        }
        return output.toArray(new City[0]);
    }

    private static void example() throws IOException {
        String[] queries = {"Kopenhamn", "Malmoe University", "Tiergarten",
            "Buckingham Palace", "Lagos", "Lagos University Teaching Hospital",
            "Massachusetts Institute of Technology", "MIT", "Copenhagen Business School", "CBS"};
        for (String q : queries) {
            JSONObject response = query(q);
            JSONObject result = response.getJSONArray("results").getJSONObject(0);
            JSONObject coords = result.getJSONObject("geometry").getJSONObject("location");
            JSONArray address = result.getJSONArray("address_components");
            System.out.println("For the query '" + q + "':");
            System.out.print("\tHas types: ");
            for (Object typeS : result.getJSONArray("types").toList()) {
                System.out.print(typeS.toString() + ' ');
            }
            System.out.println();
            System.out.println("\tAt coordinates: " + coords.getDouble("lat") + ' ' + coords.getDouble("lng"));
            System.out.println("\tAddress properties:");
            for (int i = 0; i < address.length(); i++) {
                JSONObject c = address.getJSONObject(i);
                System.out.print("\t\t" + c.getJSONArray("types").getString(0) + " = " + c.getString("long_name"));
                if (!c.getString("long_name").equals(c.getString("short_name"))) {
                    System.out.print(" (" + c.getString("short_name") + ")");
                }
                System.out.println();
            }
        }
    }
}
