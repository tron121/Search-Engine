/**
 * Created by Ronald Egyir on 12/12/2016.
 */

import java.util.Map;
import java.util.HashMap;


public class Sponsors {
    private Map<String, Double> database;

    Sponsors() {
        this.database = new HashMap<String, Double>();
    }

    public void editDatabase(String str, double newRate) throws LowerRateException, InvalidRateException {
        if (database.containsKey(str)) {
            for (String name : database.keySet()) {
                if (name.contains(str)) {
                    // sponsor is already present
                    // keep track of old and new rates
                    database.put(str, checkRate(database.get(name), newRate));
                }
            }
        } else {
            database.put(str, checkRate(0.0, newRate));
        }
    }

    private double checkRate(double old, double newRate) throws LowerRateException, InvalidRateException {
        double value = 0;
        if (newRate > 0.1 || newRate < 0) {
            throw new InvalidRateException(newRate);
        }
        if (newRate < old) {
            throw new LowerRateException(old, newRate);
        }
        return newRate;
    }

    public Map<String, Double> viewData() {
        return this.database;
    }

}
