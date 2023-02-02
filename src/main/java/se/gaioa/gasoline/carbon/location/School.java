package se.gaioa.gasoline.carbon.location;

import java.util.Arrays;

public class School extends City {

    protected String[] synonyms = new String[1];
    static City[] CACHE;

    protected School(String ID, String name, String city, String subcountry, String country, double latitude, double longitude) {
        super(ID, city, subcountry, country, latitude, longitude);
        synonyms[0] = name;
    }

    @Override
    public String getName() {
        return synonyms[0];
    }

    @Override
    public String getName(int i) {
        return synonyms[i];
    }

    @Override
    protected void addSynonym(String n) {
        synonyms = Arrays.copyOf(synonyms, synonyms.length + 1);
        synonyms[synonyms.length - 1] = n;
    }
}
