package timeHelper;

public class TimeHelper {
    public static int expandAbsoluteTime(int hh, int mm, int ss) {
        return ss + 60 * mm + 3600 * hh;
    }

    public static String formatTime(int time) {
        int hh = time / 3600;
        int mm = (time % 3600) / 60;
        int ss = time % 60;
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }

}

