package se.gaioa.gasoline.carbon;

import java.io.Serializable;
import java.util.Map;
import org.json.JSONObject;

public class TinderLifestyle implements Serializable {

    public final String KEY;
    public final String VALUE;
    private static final TinderLifestyle[] EMPTY = new TinderLifestyle[0];

    public static TinderLifestyle[] parse(JSONObject mapO) {
        if (mapO.isEmpty()) {
            return EMPTY;
        }
        Map<String, Object> map = mapO.toMap();
        TinderLifestyle[] output = new TinderLifestyle[map.size()];
        int i = 0;
        for (Map.Entry<String, Object> ls : map.entrySet()) {
            output[i] = new TinderLifestyle(ls.getKey(), ls.getValue());
            i++;
        }
        return output;
    }

    private TinderLifestyle(String key, Object value) {
        if (key == null || value == null) {
            throw new NullPointerException("TinderLifestyle: " + key + " > " + value);
        }
        KEY = key.replace("_", " ").intern();
        VALUE = value.toString().intern();
    }

    public String getKeyEmoji() {
        return getKeyEmoji(KEY);
    }

    // scraping point changed around 1662250127529
    public static boolean keysAreSynonyms(TinderLifestyle ls1, TinderLifestyle ls2) {
        return keysAreSynonyms(ls1.KEY, ls2.KEY);
    }

    public static boolean keysAreSynonyms(String key1, String key2) {
        return getKeyEmoji(key1).equals(getKeyEmoji(key2));
    }

    public static String getKeyEmoji(String key) {
        return switch (key) {
            case "" -> "❓";
            case "smoking" -> "🚬";
            case "pets" -> "🐾";
            case "zodiac" -> "🌙";
            case "astrological sign" -> "🌙";
            case "dietary preference" -> "🍕";
            case "appetite" -> "🍕";
            case "workout" -> "🏋";
            case "drink of choice" -> "🍸";
            case "communication style" -> "💬";
            case "social media" -> "📧";
            case "drinking" -> "🥂";
            case "covid comfort" -> "😷";
            case "love language" -> "💌";
            case "education" -> "🎓";
            case "blood type" -> "🩸";
            case "mbti" -> "🧩";
            case "my personality" -> "🙂";  // what?
            case "relationship intent clinking glasses" -> "🥂";
            default -> throw new AssertionError("Unknown lifestyle key: " + key);
        };
    }

    @Override
    public String toString() {
        return getKeyEmoji() + "  " + VALUE;
    }
}
