package emailfilter.filterevidence;

import java.util.List;

public interface  Filter<T> {
    List<T> filt(List<T> list);
    char getOperation();
}
