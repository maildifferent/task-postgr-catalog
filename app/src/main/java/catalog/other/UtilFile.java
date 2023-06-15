package catalog.other;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UtilFile {
  static public String readToString(String fileName) {
    var sb = new StringBuilder();
    try (var inputStream = new BufferedReader(new FileReader(fileName));) {
      String str;
      while ((str = inputStream.readLine()) != null) {
        sb.append(str + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException();
    }
    return sb.toString();
  }
}
