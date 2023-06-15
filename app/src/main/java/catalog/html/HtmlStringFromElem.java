package catalog.html;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HtmlStringFromElem {
  public static String conv(HtmlElement<?> elem) {
    String attributes = convAttributesToString(elem);

    String result = "<" + elem.tagName + " " + attributes + ">"
        + (elem.innerText == null ? "" : elem.innerText)
        + (elem.innerHTML == null ? "" : elem.innerHTML)
        + "{{children}}"
        + "</" + elem.tagName + ">";
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Util private.
  //////////////////////////////////////////////////////////////////////////////
  private static String convAttributesToString(HtmlElement<?> elem) {
    LinkedList<String> attributes = new LinkedList<>();
    var notAttributes = new HashSet<String>(Arrays.asList("tagName", "innerText", "innerHTML"));
    var fields = getFields(elem);

    for (var field : fields) {
      field.setAccessible(true);
      String fieldName = field.getName();
      if (notAttributes.contains(fieldName)) {
        continue;
      }

      Object value;
      try {
        value = field.get(elem);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
        throw new IllegalArgumentException();
      }
      if (value == null)
        continue;
      String convertedValue = convAttributeValueToString(value);
      if (convertedValue == null)
        continue;

      fieldName = removeTrailingZero(fieldName);
      attributes.add(fieldName + "=\"" + convertedValue + "\"");
    }

    String result = attributes.stream().collect(Collectors.joining(" "));
    return result;
  }

  private static List<Field> getFields(HtmlElement<?> obj) {
    var fields = new LinkedList<Field>();
    Class<?> clazz = obj.getClass();
    Field[] fieldArr;

    while (clazz != null && clazz != Object.class) {
      fieldArr = clazz.getDeclaredFields();
      for (var field : fieldArr) {
        var modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers))
          continue;
        fields.add(field);
      }
      if (clazz == HtmlElement.class)
        return fields;
      clazz = clazz.getSuperclass();
    }
    throw new IllegalArgumentException();
  }

  private static String convAttributeValueToString(Object value) {
    String result;
    if (value instanceof String casted) {
      result = casted;
    } else if (value instanceof Boolean || value instanceof Integer) {
      result = value.toString();
    } else if (value instanceof List castedList) { // E.g. classList attribute.
      if (castedList.size() < 1)
        return null;
      var list = new ArrayList<String>(castedList.size());
      for (var item : castedList) {
        if (item instanceof String castedString) {
          list.add(castedString);
        } else {
          throw new IllegalArgumentException();
        }
      }
      result = list.stream().collect(Collectors.joining(" "));
    } else {
      throw new IllegalArgumentException();
    }
    return result;
  }

  /**
   * Could not use some HTML attributes in objects since the names are reserved.
   * For example word: "for". Added trailing 0 to field name in object.
   * 
   * @param fieldName
   * @return
   */
  private static String removeTrailingZero(String fieldName) {
    if (fieldName.charAt(fieldName.length() - 1) == '0') {
      fieldName = fieldName.substring(0, fieldName.length() - 1);
    }
    return fieldName;
  }
}
