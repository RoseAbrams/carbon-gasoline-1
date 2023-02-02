package se.gaioa.gasoline.carbon.location;

import java.io.Serializable;
import java.util.Arrays;

public class City implements Serializable {

    volatile String ID;
    protected String[] synonyms = new String[1];
    public final String SUBCOUNTRY;
    public final String COUNTRY;
    public final double LATITUDE;
    public final double LONGITUDE;
    static City[] CACHE;

    protected City(String ID, String name, String subcountry, String country, double latitude, double longitude) {
        this.ID = ID;
        SUBCOUNTRY = subcountry;
        COUNTRY = country;
        LATITUDE = latitude;
        LONGITUDE = longitude;
        synonyms[0] = name;
    }

    public String getName() {
        return synonyms[0];
    }

    public String getName(int i) {
        return synonyms[i];
    }

    protected void addSynonym(String n) {
        synonyms = Arrays.copyOf(synonyms, synonyms.length + 1);
        synonyms[synonyms.length - 1] = n;
    }

    @Override
    public String toString() {
        return getName();
    }
}
