package catalog.other;

public class UtilHtml {
  // https://stackoverflow.com/questions/1265282/what-is-the-recommended-way-to-escape-html-symbols-in-plain-java
  // https://stackoverflow.com/a/25228492
  public static String escapeHTML(String str) {
    StringBuilder sb = new StringBuilder(Math.max(16, str.length()));

    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      if (ch > 127 || ch == '"' || ch == '\'' || ch == '<' || ch == '>' || ch == '&') {
        sb.append("&#");
        sb.append((int) ch);
        sb.append(';');
      } else {
        sb.append(ch);
      }
    }
    
    return sb.toString();
  }
}
