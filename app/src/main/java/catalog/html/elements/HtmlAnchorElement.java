package catalog.html.elements;

import catalog.html.HtmlElement;

public class HtmlAnchorElement extends HtmlElement<HtmlAnchorElement> {
  public String href;

  public HtmlAnchorElement() {
    super(HtmlAnchorElement.class, "a");
  }

  public HtmlAnchorElement href(String href) {
    this.href = href;
    return this;
  }
}

