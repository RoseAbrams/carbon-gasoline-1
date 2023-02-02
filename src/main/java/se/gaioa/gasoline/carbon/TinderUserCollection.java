package se.gaioa.gasoline.carbon;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class TinderUserCollection implements Iterable<TinderUser>, Serializable {

    private final ArrayList<TinderUser> C = new ArrayList<>();

    public TinderUserCollection(File baseDirectory, LoadingDialog ld) {
        System.out.println("collection build start");
        File[] userDirs = baseDirectory.listFiles();
        int n = 0;
        if (ld != null) {
            ld.reportTotal(userDirs.length);
        }
        short[] times = new short[userDirs.length];
        long startTime;
        short thisTime;
        int avgSum = 0;
        long startTimeOver = System.currentTimeMillis();
        for (int i = 0; i < userDirs.length; i++) {
            if (userDirs[i].isDirectory()) {
                try {
                    startTime = System.currentTimeMillis();
                    C.add(new TinderUser(userDirs[i]));
                    thisTime = (short) (System.currentTimeMillis() - startTime);
                    avgSum += thisTime;
                    times[i] = thisTime;
                    n++;
                    if (ld != null) {
                        ld.reportProgress(n);
                    }
                } catch (Exception e) {
                    System.err.println("error in directory: " + userDirs[i].getAbsolutePath());
                    System.err.println(e);
                    throw e;
                }
            }
        }
        long endTimeOver = System.currentTimeMillis();
        if (ld != null) {
            ld.reportDone();
        }
        System.out.println("collection build end");
        System.out.println("took " + ((endTimeOver - startTimeOver) / 1000) + " seconds");
        Arrays.sort(times);
        System.out.println("\tavg\t" + (avgSum / userDirs.length) + " ms");
        System.out.println("\tmin\t" + times[0] + " ms");
        System.out.println("\t1%ile\t" + times[times.length / 100] + " ms");
        System.out.println("\t10%ile\t" + times[times.length / 10] + " ms");
        System.out.println("\tmedian\t" + times[times.length / 2] + " ms");
        System.out.println("\t90%ile\t" + times[(times.length / 10) * 9] + " ms");
        System.out.println("\t99%ile\t" + times[(times.length / 100) * 99] + " ms");
        System.out.println("\tmax\t" + times[times.length - 1] + " ms");
    }

    public void search(TinderUserSearch tus) {
        for (TinderUser tu : this) {
            tus.offer(tu);
        }
    }

    public TinderUser get(long id) {
        TinderUserSearch s = new TinderUserSearch();
        s.requireID(id);
        search(s);
        if (s.getResults().size() > 1) {
            throw new Error("Duplicate IDs found! Should be impossible.");
        }
        if (s.getResults().isEmpty()) {
            return null;
        } else {
            return s.getResults().get(0);
        }
    }

    public int size() {
        return C.size();
    }

    @Override
    public Iterator<TinderUser> iterator() {
        return C.iterator();
    }
}
