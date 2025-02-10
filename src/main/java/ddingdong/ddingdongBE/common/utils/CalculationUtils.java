package ddingdong.ddingdongBE.common.utils;

public class CalculationUtils {

    public static int calculateRatio(int numerator, int denominator) {
        if (denominator == 0) {
            return 0;
        }
        return (int) ((double) numerator / denominator * 100);
    }

    public static int calculateDifference(int beforeCount, int count) {
        return count - beforeCount;
    }

    public static int calculateDifferenceRatio(int beforeCount, int count) {
        int difference = calculateDifference(beforeCount, count);
        return calculateRatio(difference, beforeCount);
    }
}
