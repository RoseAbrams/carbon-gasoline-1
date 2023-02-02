package se.gaioa.gasoline.carbon;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.io.FileUtils;

public class Duplicate implements Comparable<Duplicate> {

    public final TinderUser U1, U2;
    public final byte CONFIDENCE;
    private static final boolean DEBUG = false;

    private Duplicate(TinderUser u1, TinderUser u2, byte confidence) {
        U1 = u1;
        U2 = u2;
        CONFIDENCE = confidence;
    }

    @Override
    public int compareTo(Duplicate o) {
        return Byte.compare(o.CONFIDENCE, CONFIDENCE);  // falling
    }

    @Override
    public String toString() {
        return U1.TIMESTAMP + " " + U2.TIMESTAMP + " " + CONFIDENCE;
    }

    public static void main(String[] args) throws IOException {
        TinderUserCollection dataset = (TinderUserCollection) SearchFrame.loadSer(new File("C:\\Users\\Gustav\\Downloads\\tinder dataset.ser"));
        ArrayList<Duplicate> dupes = new ArrayList<>();
        boolean matchByPics;
        byte confidence;
        for (TinderUser u1 : dataset) {
            for (TinderUser u2 : dataset) {
                if (u1.TIMESTAMP < u2.TIMESTAMP // thus not match again on second run, nor with itself
                        && u1.NAME != null && u1.NAME.equals(u2.NAME)
                        && (u1.AGE == u2.AGE || u1.AGE == u2.AGE - ((u2.TIMESTAMP - u1.TIMESTAMP) / 31557600000l + 1)) //TESTME
                        && (u1.VERIFIED == u2.VERIFIED || (!u1.VERIFIED && u2.VERIFIED))) {
                    matchByPics = false;
                    confidence = 0;
                    for (File u1pic : u1.getFilesPictures()) {
                        for (File u2pic : u2.getFilesPictures()) {
                            matchByPics = FileUtils.contentEquals(u1pic, u2pic);
                            if (matchByPics) {
                                break;
                            }
                        }
                        if (matchByPics) {
                            break;
                        }
                    }
                    if (u1.BIO.equals(u2.BIO)) {
                        confidence++;
                        if (!u1.BIO.isEmpty()) {
                            confidence++;
                            if (DEBUG && !matchByPics) {
                                System.out.print("");
                            }
                        }
                    }
                    if (u1.EDUCATION.equals(u2.EDUCATION)) {
                        confidence++;
                        if (!u1.EDUCATION.isEmpty()) {
                            confidence++;
                        }
                    }
                    if (u1.WORK.equals(u2.WORK)) {
                        confidence++;
                        if (!u1.WORK.isEmpty()) {
                            confidence++;
                        }
                    }
                    if (u1.CITY.equals(u2.CITY)) {
                        confidence++;
                        if (!u1.CITY.isEmpty()) {
                            confidence++;
                        }
                    }
                    if (u1.LABEL.equals(u2.LABEL)) {
                        confidence++;
                        if (!u1.LABEL.isEmpty()) {
                            confidence++;
                        }
                    }
                    if (u1.SPOTIFY_SONG_TITLE.equals(u2.SPOTIFY_SONG_TITLE) && u1.SPOTIFY_SONG_ARTIST.equals(u2.SPOTIFY_SONG_ARTIST)) {
                        confidence++;
                        if (!u1.SPOTIFY_SONG_TITLE.isEmpty()) {
                            confidence++;
                        }
                    }
                    if (Arrays.equals(u1.INTERESTS, u2.INTERESTS)) {
                        confidence++;
                        if (u1.INTERESTS.length != 0) {
                            confidence++;
                            if (DEBUG && !matchByPics) {
                                System.out.print("");
                            }
                        }
                    }
                    if (u1.N_PICTURES == u2.N_PICTURES) {
                        confidence++;
                    }
                    if (matchByPics) {
                        confidence += 20;
                    }
                    if (confidence > 7) {
                        if (DEBUG && confidence > 9 && confidence < 20) {
                            open(u1.getFilesPictures()[0]);
                            open(u2.getFilesPictures()[0]);
                            open(u1.getFileProfile());
                            open(u2.getFileProfile());
                            System.out.print("");
                        }
                        dupes.add(new Duplicate(u1, u2, confidence));
                    }
                }
            }
        }
        dupes.sort(null);
        try ( FileOutputStream output = new FileOutputStream(new File("C:\\Users\\Gustav\\Downloads\\tinder dupes.txt"))) {
            for (Duplicate d : dupes) {
                output.write((d.toString() + "\n").getBytes());
            }
            output.flush();
        }
    }

    private static void open(File dir) {
        try {
            Desktop.getDesktop().open(dir);
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}
