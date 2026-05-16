package timeUtils;

public class TimeOperators {
    public static int expandAbsoluteTime(int hh, int mm, int ss) {
        return ss + 60 * mm + 3600 * hh;
    }

    public static int expandRelativeTime(int hh1, int mm1, int ss1, int hh2, int mm2, int ss2) {
        int time1 = expandAbsoluteTime(hh1, mm1, ss1);
        int time2 = expandAbsoluteTime(hh2, mm2, ss2);
        return time2 - time1;
    }
    public static String formatTime(int time) {
        int hh = time / 3600;
        int mm = (time % 3600) / 60;
        int ss = time % 60;
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }

}

