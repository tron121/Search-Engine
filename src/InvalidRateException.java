/**
 * Created by Ron on 12/12/2016.
 */

// throws if the entered rate is lower than 0 or greater than 0.1
public class InvalidRateException extends Exception {
    double rate;

    InvalidRateException(double rate) {
        this.rate = rate;
    }

}
