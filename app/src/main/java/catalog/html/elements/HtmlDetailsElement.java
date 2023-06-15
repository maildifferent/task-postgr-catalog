package catalog.html.elements;

import catalog.html.HtmlElement;

public class HtmlDetailsElement extends HtmlElement<HtmlDetailsElement> {
  public Boolean open;

  public HtmlDetailsElement() {
    super(HtmlDetailsElement.class, "details");
  }

  public HtmlDetailsElement open(Boolean open) {
    this.open = open;
    return this;
  }
}
