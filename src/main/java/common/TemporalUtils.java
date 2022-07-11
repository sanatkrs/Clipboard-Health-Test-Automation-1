package common;

public class TemporalUtils {

    public static Double getTotalTimeToNowAsDecimalInSeconds(long start) {
        double totalTime = (System.currentTimeMillis() - start);
        return totalTime / 1000;
    }
}
