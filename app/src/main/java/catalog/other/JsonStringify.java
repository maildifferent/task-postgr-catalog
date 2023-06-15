package catalog.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonStringify {
  // public static String stringifySimpleObject(Object obj) {
  //   var fields = UtilField.getVisibleFields(obj);
  //   var strings = new ArrayList<String>(fields.size());

  //   for (var field : fields) {
  //     field.setAccessible(true);
  //     try {
  //       Object value = field.get(obj);
  //       String str = JsonStringify.stringify(value);
  //       strings.add("\"" + field.getName() + "\":" + str);
  //     } catch (IllegalArgumentException | IllegalAccessException e) {
  //       e.printStackTrace();
  //       throw new IllegalArgumentException();
  //     }
  //   }

  //   String result = strings.stream().collect(Collectors.joining(","));
  //   return "{" + result + "}";
  // }

  public static <T> String stringify(T value) {
    // Primitive.
    if (value instanceof String casted) {
      return escapeString(casted);
    } else if (value instanceof Integer || value instanceof Boolean) {
      return value.toString();
    } else if (value == null) {
      return "null";
    } else if (value instanceof Byte // Other primitive types are not implemented.
        || value instanceof Short
        || value instanceof Long
        || value instanceof Float
        || value instanceof Double
        || value instanceof Character) {
      throw new IllegalArgumentException();
    }

    // Array.
    else if (value instanceof String[] castedArr) {
      String str = Arrays.stream(castedArr).map(elem -> escapeString(elem)).collect(Collectors.joining(","));
      return "[" + str + "]"; // ["qqq","www"]
    } else if (value instanceof Integer[] || value instanceof Boolean[]) {
      var castedArr = (Object[]) value;
      String str = Arrays.stream(castedArr).map(elem -> elem.toString()).collect(Collectors.joining(","));
      return "[" + str + "]"; // [1, 2]
    } else if (value instanceof int[] castedArr) {
      String str = Arrays.stream(castedArr).mapToObj(elem -> String.valueOf(elem)).collect(Collectors.joining(","));
      return "[" + str + "]"; // [1, 2]
    } else if (value.getClass().isArray()) { // Other types of arrays are not implemented.
      throw new IllegalArgumentException();
    }

    // List.
    else if (value instanceof List<?> list) {
      var strings = new ArrayList<String>(list.size());
      for (var item : list) {
        strings.add(stringify(item));
      }
      String str = String.join(",", strings);
      return "[" + str + "]";
    }

    // Map.
    else if (value instanceof Map<?, ?> map) {
      var strings = new ArrayList<String>(map.size());
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        Object mapKey = entry.getKey();
        Object mapValue = entry.getValue();
        if (!(mapKey instanceof String))
          throw new IllegalArgumentException();
        strings.add(stringify(mapKey) + ":" + stringify(mapValue));
      }
      String str = String.join(",", strings);
      return "{" + str + "}";
    }

    // Set.
    else if (value instanceof Set<?> set) {
      var strings = new ArrayList<String>(set.size());
      for (var item : set) {
        strings.add(stringify(item));
      }
      String str = String.join(",", strings);
      return "[" + str + "]";
    }

    // Else.
    else {
      throw new IllegalArgumentException();
    }
  }

  private static String escapeString(String str) {
    String result = str.replace("\\", "\\\\");
    result = result.replace("\"", "\\\"");
    result = result.replace("\n", "\\n");
    return "\"" + result + "\"";
  }
}
