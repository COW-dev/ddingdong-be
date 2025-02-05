package ddingdong.ddingdongBE.common.utils;

public class CalculationUtils {

    public static int calculateRate(int count, int totalCount) {
        if (totalCount == 0) {
            return 0;
        }
        return (int) ((double) count / totalCount * 100);
    }
}
