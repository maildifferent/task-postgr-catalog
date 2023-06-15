package catalog.html;

import catalog.content_html.ContentHtmlClasses;
import catalog.filter_front.FilterFront;
import catalog.filter_front.FilterFrontCheckEq;
import catalog.filter_front.FilterFrontCheckIn;
import catalog.filter_front.FilterFrontInterval;
import catalog.html.elements.HtmlInputElement;
import catalog.html.elements.HtmlLabelElement;

public class HtmlElemFromFilterFront {
  public static HtmlElement<?> conv(FilterFront<?, ?> filterFront) {
    HtmlElement<?> result;
    HtmlInputElement input = (HtmlInputElement) HtmlDocument.createElement(HtmlInputElement.class)
        .id(filterFront.id)
        .addToClassList(ContentHtmlClasses.filter_input_box);

    if (filterFront instanceof FilterFrontCheckEq) {
      input.type("checkbox");
      input.innerHTML("<span class=\""
          + ContentHtmlClasses.filter_input_checkmark
          + "\"></span>");
      result = HtmlDocument.createElement(HtmlLabelElement.class)
          .addToClassList(ContentHtmlClasses.filter_input_label)
          .addLastChild(input);
    }

    else if (filterFront instanceof FilterFrontCheckIn) {
      input.type("checkbox");
      input.innerHTML("<span class=\""
          + ContentHtmlClasses.filter_input_checkmark
          + "\"></span>");
      result = HtmlDocument.createElement(HtmlLabelElement.class)
          .addToClassList(ContentHtmlClasses.filter_input_label)
          .addLastChild(input);
    }

    else if (filterFront instanceof FilterFrontInterval) {
      input.type("number");
      result = input;
    }

    else {
      throw new IllegalArgumentException();
    }

    return result;
  }
}
