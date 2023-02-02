package se.gaioa.gasoline.carbon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import se.gaioa.gasoline.carbon.location.LocationString;

public class TinderDatasetStats {

    static void analyze(TinderUserCollection d) {
        //<editor-fold defaultstate="collapsed" desc="general block">
        int[][][] swipeStats = new int[TinderUser.Gender.values().length][TinderUser.SwipeResult.values().length][TinderUser.SwipeReason.values().length];
        int[] usable = new int[3];
        int[] usableFree = new int[3];
        int buggedLifestyle = 0;
        ArrayList<CountedString> interests = new ArrayList<>();
        String[] lsK = new String[24];
        ArrayList<String>[] lsV = new ArrayList[24];
        ArrayList<CountedString> names = new ArrayList<>();
//        int hasCity = 0;
        ArrayList<CountedString> cities = new ArrayList<>();
        ArrayList<CountedString> edus = new ArrayList<>();
        ArrayList<Character> unicodeSearch = new ArrayList<>();
        int[] hasWork = new int[2], hasEducation = new int[2], hasCity = new int[2], hasLabel = new int[2], hasBio = new int[2];
        for (TinderUser u : d) {
            swipeStats[u.GENDER_PROFILE.ordinal()][u.SWIPE_RESULT.ordinal()][u.SWIPE_REASON.ordinal()]++;
            if (u.GENDER_ACCOUNT == TinderUser.Gender.MALE
                    && u.GENDER_PROFILE == TinderUser.Gender.FEMALE
                    && u.GENDER_PROFILE_APPARENT != TinderUser.Gender.MALE
                    && u.SWIPE_RESULT != TinderUser.SwipeResult.UNDEFINED
                    && u.AGENT.isEmpty()
                    && (u.SWIPE_REASON == TinderUser.SwipeReason.UNSPECIFIED
                    || u.SWIPE_REASON == TinderUser.SwipeReason.APPEARANCE
                    || u.SWIPE_REASON == TinderUser.SwipeReason.PERSONALITY
                    || u.SWIPE_REASON == TinderUser.SwipeReason.BOTH)
                    && u.SWIPE_PERFORMER == 0) {
                if (!u.FREEMODE) {
                    usable[u.SWIPE_RESULT.ordinal() - 1]++;
                }
                usableFree[u.SWIPE_RESULT.ordinal() - 1]++;
            }
            for (String intrU : u.INTERESTS) {
                boolean found = false;
                for (CountedString intrC : interests) {
                    if (intrU.equals(intrC.S)) {
                        found = true;
                        intrC.addOne();
                        break;
                    }
                }
                if (!found) {
                    interests.add(new CountedString(intrU));
                }
            }
            for (TinderLifestyle lsU : u.LIFESTYLE) {
                if (u.hasBuggedLifestyle()) {
                    buggedLifestyle++;
                } else {
                    for (int i = 0; i < lsK.length; i++) {
                        if (lsK[i] == null) {
                            lsK[i] = lsU.KEY;
                            lsV[i] = new ArrayList<>();
                            lsV[i].add(lsU.VALUE);
                            break;
                        }
                        if (lsU.KEY.equals(lsK[i])) {
                            if (!lsV[i].contains(lsU.VALUE)) {
                                lsV[i].add(lsU.VALUE);
                            }
                            break;
                        }
                    }
                }
            }
            for (String nameU : u.getNameSplitted(true, false)) {
                boolean found = false;
                for (CountedString nameC : names) {
                    if (nameU.equals(nameC.S)) {
                        found = true;
                        nameC.addOne();
                        break;
                    }
                }
                if (!found) {
                    names.add(new CountedString(nameU));
                }
            }
            if (!u.CITY.isEmpty()) {
                boolean found = false;
                for (CountedString cityC : cities) {
                    if (u.CITY.equals(cityC.S)) {
                        found = true;
                        cityC.addOne();
                        break;
                    }
                }
                if (!found) {
                    cities.add(new CountedString(u.CITY));
                }
            }
            if (!u.EDUCATION.isEmpty()) {
                boolean found = false;
                for (CountedString eduC : edus) {
                    if (u.EDUCATION.equals(eduC.S)) {
                        found = true;
                        eduC.addOne();
                        break;
                    }
                }
                if (!found) {
                    edus.add(new CountedString(u.EDUCATION));
                }
            }
            /*if (u.NAME != null) {
                for (char cU : u.NAME.toLowerCase().toCharArray()) {
                    if () {
                        boolean found = false;
                        for (Character cC : unicodeSearch) {
                            if (cU == cC) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            unicodeSearch.add(cU);
                        }
                    }
                }
            }*/
            byte iHas = -1;
            switch (u.GENDER_PROFILE) {
                case MALE ->
                    iHas = 0;
                case FEMALE ->
                    iHas = 1;
                default -> {
                }
            }
            if (iHas != -1) {
                if (!u.WORK.isEmpty()) {
                    hasWork[iHas]++;
                }
                if (!u.EDUCATION.isEmpty()) {
                    hasEducation[iHas]++;
                }
                if (!u.CITY.isEmpty()) {
                    hasCity[iHas]++;
                }
                if (!u.LABEL.isEmpty()) {
                    hasLabel[iHas]++;
                }
                if (!u.BIO.isEmpty()) {
                    hasBio[iHas]++;
                }
            }
        }
        System.out.println("\n-- SWIPES BY GENDER --");
        for (int i = 0; i < TinderUser.Gender.values().length; i++) {
            System.out.println(TinderUser.Gender.values()[i].name() + ":");
            for (int j = 0; j < TinderUser.SwipeResult.values().length; j++) {
                System.out.println("\t" + TinderUser.SwipeResult.values()[j].name() + ":");
                System.out.println(Arrays.toString(swipeStats[i][j]));
            }
        }
        System.out.println("\n-- USABLE SWIPES --");
        System.out.println("srct: " + usable[0] + " left, " + usable[1] + " right");
        System.out.println("free: " + usableFree[0] + " left, " + usableFree[1] + " right");
        System.out.println("\n-- INTERESTS --");
        interests.sort(null);
        for (CountedString intr : interests) {
            System.out.print(intr.S + " (" + intr.n + "), ");
        }
        System.out.println();
        System.out.println("\n-- LIFESTYLES --");
        for (int i = 0; i < lsK.length; i++) {
            if (lsK[i] == null) {
                break;
            }
            System.out.print(lsK[i] + ": [");
            for (String v : lsV[i]) {
                System.out.print(v + ", ");
            }
            System.out.println("]");
        }
        System.out.println();
        System.out.println("corrupted lifestyle data: " + buggedLifestyle
                + " (" + (buggedLifestyle / (float) d.size()) + " of all)");
        System.out.println("\n-- NAMES --");
        names.sort(null);
        for (int i = 0; i < 400; i++) {
            System.out.print(names.get(i).S + " (" + names.get(i).n + "), ");
        }
        System.out.println();
        System.out.println("\n-- CITIES --");
        System.out.println("total " + (hasCity[0] + hasCity[1])
                + " users has city specified (" + ((hasCity[0] + hasCity[1]) / (float) d.size()) + " of all)");
        cities.sort(null);
        for (int i = 0; i < 300; i++) {
            System.out.print(cities.get(i).S + " (" + cities.get(i).n + "), ");
        }
        System.out.println();
        System.out.println("\n-- EDUCATION --");
        System.out.println("total " + (hasEducation[0] + hasEducation[1])
                + " users has education specified (" + ((hasEducation[0] + hasEducation[1]) / (float) d.size()) + " of all)");
        edus.sort(null);
        for (int i = 0; i < 200; i++) {
            System.out.print(edus.get(i).S + " (" + edus.get(i).n + "), ");
        }
        System.out.println();
        System.out.print("unicode in names: ");
        for (Character c : unicodeSearch) {
            System.out.print(c);
        }
        System.out.println();
        System.out.println("\n-- DATAPOINT FREQUENCY --");
//</editor-fold>
    }

    static void eduUnique(TinderUserCollection d) {
        try {
            ArrayList<LocationString> buffer = new ArrayList<>();
            for (TinderUser u : d) {
                if (!u.EDUCATION.isEmpty()) {
                    boolean found = false;
                    for (LocationString eduC : buffer) {
                        if (u.EDUCATION.equals(eduC)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        buffer.add(u.EDUCATION);
                    }
                }
            }
            OutputStreamWriter O = new OutputStreamWriter(new FileOutputStream(new File("C:\\Users\\Gustav\\Downloads\\edu.txt")), StandardCharsets.UTF_8);
            for (LocationString s : buffer) {
                O.write((s + "\n"));
            }
            O.flush();
            O.close();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    static void cityUnique(TinderUserCollection d) {
        try {
            ArrayList<LocationString> buffer = new ArrayList<>();
            for (TinderUser u : d) {
                if (!u.CITY.isEmpty()) {
                    boolean found = false;
                    for (LocationString cityC : buffer) {
                        if (u.CITY.equals(cityC)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        buffer.add(u.CITY);
                    }
                }
            }
            OutputStreamWriter O = new OutputStreamWriter(new FileOutputStream(new File("C:\\Users\\Gustav\\Downloads\\city.txt")), StandardCharsets.UTF_8);
            for (LocationString s : buffer) {
                O.write((s + "\n"));
            }
            O.flush();
            O.close();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    static void bioLength(TinderUserCollection d) {
        try {
            FileOutputStream O = new FileOutputStream(new File("C:\\Users\\Gustav\\Downloads\\stats.csv"));
            int[][] nUserNChar = new int[501][4];
            float[] total = new float[4];
            for (TinderUser u : d) {
                nUserNChar[u.BIO.length()][u.getOrientation().ordinal()] += 1;
                total[u.getOrientation().ordinal()] += 1;
            }
            O.write(",gay men,straight women,straight men,lesbians\n".getBytes());
            O.write("totals".getBytes());
            for (float t : total) {
                O.write(("," + t).getBytes());
            }
            O.write("\n".getBytes());
            for (int i = 0; i < nUserNChar.length; i++) {
                O.write((String.valueOf(i).getBytes()));
                for (int j = 0; j < nUserNChar[i].length; j++) {
                    O.write(("," + (nUserNChar[i][j] / total[j])).getBytes());
                }
                O.write("\n".getBytes());
            }
            O.flush();
            O.close();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    private static class CountedString implements Comparable<CountedString> {

        private final String S;
        private int n = 1;

        public CountedString(String s) {
            S = s;
        }

        public CountedString(CharSequence cs) {
            S = cs.toString();
        }

        private void addOne() {
            n++;
        }

        @Override
        public int compareTo(CountedString o) {
            return Integer.compare(o.n, n);
        }
    }
}
