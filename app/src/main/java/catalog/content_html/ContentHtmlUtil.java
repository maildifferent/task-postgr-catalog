package catalog.content_html;

import catalog.html.HtmlDocument;
import catalog.html.HtmlElement;
import catalog.html.elements.HtmlDetailsElement;
import catalog.html.elements.HtmlLIElement;
import catalog.html.elements.HtmlLabelElement;
import catalog.html.elements.HtmlSummaryElement;
import catalog.html.elements.HtmlUListElement;

public class ContentHtmlUtil {
  static public HtmlDetailsElement genDetailsSection(HtmlElement<?> headElem, String id, HtmlElement<?>[] list) {
    if (id == null || id.equals(""))
      throw new IllegalArgumentException();
    if (headElem.tagName != "summary") // Wrap element into summary tag.
      headElem = HtmlDocument.createElement(HtmlSummaryElement.class).addLastChild(headElem);

    var ul = HtmlDocument.createElement(HtmlUListElement.class);
    for (var elem : list) {
      if (elem.tagName == "input") // Wrap input into label tag.
        elem = HtmlDocument.createElement(HtmlLabelElement.class).addLastChild(elem);
      if (elem.tagName != "li")
        elem = HtmlDocument.createElement(HtmlLIElement.class).addLastChild(elem);
      ul.addLastChild(elem);
    }

    HtmlDetailsElement details = (HtmlDetailsElement) HtmlDocument.createElement(HtmlDetailsElement.class)
        .id(id)
        .addLastChild(headElem)
        .addLastChild(ul);
    return details;
  }
}
