import java.util.Hashtable;

public class Country {

    Hashtable<Integer, DataPoint> count;

    public Country() {

        count = new Hashtable<>();
    }

    public void addData(int year, DataPoint d) {

        count.put(year, d);
    }

    public DataPoint getData(int year) {

        return count.get(year);
    }
}
