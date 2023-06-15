package catalog.html;

import java.util.Arrays;
import java.util.LinkedList;

public class HtmlStringFromElemTree {
  public static String conv(HtmlElement<?> root) {
    String template = "{{" + root.uid.toString() + "}}";
    var elems = new LinkedList<HtmlElement<?>>(Arrays.asList(root));
    var queue = new LinkedList<HtmlElement<?>>();

    while (!elems.isEmpty()) {
      for (HtmlElement<?> currElem : elems) {
        String html = HtmlStringFromElem.conv(currElem);

        HtmlElement<?> child = currElem.firstChild;
        while (child != null) {
          html = addChildPlaceholderTo(html, child.uid.toString());
          queue.add(child);
          child = child.nextSibling;
        }

        html = removeChildrenPlaceholderFrom(html);
        template = template.replace("{{" + currElem.uid.toString() + "}}", html);
      }
      elems = queue;
      queue = new LinkedList<HtmlElement<?>>();
    }

    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Util private.
  //////////////////////////////////////////////////////////////////////////////
  private static String addChildPlaceholderTo(String template, String placeholder) {
    var result = template.replace("{{children}}", "\n{{" + placeholder + "}}{{children}}");
    return result;
  }

  private static String removeChildrenPlaceholderFrom(String template) {
    var result = template.replace("{{children}}", "");
    return result;
  }
}
