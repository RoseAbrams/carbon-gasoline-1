package se.gaioa.gasoline.carbon;

import java.util.ArrayList;

public class TinderUserSearch {

    private final ArrayList<TinderUser> RESULTS = new ArrayList<>();
    private long id = -1;
    private String name = null;
    private byte age = -1;
    private byte verified = -1;
    private String work = null;
    private String education = null;
    private String city = null;
    private String label = null;
    private String bio = null;
    private TinderUser.Gender gender = null;
    private TinderUser.SwipeResult result = null;
    private TinderUser.SwipeReason reason = null;
    private byte matched = -1;
    private boolean set = false;

    public ArrayList<TinderUser> getResults() {
        return RESULTS;
    }

    public void requireID(long id) {
        this.id = id;
        set = true;
    }

    public void requireName(String name) {
        this.name = name;
        set = true;
    }

    public void requireAge(byte age) {
        this.age = age;
        set = true;
    }

    public void requireVerified(boolean verified) {
        this.verified = (byte) (verified ? 1 : 0);
        set = true;
    }

    public void requireWork(String work) {
        this.work = work;
        set = true;
    }

    public void requireEducation(String education) {
        this.education = education;
        set = true;
    }

    public void requireCity(String city) {
        this.city = city;
        set = true;
    }

    public void requireLabel(String label) {
        this.label = label;
        set = true;
    }

    public void requireBio(String bio) {
        this.bio = bio;
        set = true;
    }

    public void requireGender(TinderUser.Gender gender) {
        this.gender = gender;
        set = true;
    }

    public void requireSwipe(TinderUser.SwipeResult swipeResult) {
        result = swipeResult;
        set = true;
    }

    public void requireReason(TinderUser.SwipeReason swipeReason) {
        reason = swipeReason;
        set = true;
    }

    public void requireMatched(boolean matched) {
        this.matched = (byte) (matched ? 1 : 0);
        set = true;
    }

    public boolean isSet() {
        return set;
    }

    public boolean offer(TinderUser u) {
        if (id == -1 || id == u.TIMESTAMP) {
            if (name == null || (u.NAME != null && u.NAME.contains(name))) {
                if (age == -1 || age == u.AGE) {
                    if (verified == -1 || verified == boolValue(u.VERIFIED)) {
                        if (work == null || u.WORK.contains(work)) {
                            if (education == null || u.EDUCATION.contains(education)) {
                                if (city == null || u.CITY.contains(city)) {
                                    if (label == null || u.LABEL.contains(label)) {
                                        if (bio == null || u.BIO.contains(bio)) {
                                            if (gender == null || (gender == u.GENDER_PROFILE)) {
                                                if (result == null || (result == u.SWIPE_RESULT)) {
                                                    if (reason == null || (reason == u.SWIPE_REASON)) {
                                                        if (matched == -1 || matched == boolValue(u.MATCHED)) {
                                                            RESULTS.add(u);
                                                            return true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static byte boolValue(boolean b) {
        return (byte) (b ? 1 : 0);
    }
}
