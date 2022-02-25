/**
 * Created by Ron on 12/12/2016.
 */


public class LowerRateException extends Exception {
    double oldRate;
    double newRate;

    LowerRateException(double old, double newRate) {
        this.oldRate = old;
        this.newRate = newRate;
    }
}
