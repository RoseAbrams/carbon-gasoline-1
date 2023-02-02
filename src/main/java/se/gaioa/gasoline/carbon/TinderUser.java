package se.gaioa.gasoline.carbon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import se.gaioa.gasoline.carbon.location.LocationString;

public class TinderUser implements Serializable {

    public final String PARENT_PATH;

    public final String NAME;
    public final byte AGE;
    public final short DISTANCE;
    public final boolean VERIFIED;
    public final boolean SUPERLIKED;
    public final byte N_PICTURES;
    public final String LABEL;
    public final String WORK;
    public final LocationString EDUCATION;
    public final LocationString CITY;
    public final boolean HAS_INSTAGRAM;
    public final boolean HAS_SPOTIFY;
    public final String SPOTIFY_SONG_TITLE;
    public final String SPOTIFY_SONG_ARTIST;
    public final String[] INTERESTS;
    public final String BIO;
    public final TinderLifestyle[] LIFESTYLE;
    public final String BADGE;

    public final long TIMESTAMP;
    public final SwipeResult SWIPE_RESULT;
    public final SwipeReason SWIPE_REASON;
    public final byte SWIPE_PERFORMER;  // [human, script, bot, adviced] - might make enum later
    public final Gender GENDER_ACCOUNT;
    public final Gender GENDER_PROFILE;
    public final Gender GENDER_PROFILE_APPARENT;
    public final LocationString LOCATION_ACCESS;
    public final LocationString LOCATION_CHOICE;
    public final String ACCOUNT;
    public final String AGENT;
    public final boolean FREEMODE;
    public final boolean MATCHED;
    public final String NOTES;

    private transient Image[] picturesLoaded = null;

    private static final JSONObject DEFAULT_METADATA = loadJSON("D:\\Dropbox\\Privat\\postGym program\\gasoline\\jattempt1\\default metadata.json");
    private static final JSONObject DEFAULT_PROFILE = loadJSON("D:\\Dropbox\\Privat\\postGym program\\gasoline\\jattempt1\\default profile.json");
    private static final String[] EMPTY_INTERESTS = new String[0];

    public TinderUser(File directory) {
        //<editor-fold defaultstate="collapsed" desc="long code">
        PARENT_PATH = directory.getParent().intern();
        //check conflicts
        for (File f : directory.listFiles()) {
            if (f.getName().contains("(")) {
//                throw new IllegalStateException("Download conflict: " + directory.getAbsolutePath());
                System.err.println("Download conflict: " + directory.getAbsolutePath());
            }
        }
        //fill defaults
        JSONObject metadata;
        JSONObject profile;
        File metadataF = new File(directory.getAbsolutePath() + "\\metadata.json");
        if (metadataF.exists()) {
            metadata = loadJSON(metadataF);
        } else {
            metadata = DEFAULT_METADATA;
        }
        File profileF = new File(directory.getAbsolutePath() + "\\profile.json");
        if (profileF.exists()) {
            profile = loadJSON(profileF);
        } else {
            profile = DEFAULT_PROFILE;
        }
        for (String key : DEFAULT_METADATA.keySet()) {
            if (!metadata.has(key)) {
                metadata.put(key, DEFAULT_METADATA.get(key));
            }
        }
        for (String key : DEFAULT_PROFILE.keySet()) {
            if (!profile.has(key)) {
                profile.put(key, DEFAULT_PROFILE.get(key));
            }
        }
        //init metadata
        long timestampFolder = Long.parseLong(directory.getName());
        if (metadata.getLong("timestamp") == -1) {
            TIMESTAMP = timestampFolder;
        } else {
            TIMESTAMP = metadata.getLong("timestamp");
            if (TIMESTAMP != timestampFolder) {
                throw new IllegalStateException("Timestamp conflict: Directory reports " + timestampFolder + ", metadata reports " + TIMESTAMP);
            }
        }
        SWIPE_RESULT = SwipeResult.get(metadata.getInt("swipe_result"));
        SWIPE_REASON = SwipeReason.get(metadata.getInt("swipe_reason"));
        int swipePerformerTemp = metadata.getInt("swipe_performer");
        if (swipePerformerTemp < Byte.MIN_VALUE || swipePerformerTemp > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("swipe_performer is not byte vaule");
        }
        SWIPE_PERFORMER = (byte) swipePerformerTemp;
//        if (TIMESTAMP < 1635471876129l) {  // before current v3 gender config
        if (metadata.has("gender_setting")) {  // v2 gender config
            switch (metadata.getInt("gender_setting")) {
                case 0 -> {
                    GENDER_ACCOUNT = Gender.FEMALE;
                    GENDER_PROFILE = Gender.MALE;
                }
                case 1 -> {
                    GENDER_ACCOUNT = Gender.MALE;
                    GENDER_PROFILE = Gender.FEMALE;
                }
                default ->
                    throw new AssertionError("Unexpected value of 'gender_setting' for " + TIMESTAMP);
            }
        } else {
            GENDER_ACCOUNT = Gender.get(metadata.getInt("gender_account"));
            GENDER_PROFILE = Gender.get(metadata.getInt("gender_profile"));
        }
        GENDER_PROFILE_APPARENT = Gender.get(metadata.getInt("gender_profile_apparent"));
        if (metadata.isNull("location_access")) {
            if (metadata.has("location_setting")) {  // v2 location config
                LOCATION_ACCESS = LocationString.city(metadata.getString("location_setting"));
            } else if (TIMESTAMP < 1634221946704l) {  // v1 location config
                LOCATION_ACCESS = LocationString.city("Malmö");
            } else {  // simply undefined for other reason
                LOCATION_ACCESS = null;
            }
        } else {
            LOCATION_ACCESS = LocationString.city(metadata.getString("location_access"));
        }
        LOCATION_CHOICE = LocationString.city(metadata.getString("location_choice"));
        if (TIMESTAMP < 1634652537123l) {  // before alts config
            if (!metadata.getString("agent").isEmpty()) {
                ACCOUNT = metadata.getString("agent").intern();
                AGENT = "".intern();
            } else {
                ACCOUNT = switch (GENDER_ACCOUNT) {
                    case MALE ->
                        "Gustav".intern();
                    case FEMALE ->
                        "Rosa".intern();
                    default ->
                        null;
                };
                AGENT = "".intern();
            }
        } else {
            if (metadata.isNull("account")) {
                ACCOUNT = null;
            } else {
                if (TIMESTAMP < 1644506921424l && GENDER_ACCOUNT == Gender.MALE && GENDER_PROFILE == Gender.MALE) {
                    ACCOUNT = "Gustav (2)".intern();  // i planned to mark samename alts from the start but didn't🤦
                } else {
                    ACCOUNT = metadata.getString("account").intern();
                }
            }
            AGENT = metadata.getString("agent").intern();
        }
        FREEMODE = metadata.getBoolean("freemode");
        MATCHED = metadata.getBoolean("matched");
        if (metadata.has("bookmarked") && metadata.getBoolean("bookmarked")) {  // useless since introducing notes
            if (!metadata.getString("notes").isEmpty()) {
                throw new IllegalArgumentException("Both notes and bookmarked: " + TIMESTAMP);  // should be none
            }
            NOTES = "bookmarked".intern();
        } else {
            NOTES = metadata.getString("notes").intern();  // intern because most are empty
        }
        //init profile
        if (profile.isNull("name")) {
            NAME = null;
        } else {
            NAME = profile.getString("name");
        }
        int ageTemp = profile.getInt("age");
        if (ageTemp < Byte.MIN_VALUE || ageTemp > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("age is not byte vaule");
        }
        AGE = (byte) ageTemp;
        int distanceTemp = profile.getInt("distance");
        if (distanceTemp < Short.MIN_VALUE || distanceTemp > Short.MAX_VALUE) {
            throw new IllegalArgumentException("distance is not short vaule");
        }
        DISTANCE = (short) distanceTemp;
        VERIFIED = profile.getBoolean("verified");
        SUPERLIKED = profile.getBoolean("superliked");
        int nPicturesTemp = profile.getInt("n_pictures");
        if (nPicturesTemp < Byte.MIN_VALUE || nPicturesTemp > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("n_pictures is not byte vaule");
        }
        N_PICTURES = (byte) nPicturesTemp;
        LABEL = profile.getString("label").intern();
        WORK = profile.getString("work");
        EDUCATION = LocationString.school(profile.getString("education"));
        CITY = LocationString.city(profile.getString("city"));
        HAS_INSTAGRAM = profile.getBoolean("has_instagram");
        HAS_SPOTIFY = profile.getBoolean("has_spotify");
        SPOTIFY_SONG_TITLE = profile.getString("spotify_song_title");
        SPOTIFY_SONG_ARTIST = profile.getString("spotify_song_artist");
        JSONArray interestsA = profile.getJSONArray("interests");
        if (interestsA.isEmpty()) {
            INTERESTS = EMPTY_INTERESTS;
        } else {
            INTERESTS = profile.getJSONArray("interests").toList().toArray(new String[0]);
            for (int i = 0; i < INTERESTS.length; i++) {
                INTERESTS[i] = INTERESTS[i].intern();
            }
        }
        BIO = profile.getString("bio");
        LIFESTYLE = TinderLifestyle.parse(profile.getJSONObject("lifestyle"));
        
        if (profile.has("badge_vaccinated") && profile.getBoolean("badge_vaccinated")) {
            BADGE = "vaccinated".intern();
        } else if (profile.has("badge_vaccine_promoter") && profile.getBoolean("badge_vaccine_promoter")) {
            BADGE = "vaccinessavelives".intern();
        } else {
            BADGE = profile.getString("badge").intern();
        }
//</editor-fold>
    }

    private static JSONObject loadJSON(File f) {
        try {
            char[] buffer = new char[2048];
            new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8")).read(buffer);
            return new JSONObject(new String(buffer));
        } catch (FileNotFoundException e) {
            throw new Error("Unchecked nonexistant file: " + f.getAbsolutePath(), e);
        } catch (IOException e) {
            throw new Error("Generic error reading from file: " + f.getAbsolutePath(), e);
        }
    }

    private static JSONObject loadJSON(String path) {
        return loadJSON(new File(path));
    }

    public LocalDateTime getDatetime() {
        return LocalDateTime.ofEpochSecond(TIMESTAMP / 1000,
                (int) (TIMESTAMP % 1000) * 1000000,
                ZoneOffset.of("+1")/* ive only ever swiped in EuropeCentral so far */);
    }

    public File getFileDirectory() {
        return new File(PARENT_PATH + "\\" + TIMESTAMP);
    }

    public File getFileProfile() {
        return new File(getFileDirectory(), "profile.json");
    }

    public File getFileMetadata() {
        return new File(getFileDirectory(), "metadata.json");
    }

    public File[] getFilesPictures() {
        return getFileDirectory().listFiles((pathname) -> {
            return pathname.getName().contains("picture");
        });
    }

    private static final String WORK_SEPR = " at ";

    public String getWorkTitle() {
        if (!WORK.contains(WORK_SEPR)) {
            return null;
        }
        return WORK.substring(0, WORK.indexOf(WORK_SEPR));
    }

    public String getWorkPlace() {
        if (!WORK.contains(WORK_SEPR)) {
            return null;
        }
        return WORK.substring(WORK.indexOf(WORK_SEPR) + WORK_SEPR.length());
    }

    static final Pattern REGEX_SEPR = Pattern.compile("[ -.,]");
    static final Pattern REGEX_ASCII = Pattern.compile("[^\\p{ASCII}]");

    public String[] getNameSplitted(boolean namecase, boolean ascii) {
        if (NAME == null) {
            return new String[0];
        }
        Scanner nameScan = new Scanner(NAME).useDelimiter(REGEX_SEPR);
        ArrayList<String> output = new ArrayList<>();
        while (nameScan.hasNext()) {
            String cName = nameScan.next();
            if (!cName.isBlank()) {
                if (namecase) {
                    char[] caseChange = cName.toCharArray();
                    caseChange[0] = Character.toUpperCase(caseChange[0]);
                    for (int i = 1; i < caseChange.length; i++) {
                        caseChange[i] = Character.toLowerCase(caseChange[i]);
                    }
                }
                if (ascii) {
                    if (!Normalizer.isNormalized(cName, Normalizer.Form.NFD)) {
                        cName = Normalizer.normalize(cName, Normalizer.Form.NFD);
                    }
                    REGEX_ASCII.matcher(cName).replaceAll("");
                }
                output.add(cName);
            }
        }
        return output.toArray(String[]::new);
    }

    public Orientation getOrientation() {
        return Orientation.get(this);
    }

    public boolean hasDistance() {
        return DISTANCE != 0;
    }

    public boolean hasAge() {
        return AGE != 0;
    }
    
    public boolean hasBuggedLifestyle() {  // plenty are bugged due to long-undiscovered scraping issue
        for (TinderLifestyle ls : LIFESTYLE) {
            if (ls.KEY.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static final Pattern SOCIALS_DELIM = Pattern.compile("[: ]+");

    /*public String searchInstagram() {
        String output;
        if (BIO.contains("@")) {
            output = BIO.substring(BIO.indexOf("@"), BIO.indexOf(" ", BIO.indexOf("@")));
        }
    }*/
    public Image getPicture(int index) {
        if (picturesLoaded == null) {
            throw new IllegalStateException("Pictures has not been loaded.");
        }
        return picturesLoaded[index];
    }

    public void loadPictures() {
        if (picturesLoaded != null) {
            return;
        }
        File[] picFs = getFilesPictures();
        picturesLoaded = new Image[picFs.length];
        for (int i = 0; i < picFs.length; i++) {
            picturesLoaded[i] = Toolkit.getDefaultToolkit().createImage(picFs[i].getAbsolutePath());
        }
    }

    public JSONObject toJSON() {
        //<editor-fold defaultstate="collapsed" desc="long code">
        throw new UnsupportedOperationException("TODO");
//</editor-fold>
    }

    public String toProfileText() {
        StringBuilder o = new StringBuilder();
        //<editor-fold defaultstate="collapsed" desc="long code">
        o.append("(has ").append(N_PICTURES).append(" pictures)\n\n");
        o.append(NAME);
        if (hasAge()) {
            o.append("  ").append(AGE);
        }
        if (SUPERLIKED) {
            o.append(" 🔷");
        }
        if (VERIFIED) {
            o.append(" ✅");
        }
        o.append('\n');
        if (!WORK.isEmpty()) {
            o.append("💼 ").append(WORK).append('\n');
        }
        if (!EDUCATION.isEmpty()) {
            o.append("🎓 ").append(EDUCATION).append('\n');
        }
        if (!CITY.isEmpty()) {
            o.append("🏠 Lives in ").append(CITY).append('\n');
        }
        if (!LABEL.isEmpty()) {
            o.append("👤 ").append(LABEL).append('\n');
        }
        if (hasDistance()) {
            o.append("📍 ").append(DISTANCE).append(" kilometers away\n");
        }
        if (!BIO.isEmpty()) {
            o.append("----------\n").append(BIO).append('\n');
        }
        if (LIFESTYLE.length != 0) {
            if (hasBuggedLifestyle()) {
                o.append("[DATA CORRUPTED]");
            } else {
                for (TinderLifestyle tls : LIFESTYLE) {
                    o.append("( ").append(tls).append(" )   ");
                }
                o.append('\n');
            }
        }
        if (INTERESTS.length != 0) {
            o.append("----------\nPassions\n");
            for (String intr : INTERESTS) {
                o.append("( ").append(intr).append(" )   ");
            }
            o.append('\n');
        }
        if (!SPOTIFY_SONG_TITLE.isEmpty()) {
            o.append("----------\nMy Anthem\n");
            o.append(SPOTIFY_SONG_TITLE).append('\n');
            o.append("🟢 ").append(SPOTIFY_SONG_ARTIST).append('\n');
        }
        if (HAS_SPOTIFY) {
            o.append("----------\nMy Top Spotify Artists\n...\n");
        }
        if (HAS_INSTAGRAM) {
            o.append("----------\nRecent Instagram Photos\n...\n");
        }
//</editor-fold>
        return o.toString();
    }

    public String toMetadataText() {
        StringBuilder o = new StringBuilder();
        //<editor-fold defaultstate="collapsed" desc="long code">
        o.append("ID: ").append(TIMESTAMP).append('\n');
        o.append("Saved on ").append(getDatetime()).append('\n').append('\n');
        o.append("This user is ").append(GENDER_PROFILE);
        if (GENDER_PROFILE_APPARENT != Gender.UNSPECIFIED) {
            o.append(" (but presents as ").append(GENDER_PROFILE_APPARENT).append(")");
        }
        o.append(" and appeared to ").append(GENDER_ACCOUNT).append(".\n");
        o.append("This user was presented to the account of ").append(ACCOUNT).append('.');
        if (!AGENT.isEmpty()) {
            o.append(" The swipe was preformed by ").append(AGENT).append('.');
        }
        o.append('\n');
        o.append("The browsing session took place in ").append(LOCATION_ACCESS).append('.');
        if (!LOCATION_CHOICE.isEmpty()) {
            o.append(" The session was displaying users in ").append(LOCATION_CHOICE).append('.');
        }
        o.append('\n').append('\n');
        o.append("This user has ").append(NOTES.isEmpty() ? "no " : "the following 📝").append("notes");
        if (!NOTES.isEmpty()) {
            o.append(": \"").append(NOTES).append('\"');
        } else {
            o.append('.');
        }
        o.append('\n').append('\n');
        o.append("You swiped ").append(SWIPE_RESULT).append(" because ").append(SWIPE_REASON);
        if (FREEMODE) {
            o.append(" (in freemode)");
        }
        o.append(".\n");
        if (SWIPE_PERFORMER != 0) {
            o.append("The swipe was tool-assisted, with tool identifier \"").append(SWIPE_PERFORMER).append("\".\n");
        }
        if (MATCHED) {
            o.append("You matched!✔");
        } else {
            o.append("You did not match.❌");
        }
//</editor-fold>
        return o.toString();
    }

    private static final Font FONT_NAME = new Font("Helvetica", Font.BOLD, 32);
    private static final Font FONT_AGE = new Font("Helvetica", Font.PLAIN, 32);
    private static final Font FONT_BODY = new Font("Helvetica", Font.PLAIN, 16);
    private static final Font FONT_TITLE = new Font("Helvetica", Font.BOLD, 18);
    private static final Font FONT_LIST = new Font("Helvetica", Font.PLAIN, 14);

    public Image toGUI() {
        BufferedImage output = new BufferedImage(640, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics g = output.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 640, 1000);
        //<editor-fold defaultstate="collapsed" desc="long code">
        int x = 20;
        int y = 25;
//        loadPictures();
        // ... image(s)
        y += FONT_NAME.getSize();
        g.setColor(Color.BLACK);
        g.setFont(FONT_NAME);
        g.drawString(NAME, x, y);
        g.setFont(FONT_AGE);
        x += (NAME.length() + 2) * (FONT_NAME.getSize() / 2);
        g.drawString((hasAge() ? AGE : "") + (SUPERLIKED ? " 🔷" : "") + (VERIFIED ? " ✅" : ""), x, y);
        g.setFont(FONT_LIST);
        x = 20;
        g.setColor(Color.GRAY);
        y += FONT_LIST.getSize() * 2;
        if (!WORK.isEmpty()) {
            g.drawString("💼 " + WORK, x, y);
            y += FONT_LIST.getSize() * 2;
        }
        if (!EDUCATION.isEmpty()) {
            g.drawString("🎓 " + EDUCATION, x, y);
            y += FONT_LIST.getSize() * 2;
        }
        if (!CITY.isEmpty()) {
            g.drawString("🏠 Lives in " + CITY, x, y);
            y += FONT_LIST.getSize() * 2;
        }
        if (!LABEL.isEmpty()) {
            g.drawString("👤 " + LABEL, x, y);
            y += FONT_LIST.getSize() * 2;
        }
        if (hasDistance()) {
            g.drawString("📍 " + DISTANCE + " kilometers away", x, y);
            y += FONT_LIST.getSize() * 2;
        }
        g.setFont(FONT_BODY);
        if (!BIO.isEmpty()) {
            g.setColor(Color.BLACK);
            g.drawLine(x, y, output.getWidth() - x, y);
            y += FONT_LIST.getSize() * 2;
            Scanner bioLines = new Scanner(BIO);
            bioLines.useDelimiter("\n");
            while (bioLines.hasNext()) {
                g.drawString(bioLines.next(), x, y);
                y += FONT_LIST.getSize() * 2;
            }
        }
        if (LIFESTYLE.length != 0 && !hasBuggedLifestyle()) {
            g.setColor(Color.BLACK);
            g.setFont(FONT_LIST);
            g.setColor(Color.GRAY);
            StringBuilder lsS = new StringBuilder();
            for (TinderLifestyle ls : LIFESTYLE) {
                lsS.append(ls).append("     ");
            }
            g.drawString(lsS.toString(), x, y);
            y += FONT_LIST.getSize() * 2;
        }
        if (INTERESTS.length != 0) {
            g.setColor(Color.BLACK);
            g.drawLine(x, y, output.getWidth() - x, y);
            g.setFont(FONT_TITLE);
            y += FONT_TITLE.getSize() * 2;
            g.drawString("Passions", x, y);
            y += FONT_TITLE.getSize() * 2;
            g.setFont(FONT_LIST);
            g.setColor(Color.GRAY);
            StringBuilder intrS = new StringBuilder();
            for (String intr : INTERESTS) {
                intrS.append(intr).append("     ");
            }
            g.drawString(intrS.toString(), x, y);
            y += FONT_TITLE.getSize() * 2;
        }
        // more
//</editor-fold>
        g.dispose();
        return output;
    }

    @Override
    public String toString() {
        return TIMESTAMP + " | " + NAME + " " + AGE;
    }

    public static enum SwipeResult {
        UNDEFINED, LEFT, RIGHT, SUPER;

        public static SwipeResult get(int i) {
            return values()[i + 1];
        }
    }

    public static enum SwipeReason {
        UNDEFINED, UNSPECIFIED, APPEARANCE, PERSONALITY, BOTH, HORNY, CURIOUS;

        public static SwipeReason get(int i) {
            return values()[i + 1];
        }
    }

    public static enum Gender {
        UNDEFINED, UNSPECIFIED, MALE, FEMALE, ENBY;  // crossed?

        public static Gender get(int i) {
            return values()[i + 1];
        }
    }

    public static enum Orientation {  // only binary so far
        GAY_MAN {
            @Override
            public Gender getIdentity() {
                return Gender.MALE;
            }

            @Override
            public Gender getPreference() {
                return Gender.MALE;
            }
        }, STRAIGHT_MAN {
            @Override
            public Gender getIdentity() {
                return Gender.MALE;
            }

            @Override
            public Gender getPreference() {
                return Gender.FEMALE;
            }
        }, STRAIGHT_WOMAN {
            @Override
            public Gender getIdentity() {
                return Gender.FEMALE;
            }

            @Override
            public Gender getPreference() {
                return Gender.MALE;
            }
        }, LESBIAN {
            @Override
            public Gender getIdentity() {
                return Gender.FEMALE;
            }

            @Override
            public Gender getPreference() {
                return Gender.FEMALE;
            }
        };

        public abstract Gender getIdentity();

        public abstract Gender getPreference();
        
        public static Orientation get(TinderUser u) {
            switch (u.GENDER_ACCOUNT) {
                case MALE -> {
                    switch (u.GENDER_PROFILE) {
                        case MALE -> {return GAY_MAN;}
                        case FEMALE -> {return STRAIGHT_MAN;}
                        default -> throw new AssertionError();
                    }
                }
                case FEMALE -> {
                    switch (u.GENDER_PROFILE) {
                        case MALE -> {return STRAIGHT_WOMAN;}
                        case FEMALE -> {return LESBIAN;}
                        default -> throw new AssertionError();
                    }
                }
                default -> throw new AssertionError();
            }
        }
    }
}
