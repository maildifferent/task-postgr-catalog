package catalog.other;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class UtilField {
  static public List<Field> getAllFields(Object obj) {
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
      clazz = clazz.getSuperclass();
    }

    return fields;
  }

  static public List<Field> getVisibleFields(Object obj) {
    var result = new LinkedList<Field>();
    var fields = getAllFields(obj);

    for (var field : fields) {
      var modifiers = field.getModifiers();
      if (Modifier.isPrivate(modifiers))
        continue;
      if (Modifier.isStatic(modifiers))
        continue;
      result.add(field);
    }

    return result;
  }
}
