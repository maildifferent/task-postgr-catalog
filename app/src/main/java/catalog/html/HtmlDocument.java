package catalog.html;

import java.lang.reflect.InvocationTargetException;

/**
 * Method HtmlDocument.createElement is used the same way like
 * document.createElement(...)
 * in javascript.
 */
public class HtmlDocument {
  public static HtmlElement<?> createElement(String tagName) {
    throw new IllegalArgumentException();
  }

  public static <T> T createElement(Class<T> clazz) {
    try {
      var obj = clazz.getDeclaredConstructor().newInstance();
      return obj;
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
      throw new IllegalArgumentException();
    }
  }
}
