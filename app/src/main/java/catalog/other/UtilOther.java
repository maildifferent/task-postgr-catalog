package catalog.other;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilOther {
  /**
   * Convert list of tuples to map. Tuple - is a list with two elements.
   */
  public static <KeyT, ValT> Map<KeyT, ValT> convTuplesToMap(List<?> tuples, Class<KeyT> keyCl, Class<ValT> valCl) {
    var result = new HashMap<KeyT, ValT>();
    for (var obj : tuples) {
      List<?> tuple = (List<?>) obj;
      if (tuple.size() != 2)
        throw new IllegalArgumentException();
      Object key = tuple.get(0);
      Object val = tuple.get(1);
      result.put(keyCl.cast(key), valCl.cast(val));
    }
    return result;
  }
}
