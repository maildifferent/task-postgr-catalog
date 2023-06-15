package catalog.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://stackoverflow.com/questions/8649975/is-there-a-form-of-type-safe-casting-in-java
// https://stackoverflow.com/a/44680634
public abstract class UtilTypify {
  public static <T> List<T> typifyList(List<?> objs, Class<T> clazz) {
    if (objs == null)
      return null;

    var result = new ArrayList<T>(objs.size());
    for (var obj : objs) {
      result.add(clazz.cast(obj));
    }

    return result;
  }

  public static <T> Map<String, T> typifyStringMap(Map<?, ?> objs, Class<T> clazz) {
    if (objs == null)
      return null;

    var result = new HashMap<String, T>();
    for (var entry : objs.entrySet()) {
      var key = entry.getKey();
      var value = entry.getValue();
      result.put((String) key, clazz.cast(value));
    }

    return result;
  }
}
