import java.util.Comparator;
import java.util.function.Function;

public class TimeComparator implements Comparator<String> {
    Function<String,Integer> timer= i->i.split(" ")[i.split(" ").length - 1]
            .equals("lightning") ? 5:
            Integer.parseInt(i.split(" ")[i.split(" ").length - 1].substring(0, 2));
    @Override
    public int compare(String o1, String o2) {
            return Integer.compare(timer.apply(o1),timer.apply(o2));
    }
}
