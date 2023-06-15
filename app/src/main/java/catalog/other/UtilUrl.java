package catalog.other;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * https://stackoverflow.com/questions/607176/java-equivalent-to-javascripts-encodeuricomponent-that-produces-identical-outpu
 */
public class UtilUrl {
  public static String decode(String s) {
    if (s == null) {
      return null;
    }

    String result = null;

    try {
      result = URLDecoder.decode(s, "UTF-8");
    }

    // This exception should never occur.
    catch (UnsupportedEncodingException e) {
      result = s;
    }

    return result;
  }

  public static String encode(String s) {
    String result = null;

    try {
      result = URLEncoder.encode(s, "UTF-8")
          .replaceAll("\\+", "%20")
          .replaceAll("\\%21", "!")
          .replaceAll("\\%27", "'")
          .replaceAll("\\%28", "(")
          .replaceAll("\\%29", ")")
          .replaceAll("\\%7E", "~");
    }

    // This exception should never occur.
    catch (UnsupportedEncodingException e) {
      result = s;
    }

    return result;
  }
}
