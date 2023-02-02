package se.gaioa.gasoline.carbon.location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class LocationString implements CharSequence, Serializable {

    private final String TEXT;
    private final City LOCATION;
    private static final ArrayList<LocationString> CACHE = new ArrayList<>();

    public static LocationString city(String text) {
        return get(text, 0);
    }

    public static LocationString school(String text) {
        return get(text, 1);
    }

    private static LocationString get(String text, int type) {
        for (LocationString ls : CACHE) {
            if (text.equals(ls.TEXT)) {
                return ls;
            }
        }
        LocationString newLS;
        if (text.isBlank()) {
            newLS = new LocationString(text, null);
        } else {
            switch (type) {
                case 0 -> newLS = new LocationString(text, null);//City.get(text));
                case 1 -> newLS = new LocationString(text, null);//School.get(text));
                default -> throw new AssertionError();
            }
        }
        CACHE.add(newLS);
        return newLS;
    }

    private LocationString(String text, City location) {
        TEXT = text;
        LOCATION = location;
    }

    @Override
    public int length() {
        return TEXT.length();
    }

    @Override
    public char charAt(int index) {
        return TEXT.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return TEXT.subSequence(start, end);
    }

    @Override
    public IntStream chars() {
        return TEXT.chars();
    }

    @Override
    public IntStream codePoints() {
        return TEXT.codePoints();
    }

    public boolean contains(CharSequence cs) {
        return TEXT.contains(cs);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return TEXT.equals(obj);
        } else if (obj instanceof CharSequence) {
            return TEXT.equals(obj.toString());
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public String toString() {
        return TEXT;
    }
}
